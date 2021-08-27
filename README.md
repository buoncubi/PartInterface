# Part-Interface Prototype

---
This is a framework to represent **Parts** in an OWL ontology.
Parts are made of **Features** that can be compared with *target* values, and the comparison is made by the mean
of general-purposes **Kernels**.
The objective of such a comparison is to rank Parts base on the *affinity* of its Features with some target Kernels.

## Contents
1. *Dependencies & Installation* → Brief procedure.
2. *Ontology* → Brief description of the knowledge representation and its main concepts:  
   2.1. *Part Type*,  
   2.2. *Part Individual & Feature*,  
   2.3. *Part Feature Datatype*,  
   2.4. *Ontology Part Concepts*.  
3. *Software Architecture* → Description of the main components of the architecture:  
   3.1. `BaseFeature<V>`,  
   3.2. `WritableFeature<V>`,  
   3.3. `OWLFeature<V>`,   
   3.4. `OWLRangeFeature`,  
   3.5. `BaseKernel<V,P>`,   
   3.6. `BasePart<F>`,  
   3.7. `OWLPart`,  
   3.8. `Part`,  
   3.9. `Affinity`.
4. *Kernel-Based Reasoning* → Description of the kernels and their comparing methods:  
   4.1. `KernelBoolean`,  
   4.2. `KernelString`,  
   4.3. `KernelPoint`,  
   4.4. `KernelRange`.  
5. *Tests & Examples* → Introduction to executables, and some simple snipped of code:  
   5.1. *Ontology and Logging Management*,  
   5.2. *Inputs Requirements*,  
   5.3. *Part Definition in the Ontology*,  
   5.4. *Target Kernels Definition*,  
   5.5. *Query Part Affinity*.  
6. *Designed Extensions & Discussions* → Discussion of possible improvements and limitations, 
               as well as the template to implement further types of new Kernels.
---

