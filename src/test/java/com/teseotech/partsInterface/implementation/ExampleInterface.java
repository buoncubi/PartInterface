package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.core.BasePart;
import com.teseotech.partsInterface.implementation.kernel.*;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.utility.CSVFile;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import org.junit.jupiter.api.Test;

import java.util.*;

/*
 * A simple executable implementing an interface to this framework.
 * The interface allow adding parts to an ontology and query their affinities to target Kernels.
 * It addresses both the case when the ontology is store on a file or kept in the memory only.
 * To test the interface, this class uses data stored in a CSV file.
 */
public class ExampleInterface {
    private ExampleInterface(){}  // Make this static class not instantiable.

    // Define resources.
    public static final String FILE_PATH = "src/test/resources/";
    public static final String ONTO_NAME = "PartDescriptionExample";


    @Test
    public void testByClosingOnto(){  // The test involves closing and reopening the ontology.
        OWLReferences ontology = buildOntology();  // Creates a new ontology with some Parts (see below).
        ontology.saveOntology();  // Store the ontology to a file.
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); // Delete (i.e., close) ontology reference and allow to load it again.
        evaluateAffinity();  // Reload ontology and query Parts affinity (see below).

        System.out.println("------------------------------------------");
        // Delete (i.e., close) ontology reference and allow to run multiple tests.
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology);
    }

    @Test
    public void testWithRuntimeOnto(){  // The test involves no ontology stored to file.
        OWLReferences ontology = buildOntology();  // Creates a new ontology with some Parts (see below).
        ontology.synchronizeReasoner();  // It updates the internal reasoner of the ontology. It might take some time.
        evaluateAffinity(ontology);  // Query Parts affinities based on the ontology and target Kernels (see below).

        System.out.println("------------------------------------------");
        // Delete (i.e., close) ontology reference and allow to run multiple tests.
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology);
    }

    private static OWLReferences buildOntology(){
        /*      Configure the system.      */
        OWLReferences ontology = OntologyBootstrapper.createOntology(ONTO_NAME, FILE_PATH);
        StaticLogger.setLevel(StaticLogger.WARNING);

        /*      Read CSV file that contains headers (i.e., the features key).
                Hypothesis: the size of `type`, `header` and each CSV lines is always equal.      */
        Class<?>[] datatypes = new Class[]{Long.class, String.class, String.class, Range.class, Float.class, Boolean.class, Number.class};
        String[] header = new String[]{"id", "TYPE", "code", "freq", "weight", "available", "pole"};
        CSVFile csv = new CSVFile(FILE_PATH + "dataExampleNoHeader.csv", ",", datatypes, header);
        List<Set<OWLFeature<?>>> data = csv.getData();
        // Eventually, the headers can be read from the CSV file (to test uncomment below and comment 2 lines above).
        //CSVFile csv = new CSVFile(FILE_PATH + "dataExampleHeader.csv", datatypes);
        //List<Set<OWLFeature<?>>> data = csv.getData();


        /*      Create an ontology and store each part.      */
        // An error will occur if pulled feature are not available for all parts.
        List<String> ids = csv.pullFeature("id");   // Get the `ID` from the CSV to be given explicitly in `new Part(...)`.
        List<String> partTypes = csv.pullFeature("TYPE");  // Get the `PartType` from the CSV to be given explicitly in `new Part(...)`.
        for(int i = 0; i < data.size(); i++){
            OWLPart part = new Part(ids.get(i), partTypes.get(i), data.get(i), ontology); // Define a new Part to add in the ontology.
            part.addInstance(); // You might want to `removeInstance()`
        }
        return ontology;
    }

    // This function is called when the ontology should be loaded on file, otherwise just
    // `evaluateAffinity(OWLReferences ontology)` will be used.
    // This script generates WARNING since some data is intentionally missing in the CSV file.
    private static void evaluateAffinity(){
        OWLReferences ontology = OntologyBootstrapper.loadOntology(ONTO_NAME, FILE_PATH);
        evaluateAffinity(ontology);
    }
    private static void evaluateAffinity(OWLReferences ontology){
        /*      Load parts from an ontology.      */
        Set<Part> parts = OWLPart.readParts("MOTOR", ontology);

        /*      Create kernels for evaluation.      */
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        // The feature key are based on a sub set of the csv header.
        kernels.add(new KernelRange("freq", new Range(0,260), 2));
        kernels.add(new KernelPoint("weight", 80f, getKernelPointParams(), 1));
        kernels.add(new KernelString("code", "A4-106V"));
        kernels.add(new KernelBoolean("available", true));
        // Note that the `pole` feature is not considered in any kernels.
        // kernels.add(new KernelRange("???", new Range(0,1)));  // The related feature do not exists in the CSV! It will generate a warning.

        /*      Evaluate Affinity.      */
        List<Affinity> affinities = new ArrayList<>();
        for(Part p: parts){
            Affinity affinity = p.evaluateAffinity(kernels);
            affinities.add(affinity);
            System.out.println("Affinity with (partId, affinityDegree)->" + affinity + '.');
        }

        /*      Retrieve the best part.      */
        BasePart.sortAffinities(affinities);
        Affinity bestMatch = affinities.get(affinities.size() - 1);
        for(Part p: parts){
            if(p.getID().equals(bestMatch.getID())) {
                System.out.println("The best part is: " + p + " with a degree of "
                        + String.format("%.1f",bestMatch.getDegree() * 100) + "%.");
                break;
            }
        }
    }

    public static List<KernelPointParam> getKernelPointParams() {
        // The `parameters` should be ordered by their `value` and their `degree` should be in [0,1]!
        List<KernelPointParam> params = new ArrayList<>();
        params.add(new KernelPointParam( -1f,0f));
        params.add(new KernelPointParam(-.8f,0.5f));
        params.add(new KernelPointParam(-.3f,1f));
        params.add(new KernelPointParam( .2f,1f));
        params.add(new KernelPointParam(-.8f,0.4f));
        params.add(new KernelPointParam(  1f,0f));
        return params;
    }
}