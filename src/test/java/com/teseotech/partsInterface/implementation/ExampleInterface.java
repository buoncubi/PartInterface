package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.core.BasePart;
import com.teseotech.partsInterface.implementation.kernel.*;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.utility.CSVFile;
import com.teseotech.partsInterface.utility.Configurer;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import org.junit.jupiter.api.Test;

import java.util.*;

public class ExampleInterface {
    private ExampleInterface(){}  // Not instantiable.

    public static final String FILE_PATH = "src/test/resources/";
    public static final String ONTO_NAME = "PartDescriptionExample";

    private static OWLReferences buildOntology(){
        /*      Configure the system.      */
        OWLReferences ontology = Configurer.createOntology(ONTO_NAME, FILE_PATH);
        StaticLogger.setLevel(StaticLogger.WARNING);

        /*      Read CSV file that contains headers (i.e., the features key).
                Hypothesis: the size of `type`, `header` and each CSV lines is always equal.      */
        Class<?>[] types = new Class[]{Long.class, String.class, String.class, Range.class, Float.class, Boolean.class, Number.class};
        String[] header = new String[]{"id", "TYPE", "code", "freq", "weight", "available", "pole"};
        CSVFile csv = CSVFile.readCsv(FILE_PATH + "dataExampleNoHeader.csv", ",", types, header);
        List<Set<OWLFeature<?>>> data = csv.getData();
        // Eventually, the headers can be read from the CSV file.
        //CSVFile csv = CSVFile.readCsv(FILE_PATH + "dataExampleHeader.csv", types);
        //List<Set<OWLFeature<?>>> data = csv.getData();


        /*      Create an ontology and store each part.      */
        // An error will occur if pulled feature are not available for all parts.
        List<String> ids = csv.pullFeature("id");   // Get the `ID` from the CSV (or give it explicitly in `new Part(...)`
        List<String> partTypes = csv.pullFeature("TYPE");  // Get the `PartType` from the CSV (or give it explicitly in `new Part(...)`
        for(int i = 0; i < data.size(); i++){
            OWLPart part = new Part(ids.get(i), partTypes.get(i), data.get(i), ontology);
            part.addInstance(); // You might want to `removeInstance()`
        }
        return ontology;
    }

    private static void queryAffinity(){
        OWLReferences ontology = Configurer.loadOntology(ONTO_NAME, FILE_PATH);
        queryAffinity(ontology);
    }
    private static void queryAffinity(OWLReferences ontology){
        /*      Load parts from an ontology.      */
        Set<Part> parts = OWLPart.readParts("MOTOR", ontology);  // todo read "MOTOR" from the ontology.

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
            Affinity affinity = p.queryAffinity(kernels);
            affinities.add(affinity);
            System.out.println("Affinities found (partId, affinityDegree): " + affinity);
        }

        /*      Retrieve best part.      */
        BasePart.sortAffinities(affinities);
        Affinity bestMatch = affinities.get(affinities.size() - 1);
        for(Part p: parts){
            if(p.getID().equals(bestMatch.getID())) {
                System.out.println("The best part is: " + p + " with a degree of " + (bestMatch.getDegree() * 100) + "%.");
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

    @Test
    public void testByClosingOnto(){
        OWLReferences ontology = buildOntology();  // Creates ontology.
        ontology.saveOntology();
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); // Delete (i.e., close) ontology reference and allow to load it again.
        queryAffinity();  // Reload ontology.

        System.out.println("------------------------------------------");
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); // Delete (i.e., close) ontology reference and allow to run multiple tests.
    }

    @Test
    public void testWithRuntimeOnto(){
        OWLReferences ontology = buildOntology();
        ontology.synchronizeReasoner();  // It updates the internal reasoner of the ontology. It might take some time.
        queryAffinity(ontology);

        System.out.println("------------------------------------------");
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); // Delete (i.e., close) ontology reference and allow to run multiple tests.
    }
}