## 1. Dependencies & Installation
The framework depends on [OWLOOP API](https://github.com/TheEngineRoom-UniGe/OWLOOP), 
which, in turns, depends on [aMOR](https://github.com/EmaroLab/multi_ontology_reference) 
and on  [OWL-API](https://github.com/owlcs/owlapi).
OWLOOP and aMOR are imported from the jar files available in the `\lib` folder, while 
OWL-API is loaded through the configuration in the `build.gradle` file.
 
The development and tests have been done using: Java 8, 
[Intellij IDE](https://www.jetbrains.com/idea/) and
[JUnit Jupiter](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api/5.7.0).

To install the framework `clone` this repository and open it as a Java project based on 
Gradle with a suitable IDE.
The sections below show the main concepts of the framework and introduce some examples 
provided in the repository. 

---
## 2. Ontology
The framework relies on an OWL ontology to store Parts and perform affinity-based queries.
However, for improving the computation, the current version do not use OWL-based *reasoning* 
to the disadvantage of the explicitness of the knowledge represented in the ontology.

Let us list now the main concepts that can be used to structure an ontology.

### ‣ 2.1. Part Type
The ontology contains *OWL Classes* specifying different **Types** of Parts, e.g., `MOTOR`. 
Each class is not define in logic terms, and it only contains Parts that are asserted to be 
of a given Type.
In other words, the OWL reasoner does not perform classification of Parts Types based on 
logic axioms, but it only relies on prior knowledge.

### ‣ 2.2. Part Individual & Feature
An OWL class (e.g., `MOTOR`) might contain *OWL Instances* `I`, which identifies different Parts.
Each instance has some Features that are represented as *OWL Data Properties*, 
e.g., `hasWeight(I,57)`, `hasDescriptor(I,"....")`, etc.
More generally, each Part `I` has some *relation-value* pairs `r(I,v)`, where `r` is identified 
by a `key` (i.e., a `String`).

### ‣ 2.3. Part Feature Datatype
Each property `r` is defined in terms of a **Datatype** that can be: a `Number` (i.e., a `Float`, 
a `Double`, a `Integer` or a `Long`), a `String`, a `Boolean`, or a `Range`.

Two instances `I` and `J` cannot be specified with the same property `r ≡ "hasWeight"`
relating values with different Datatypes, e.g, `Long` and `String`. 
Note that two different types of `Number` are always consistent, i.e., it is 
possible to compare `I(r,3.0f)` and `J(r,38432984L)`.

Also, each property `r` occurs only once for each Instance, e.g., `I` cannot have two weights.
However, when a property `r` concerns a `Range` Datatype (which is defined through a `min` 
and a `max` values as `Number`), `I` would be described with two properties instead of one, i.e., 
`r¹ ≡ hasWeight-MiN-` and `r² ≡ hasWeight-MaX-`.

### ‣ 2.4. Ontology Part Concepts
To summarises, each Part `I` should be defined with:
 1. a name of the instance `I` as a `String`, i.e., an identifier; 
 2. the name of the Type classifying the Part, e.g., `MOTOR`,
 3. an unspecified amount of Features, each represented through 
    - a property `r` (or two properties `r¹` and `r²`), each with a specific Datatype and a `key`;
    - the relative value(s) `v` with a consistent Datatype for the related property key.

Some simple examples of OWL ontologies are available in the `/src/test/resources` folder, 
which can be opened and inspected through the [Protégé](https://protege.stanford.edu/) GUI.

---
## 3. Software Architecture
The framework is divided in packages concerning:
 - `core` → general interfaces and abstract classes,  
 - `implementation` → the implementation of the `core` interfaces, 
   - `owlInterface` → the implementation related to OWL ontology,
      - `owloopDescriptor` → OWLOOP descriptors to efficiently manipulate and query the ontology,
   - `kernel` → the implementation to evaluate Features and Part affinities involving different Datatypes,
 - `utility` → auxiliary classes. 

![Alt text](./doc/umlCore.png?raw=true "Fig. 1: The Core Architecture")

The architecture shown in the UML above defines some components of the framework, 
involving the following classes.

### ‣ 3.1. BaseFeature\<V\> 
It defines a relation-value pair between a property `r` with a given `key` (i.e., an identifying name) 
and a value `v` of Datatype `V`.

### ‣ 3.2. WritableFeature\<V\> 
It extends `BaseFeature` and introduces abstract functions for adding (and removing) 
a Feature to (from) the ontology.

### ‣ 3.3. OWLFeature\<V\> 
It extends `WritableFeature` and implements its abstract function using OWLOOP,
i.e., it adds (and remove) from (to) the ontology relation-value pairs involving`r` and `v`.

### ‣ 3.4. OWLRangeFeature 
It extends `OWLFeature` to deal with the `Range` Datatype, i.e., it adds (and remove) from (to) 
the ontology `r¹(I,min)` and `r²(I,max)`.

### ‣ 3.5. BaseKernel\<V,P\> 
It extends `BaseFeature` and defines an abstract function to compare an `actual` Feature
(given as input `X`) with `this` target Feature (which is of Datatype `V`) and return a
number in [0,1].
A `BaseKernel` also specifies a weight that will be used while computing the 
Part affinity among severlal Kernels.

The framework is made to allow the definition of general-purpose Kernel with 
generic parameters `P` as detailed below.

### ‣ 3.6. BasePart\<F\> 
It represents a Part through:
 - a `String` identifying the Part instance name`I`, 
 - a Part Type, e.g., `MOTOR`,
 - a set of `F extends BaseFeature<?>` Features and a related set of `BaseKernel<V>`, which 
   are coupled based on `key` equivalence, and they should have consistent Datatype.
   
Also, `BasePart` defines abstract functions to add (and remove) the Part to (from) the ontology, 
and for evaluating affinities by comparing Features and relative Kernels.

### ‣ 3.7. OWLPart 
It extends `BasePart` to add (and remove) the Part `I` to (from) the ontology based on OWLOOP.

### ‣ 3.8. Part
It extends `OWLPart` to allow querying the Part affinity by aggregating the evaluation of each
`BaseKernel`, which are given as target.
The comparison returns an affinity value in [0,1], which is computed as the weighted average 
between the results of the evaluation of each Kernel.

### ‣ 3.9. Affinity
It is the class returned by `Part.evaluateAffinity(kernels)`, which contains an evaluation 
in [0,1] associated with the `Part` identifier.

---
## 4. Kernel-Based Reasoning
Each Kernel represents a target value for a Feature, which can be of different Datatype.
Therefore, the `BaseKernel` interface can be extended to tackle different Features.
The UML below shows the implemented Kernels, which description follows.

![Alt text](./doc/umlKernel.png?raw=true "Fig. 2: The Kernl Interface")

### ‣ 4.1. KernelBoolean
It compares two `OWLFeature<Boolean>` values, and it returns 1 if those are equal, 
0 otherwise.
It can evaluate symbols as "true" and "false", or `Number`, i.e., `true` if 
the number is greater than zero.
`KernelBoolean` does not require any additional parameters `P`. 

### ‣ 4.2. KernelString 
It evaluates two `OWLFeature<String>` values, and it returns 1 if those are equal,
0 otherwise. 
In addition, `KernelString` does not require any additional parameters `P`.

### ‣ 4.3. KernelPoint
It evaluates two `OWLFeature<Number>` values by the means of a fuzzy membership
function. 
The latter is given as a parameter, i.e., a `List<KernelPointParam>`, which contains 
points `(x:value,y:degree)` representing the discontinuities of the fuzzy 
function, which will be reconstructed using linear interpolation.
Note that the parameter list must contain points that are ordered with an ascending `x` value.

### ‣ 4.4. KernelRange
It evaluates a combination of two `OWLFeature<Range>`, where a `Range` can also be 
derived from a `Number` considering `min = max`.

Given `this` target range and an `actual` range (of Datatype `X`), it classifies their mutual 
relationships as described by `RangeEval`, i.e.,
 - `WITHIN`: when the `actual` range is entirely contained in `this` range. 
 - `OVERLAPS`: when the `actual` range excesses `this` range both in the `min` and `max` limits.
 - `OVERLAPS_MIN`: when the `actual` range excesses the `min` limit of `this` range 
     but does not excess the `max` limit.
 - `OVERLAPS_MAX`: when the `actual` range excesses the `max` limit of `this` range 
     but does not excess the `min` limit.
 - `OUTSIDE`: when the `actual` range is not within `this` range.
 - `UNKNOWN`: when an error occurs.  

The `KernelRange` evaluation returns a value within [0,1] that is related to the amount of the
`actual` range within the limits of `this` range.
In other words, it returns 1 if the `OVERLAPS` case occurs, 0 if `OUTSIDE` occurs, and a 
linear value in (0,1) in the other cases.

This Kernel does not require any parameters, but it needs specific implementations
in the `OWLRangeFeature` and `OWLPart` classes to deal with the definition in 
the ontology of the specific  `r¹` and `r²` OWL Data Property. 

---
## 5. Tests & Examples
The repository is provided with the test-like environment that Intellij uses by default.
Nevertheless, they are not meant to perform unit-testing since the result of the tests should 
be manually inspected.
For this reason,  if necessary, they can be easily converted to `main` executables.

More in particular, the repository provides in the `src/test/java/...` folders executables that concern:
 - the Feature comparison based on all the Kernels presented above, i.e, `KernelBooleanTest`,
   `KernelBooleanTest`, `KernelPontTest` and `KernelRangeTest`;
 - adding (and removing) Parts to (from) an ontology based on OWLOOP, i.e., `OWLFeatureTest` and `OWLPartTest`, 
   which generate ontologies with the respective names in the `src/test/resources/` folder;
 - the affinity evaluation between the Parts in the ontology and a target given as a set 
   of Kernels,  i.e., `PartTest`; 
 - an interface to add Parts and query affinities based on a CSV file, i.e., `ExampleInterface`.
   This test involves both the situation where the ontology is saved on file or store on memory only.
   Remarkably, this executable involves all the abilities of the framework.

The detail of relevant instructions used in the tests above follows. 

### ‣ 5.1. Ontology and Logging Management
The repository provides the static `OntologyBootstrapper` utility, which can `createOntology()` and 
`loadOntology()` given an identifier (i.e., `ontName`) and a directory where the ontology might be stored.
These functions return an `ontology` that can exploits OWL-API, e.g,
`synchronizeReasoner()`, `saveOntology()` or `getIndividualB2Class()`; where `B2` stands for "belong to".

The APIs concerning the ontology use the Java logging utilities, and they might print a warning 
during framework boostrap.
On the other hand, the architecture implemented in this repository uses a simple `StaticLogger` 
utility to print on screen `VERBOSE`, `INFO`, `WARNING` and `ERROR` logs, 
which level can be set with `StaticLogger.setLevel()` (default value is `WARNING`).

### ‣ 5.2. Inputs Requirements
Accordingly, with the definition of Parts in the ontology, the framework requires 
an input *matrix*, which ca be given in CSV format or be set through API.

More generally, the inputs of `n` Parts can be depicted as the table below, where:
 - ID → is an optional field associated to the identifier of a Part `I`,
 - Type → is the name of the OWL class representing the type of `I`,
 - `r1,r2..,rj,...,rm` → are the Features keys that a Part can have (e.g., `"weight"`, 
   `"frequency"`, etc.).
   Note that some related value `vij` might be empty.
   
Each `rj` Feature can be shared among individuals `Ii` of different Type, but it 
represents always values `vij` of the same Datatype, e.g., `r1` concerns `Number`, 
`r2` concerns `String`, etc.
 However, exceptions occur for `Range` and `Boolean` Datatypes that can involve
`Number` as well.

|  ID   |    Type  |  r*1* |  r*2* | ... |  r*j* | ... |  r*m* |
|:-----:|:--------:|:-----:|:-----:|:---:|:-----:|:---:|:-----:|
|  I*1* |  `MOTOR` | v*11* | v*12* | ... | v*1j* | ... | v*13* |
|  I*2* | `SPRING` | v*21* | v*22* | ... | v*2j* | ... | v*23* |
|  ...  |    ...   |  ...  |  ...  | ... |  ...  | ... |  ...  |
|  I*i* |  `MOTOR` | v*i1* | v*i2* | ... | v*ij* | ... | v*1m* |
|  ...  |    ...   |  ...  |  ...  | ... |  ...  | ... |  ...  |
|  I*n* |  `MOTOR` | v*2n* | v*2n* | ... | v*in* | ... | v*nm* |

### ‣ 5.3. Part Definition in the Ontology
The repository provides a `CSVFile` utility which can parse the data formatted as in the 
table above into a `List<Set<OWLFeature<?>>>`.

`CSVFile` allows the ID and Type columns to be encoded in the CSV and polled for 
being set trough API with
```java
List<String> ids = csv.pullFeature("ID");  
List<String> ids = csv.pullFeature("Type"); 
// They removes the column ID and Type from the data structured parsed from the CSV file.  
```

`CSVFile` can process headers that specifies the Features `key` in the first line,
i.e., `ID, Type, weight, frequency, ..., rm`.
Nevertheless, as shown in the code snippet below, the headers can also be specified through API.

Finally, `CSVFile` requires to specify the Datatype associated to each column in the same order as 
defined in the header.

As an example, below there is a snippet of code showing how to read `n` Parts from a CSV file 
that is formatted as shown in the table above (see `src/test/resources/dataExampleNoHeader.csv`, 
and `src/test/resources/dataExample.csv`).

```java
// Define header with a relative Datatype.
String[] header = new String[]{"ID", "Type", "code", "frequqncy", "weight", "available"};
Class<?>[] datatypes = new Class[]{Long.class, String.class, String.class, Range.class, Float.class, Boolean.class};

// Parse file
CSVFile csv = new CSVFile("path/to/file.csv", datatypes, header);
// Eventually let the `header` be specified in the first line of the CSV file.
//CSVFile csv = new CSVFile("path/to/fileWithHeader.csv", datatypes);
List<Set<OWLFeature<?>>> data = csv.getData();

// Add Parts to the ontology with the Features given from the CSV file.
List<String> ids = csv.pullFeature("id");
List<String> partTypes = csv.pullFeature("TYPE");
for(int i = 0; i < data.size(); i++){
    OWLPart part = new Part(ids.get(i), partTypes.get(i), data.get(i), ontology); 
    part.addInstance();
}
```

Alternatively, it is also possible to encode the Features of a single Part through API as 
(see `OWLPartTest`):
```java
Set<OWLFeature<?>> features = new HashSet<>();
features.add(new OWLFeature<>("weight", 2.4f, ontology));
features.add(new OWLRangeFeature("frequency", new Range(0,5), ontology));
features.add(new OWLFeature<>("code", "F4", ontology));
features.add(new OWLFeature<>("availability", true, ontology));
OWLPart part = new Part("I1", "MOTOR", featurs, ontology);
part.addInstance();
```
The `part` can then be added (and removed) to (from) the ontology, and it can be used 
to query affinities by the means of Kernels.

Remarkably, when the contents of an ontology should be queried after some changes, 
the OWL reasoner needs to be updated with `ontology.synchronizeReasoner()`.
Nevertheless, this operation might be computationally expensive, and it should be used as less as possible.

When an ontology is loaded from a file, the reasoner will be automatically updated, and synchronization
is not required.
Note that to load an `ontology` multiple times within the same application, the `OWLReference`
should be closed using:
```java
OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); 
```

### ‣ 5.4. Target Kernel Definition
An `OWLPart` can compute the affinities with respect to some Kernels that encode target Feature values. 
The latter are specified by `BaseKernel` since it extends `BaseFeature`.

Kernels must be set through API and, based on the latter code snippet, they might be defined as 
(see `ExampleInterface` and `PartTest`):
```java
float weight = 1;
Set<BaseKernel<?,?>> kernels = new HashSet<>();
kernels.add(new KernelPoint("weight", 1.8, getKernelPointParams(), weight));
kernels.add(new KernelRange("frequency", new Range(2,3), weight));
kernels.add(new KernelBoolean("code", "A4-106V"), weight); 
kernels.add(new KernelBoolean("availability", 0)); // Default weight is 1.
```
Remarkably, the Kernels can span in Features that are (or not) defined in an ontology, and that can 
(or cannot) be assinged to the same instance `I`.
In other words, keys mismatching between the Feature of a Part and some provided Kernels might occur. 
In these case, the result of the evaluation is discarded to do not affect the affinity degree.

Note, from the two snippets of code above, that the Datatype associated to a Kernel should be 
consistent with the Datatype of a Feature with the same key `rj`.

### ‣ 5.5. Query Part Affinity
Given a set of Kernels encoding some target Features, and a Part with some actual Features, 
their affinity can be evaluated with:
```java
// Retrieve an Affinity, i.e., an <Ii,di> pair, where `di` is the affinity degree in [0,1].
Affinity affinity = part.evaluateAffinity(kernels);  
```
To use the above function effectively, it is possible to retrieve all the Parts `Ii` of a given Type with:
```java
Set<Part> parts = OWLPart.readParts("MOTOR", ontology);
```
Then, it is possible to iterate for each element in `parts` and retrieve a `Set<Affinity> affinities` on the
basis of `evaluateAffinity()`.
Eventually, the affinities can also be ranked with 
```java
BasePart.sortAffinities(affinities);
```
In this way, the last element of the `affinities` set would represent the Part with the best affinity.

---
## 6. Designed Extensions & Discussions
The framework has designed to be general in terms of Datatypes applied to Features and Kernels.
Indeed, it only requires that Datatypes are consistent among each other, but it does not limit their 
implementation.
Therefore, it is possible to rely on flexible Parts and Feature definition.  

The current implementation does not use the logic-based reasoning associate to the ontology, but this
capability might be used in further applications, e.g., to structure Parts Type.
However, if the representation of the Features in the ontology becomes more complex (e.g., as for a 
`Range`-based Feature), the classes `OWLPart` and `OWLFeatures` should be adapted, e.g., has done with 
`OWLRangeFeature` (see the UML depicting Kernels above).

Note that the architecture decouples basic interfaces from the implementation related to OWL.
Thus, the design paradigm might also be used to implement a version of this framewokor that
does not depend on OWL ontologies.

The framework has been designed to support the developing of more sophisticated method to
evaluate Part affinities.
This implies the implementation and test of different Kernels, and the code snippet below 
shows the template to create custom Kernels easily.
```java
public class MyKernel extends BaseKernel<`Datattype`, `Parametertype`> {
    public MyKernel(String targetKey, `Datatype` targetValue, `Parametertype` parameter) {
        super(targetKey, targetValue, parameter);  // The default weight is 1
    }
    public MyKernel(String targetKey, `Datatype` targetValue, `Parametertype` parameter, float weight) {
        super(targetKey, targetValue, parameter, weight);
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // Return a value within [0,1], or null to discard this result from the part affinity evaluation.
        // Use `this.getValue()` as the target value and compare it with the `actualValue`.
        `Datatype` actualValue = (`Datatype`) actual.getValue();
        ...
    }

    @Override
    protected <X extends BaseFeature<?>> boolean checkType(X actual) {
        // Return `true` if the `actual` value should be passed to `evaluateChecked()`, or `false` to do not invoke it.
        // By default it returns `true` if `X` is equal to the specified `Datatype`.
        return super.checkType(actual); 
    }
}
```
-----
© Luca Buoncompagni, August, 2021,  
GNU GENERAL PUBLIC LICENSE,  
University of Genoa & Teseo srl.
