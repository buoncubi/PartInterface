# Part-Interface Prototype
This is a framework to represent **parts** in an OWL ontology.
Parts are made of **features** that can be compared with *target* values, and the comparison is made by the mean
of general-purposes **Kernels**.
The objective of such a comparison is to rank parts base on the affinity with some target features.

## Dependencies & Installatioased on the jar file available in the `\lib` folder, while OWL-API is loaded through 
the configuration in the `\build.gradle`.
 
The development and test has been done using: Java 8, 
[Intellij IDE](https://www.jetbrains.com/idea/), 
[JUnit Jupiter](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api/5.7.0).

To install the framework run `git clone` and open the repository with an IDE suitable for Java.

See the sections below to run examples provided in the repository. 

## Ontology
The framework relies on an OWL ontology to store parts and perform affinity-based query.
Nevertheless, the current version do not use OWL-based reasoning for improving computation time, to the
disadvantage of explicitness.

The ontology contains OWL Classes specifying different *types*, e.g., `MOTOR`. 
Each class is not define in logic terms, and it only contains parts that are asserted to be of a given type.

Each class might contains OWL Instances `I`, each associated to a *Part*, e.g., `I:MOTOR`.
Each instance `I` has some features, which are represented as OWL Data Properties, e.g., `hasWeight(I,57)`, 
`hasDescriptor(I,"....")`; more in general a par *relation-value* `r(I,v)`, where `r` is identified by a `key`.

In the ontology, each property is defined in terms of *datatypes* that they concern, i.e., `Float`, `Double`, `Integer`, 
`Long`, i.e., `Number`, `String` and`Boolean`.
In other words, two instances `I` and `J` cannot be specified with the same property `r ≡ hasWeight` but different 
datatypes, e.g, `Long` and `String`.

Also, it is assumed that each property `r` occurs only once for each Instance `I`, e.g., `I` cannot have two weights.
Moreover, a property can also concern a `Range` data, which are defined based on a `min` and a `max` values as `Number`.
In this case, `I` would be described with two properties instead of one, i.e., 
`r¹ ≡ hasWeight-MiN-` and `r² ≡ hasWeight-MaX-`.

In general, each part `I` should be defined with:
 1. a name of the instance `I` as a string, i.e., an identifier; 
 2. the name of the type classifying the part, e.g., `MOTOR`,
 3. an unspecified amount of features, each represented through 
    - a property `r` or two properties `r¹` and `r²`,
    - the relative value(s) with a consistent datatype.

Some simple examples of OWL ontologies are avaliable in the `/src/test/resources` folder, which can be opened
and inspected through the [Protégé](https://protege.stanford.edu/) editor.

## Software Architecture
The framework is divided is packages containing:
 - `core`: general interfaces and abstract classes,  
 - `implementation`: the implementation of the `core` interfaces, 
   - `owlInterface`: implementation based on OWL ontology,
      - `owloopDescriptor`: OWLOOP descriptor to efficiently interface with the ontology,
   - `kernel`: implementation to evaluate feature involving different datatypes,
 - `utility`: auxiliary classes. 

![Alt text](./doc/umlCore.png?raw=true "Fig. 1: The Core Architecture")

The architecture defines different components, involving the following class as shwon in the
UML above.

`BaseFeature<V>`: define a relation-value pair between a property with a given name `r` and a 
value `v` of datatype `V`.

`WritableFeature<V>`: is an abstract class that extends `BaseFeature` to add functions for storing 
a feature in an ontology.
`OWLFeature<V>`: extends `WritableFeature` and implements the abstract function using OWLOOP,
i.e., it adds (and remove) from (to) the ontology relation-value pair involving`r` and `v`.

`OWLRangeFeature`: extends `OWLFeature` to deal with `Range` datatype, i.e.,  
i.e., it adds (and remove) from (to) the ontology `r¹(I,min)` and `r²(I,max)`.

`BaseKernel<V,P>`: is an abstract class that extends `BaseFeature` to add function for evaluating an 
`actual` feature (given as input `X`) and `this` target. The framework is made to allow the 
definition of general-purpose kernel, with generic parameters `P` as detailed below.

`BasePart<F>`: represents a Part through a `String` identifier (e.g., `I`), a type (e.g., `MOTOR`),
a set of `F extends BaseFeature<?>` and relative set of `BaseKernel`.
It defines abstract functions to add (and remove) to (from) the ontology the part, and to evaluate
its features based on relative kernels.

`OWLPart`: extends `BasePart` to add (and remove) the part `I` to (from) the ontology
based on OWLOOP.

`Part`: extends `OWLPart` to query the affinities and aggregation of each `BaseKernel` 
(which represent the target) and relative `this.features` (which is the actual value of the part).
The evaluation returns an affinity value in [0,1], which is computed as the weighted average 
between the results of the evaluation of each kernel.

`Affinity`: is the class returned by `Part.queryAffinities()`, which contains an evaluation in [0,1]
associated with the Part identifier.

## Kernel-Based Reasoning
Each kernel represents a target value for a feature, which can be of different datatype.
Therefore, the `BaseKernel` interface can be extended to tackle different features.
The figure below shows the implemented kernels, which description follows.

![Alt text](./doc/umlKernel.png?raw=true "Fig. 2: The Kernl Interface")

`KernelBoolean`: it evaluates two `OWLFeature<Boolean>` values, and it returns 1 if those are equal, 
0 otherwise.
It can compare symbols as "true" and "false", or integer, i.e., `true` if the number is greater 
than zero.
Also, it does not require any additional parameters. 

`KernelString`: it evaluates two `OWLFeature<String>` values, and it returns 1 if those are equal,
0 otherwise. 
In addition, it does not require any additional parameters.

`KernelPoint`: it evaluates two `OWLFeature<Number>` values by the means of a fuzzy membership
function. 
The latter is given as a parameter, i.e., `KernelPointParam`, which contains a list of
point `(x:value,y:degree)` representing the discontinuities of the function, which will
be reconstructed using linear interpolation.

`KernelRange`: it evaluates a combination of two `OWLFeature<Range>`, where a `Range` can be derived also from 
a `Number` where `min = max`.
Given `this` target range and an actual range `X`, it computes the mutual relationships within ranges as 
described by `RangeEval` as:
 - `WITHIN`: when the `X` range is entirely inside `this` range. 
 - `OVERLAPS`: when the `X` range excesses `this` range both in the `min` and `max` limits.
 - `OVERLAPS_MIN`: when the `X` range excesses the `min` limit of `this` range 
     but does not excess the `max` limit.
 - `OVERLAPS_MAX`: when the `X` range excesses the `max` limit of `this` range 
     but does not excess the `min` limit.
 - `OUTSIDE`: when the `X` range is not within `this` range.
 - `UNKNOWN`: when an error occurs.  

The `KernelRange` evaluation returns a value within [0,1] that is related to thw amount of range `X` within the
limits of `this` range.
In other words, it returns 1 if the `OVERLAPS` cases occurs, 0 if `OUTSIDE` occurs and a value in (0,1) if the 
other cases occur.
Finally, also this kernel does not require any parameters, but the `KernelRange` needs specific implementations
in `OWLRangeFeature` and `OWLPart` introduced above. 

## Tests & Examples
The repository is provided with the test-like environment that Intellij uses by default.
Nevertheless, they are not meant to perform unit-testing since the result of the test should be manually
inspected.
For this reason they can be easily converted to `main` executable if necessary.

More imparticolar, the repository provides in the `src/test/java7...` folders executable concerning:
 - the feature evaluation based on all the kernels presented above, i.e, `KernelBooleanTest`,
   `KernelBooleanTest`, `KernelPontTest` and `KernelRangeTest`;
 - adding and removing part from an ontology based on OWLOOP, i.e., `OWLFeatureTest` and `OWLPartTest`, which
   generate ontologies wiht the corresponding name in the `src/test/resources/` folder;
 - the affinity evaluation between the parts in the ontology and a target composed by a set of kernels, 
   i.e., `PartTest`; 
 - an interface to add parts and query affinities based on a CSV file, i.e., `ExampleInterface`. 
   This test involves both the situation where the ontology is saved on file or store on memory.  

While the provided test are for developing mostly, the last executable might be more relevant for 
the deployment of the framework on further applications.
Therefore, follows the details of the relevant functions for the `ExampleInterface` executable.
 
## Ontology and Logging Management

The repository provides the static `Configure` utility, which can `createOntology()` and `loadOntology()`
given an identifier (i.e., `ontName`) and a directory where the ontology might be stored.
These functions return an `ontology` from which is possible to interact with the OWL-API, e.g,
`synchronizeReasoner()`, `saveOntology()` or `getIndividualB2Class()` where `B2` stands for "belong to".

The OWL and OWLOOP API uses the Java logging utilities, and they might print a warning during framework boostrap.
On the other hand, the architecture implemented in this repository uses a simple `StaticLogger` utility to
print on screen `VERBOSE`, `INFO`, `WARNING` and `ERROR` logs, which level can be set with `StaticLogger.setLevel()`.

## Inputs Requirements & Part Definition
By the definition of parts in the ontology, the framework requires as input a *matrix*, which ca be parsed in CSV 
format or be set through API.
More generally, the inputs of `n` parts can be depicted as the table below, where:
 - `ID` → is an optional field associated to the identifier of a part `I`,
 - `TYPE` → is the name of the OWL class representing the type of `I`,
 - `r1,r2..,rj,...,rm` → are the features that a part can have (e.g., `weight`, `frequenct`, etc.),
   and some `vij` might be empty.
   Each `rj` feature can be shared among individual `Ii` of different `TYPE` but it represents always
   values `vij` of the same datatype, e.g., `r1`concerns `Number`, `r2` concerns `String`, etc.
   Only exception is for `Range` datatypes that can involve `Number` as well.

| `ID`* |  `TYPE`* |  r*1* |  r*2* | ... |  r*j* | ... |  r*m* |
|:-----:|:--------:|:-----:|:-----:|:---:|:-----:|:---:|:-----:|
|  I*1* |  `MOTOR` | v*11* | v*12* | ... | v*1j* | ... | v*13* |
|  I*2* | `SPRING` | v*21* | v*22* | ... | v*2j* | ... | v*23* |
|  ...  |    ...   |  ...  |  ...  | ... |  ...  | ... |  ...  |
|  I*i* |  `MOTOR` | v*i1* | v*i2* | ... | v*ij* | ... | v*1m* |
|  ...  |    ...   |  ...  |  ...  | ... |  ...  | ... |  ...  |
|  I*n* |  `MOTOR` | v*2n* | v*2n* | ... | v*in* | ... | v*nm* |

The repository provides a `CSVFile` utility which can read such a data format.
In particular, it allows the `ID` and `TYPE` columns to be encoded in the CSV or being set from API.
Also, it can process CSV with header strings specifying the keys `ID`, `TYPE`, `rj` in the first line, 
or the keys can be set from API.
Finally, it requires to specify the datatype associated to each column in the same order as presented in the CSV.
Therefore, an example showing how to read a CSV (e.g., `src/test/resources/dataExampleNoHeader.csv`) file is:
```java
// Define optional header with a relative datatype.
String[] header = new String[]{"ID", "TYPE", "code", "freq", "weight", "available", etc.};
Class<?>[] datatypes = new Class[]{Long.class, String.class, String.class, Range.class, Float.class, Boolean.class, etc.};

// Parse file
CSVFile csv = CSVFile.readCsv("path/to/file.csv", datatypes, header);
// Eventualy let the `header` be specified in the first line of the CSV file.
//CSVFile csv = CSVFile.readCsv("path/to/fileWithHeadher.csv", datatypes);
List<Set<OWLFeature<?>>> data = csv.getData();
```

Alternatively, it is possible to encode the features of a part through API (see `OWLPartTest`) as, for example,
```java
// Define some features (shared to all parts for simplicity).
Set<OWLFeature<?>> features = new HashSet<>();
features.add(new OWLRangeFeature("r1", new Range(2f,3f), ontology));
features.add(new OWLFeature<>("r2", 2L, ontology));
features.add(new OWLFeature<>("r3", 3, ontology));
features.add(new OWLFeature<>("r4", "F4", ontoRef));
features.add(new OWLFeature<>("r5", true, ontoRef));
OWLPart part = new Part("I1", "MOTOR", featurs, ontology);
```
The `part` can than be added (or removed) to (from) the ontology, and it can be used 
to query affinities by the means of kernels.

Remarkably, when parts or features are added (or removed) to (from) an ontology that should be used 
again without storing in a file, the OWL reasoner should be updated with `ontology.synchronizeReasoner()`.
Nevertheless, this operation might be computationally expencive, and it should be used as less as possible.

On the other hand, if the ontology is saved into a file and reopened within the same application,
the `OWLReference` should be closed by using:
```java
OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); 
```

## Query Part Affinities
To query part affinities a `OWLPart` and a set of kernels is required. 
Each extension of `BaseKernel` encodes a target `BaseFeature`, and it defines the evaluating method as well as
the related parameters if necessary.

Kernels must be set through API and, based on the latter code snippet (see also `ExampleInterface` 
and `PartTest`), they might be defined as
```java
float weight = 1;
Set<BaseKernel<?,?>> kernels = new HashSet<>();
kernels.add(new KernelRange("r1", new Range(0,5), weight));
kernels.add(new KernelPoint("r2", 0L, getKernelPointParams(), weight));
kernels.add(new KernelString("r3", new Range(4,6)));
kernels.add(new KernelBoolean("r4", "A4-106V"));
kernels.add(new KernelBoolean("r5", 0), weight);
```
Remarkably, the kernels address a subset of the part features, or features that are not defined in the ontology.
In these case, the related evaluation is discarded for not affecting the affinity degree.
Also, note that the datatype associated to a kernel should be consistent with the datatype of a feature with
the same key `rj`.

Given a set of target kernels, the affinity of a given part can be queried by using:
```java
// Retrieve an Affinity, i.e., an <Ii,di> pair, where `di` is the affinity degree in [0,1].
Affinity affinity = part.queryAffinity(kernels);  
```
To using the above function effectively, it is possible to retrieve all the parts `Ii` of a given `TYPE` with:
```java
Set<Part> parts = OWLPart.readParts("MOTOR", ontology);
```
and iterate to define a `Set<Affinity> affinities`.
Then, the affinities can be ranked with 
```java
BasePart.sortAffinities(affinities);
```
where the last value concerns the part with the best activity.

## Designed Extensions & Discussion
The framework has designed to be general in terms of datatypes applied to features and kernels.
Indeed, it only requires that datatypes are consistent among each other, but it does not limit their 
implementation.
Therefore, it is possible to rely on flexible parts and feature definition.  

The current implementation does not use the logic-based reasoning associate to each ontology, but this
capability might be used in further application to structure parts `TYPE`, for instance.
However, if the representation of the features in the ontology, the framework might require some changes of the
`OWLPart` and `OWLFeatures` class, similarly to the implementation related to `Range` (e.g., `OWLRangeFeature`, 
see the UML showing the kernels).

Nevertheless, this implementation decouples base interface and implementation related to OWL.
Therefore, it might be used as a base to implement the framework using other data representation formalisms.

The framework allows using ontologies created at run time, and the most extendible classes are the Kernels. 
Indeed, it has been designed to support the developing of more sophisticated kernels.
The snippet below shows the template of a customizable kernel:
```java
public class MyKernel extends BaseKernel<`datattype`, `parametertype`> {
    public MyKernel(String targetKey, `datatype` targetValue, `parametertype` parameter) {
        super(targetKey, targetValue, parameter);  // The default weight is 1
    }
    public MyKernel(String targetKey, `datatype` targetValue, `parametertype` parameter, float weight) {
        super(targetKey, targetValue, parameter, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // Return a value within [0,1], or null to discart this result from the part affinity evaluation.
        // Use `this` as the target value associated to compare with `actual`.
    }

    @Override
    protected <X extends BaseFeature<?>> boolean checkType(X actual) {
        // Eventually, retuns the feature datatypes `X` that are accepted as actual value.
        // By defult, it returns `true` if `X` is equal to the specified `datatype`.
        return super.checkType(actual); 
    }
}
```
-----
Luca Buoncompagni,  
University of Genoa & Teseo srl.,  
August, 2021.