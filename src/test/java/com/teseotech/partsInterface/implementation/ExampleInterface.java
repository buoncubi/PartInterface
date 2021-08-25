package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.kernel.*;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.utility.CSVFile;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.amor.owlInterface.OWLReferencesInterface;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class ExampleInterface {
    private ExampleInterface(){}  // Not instantiable.

    public static final String FILE_PATH = "src/test/resources/";
    public static final String ONTO_NAME = "PartDescriptionExample";

    private static OWLReferences buildOntology(){
        /*      Configure the system.      */
        OWLReferences ontology = Configurer.createOntology(ONTO_NAME, FILE_PATH);
        Configurer.setLogging(Level.INFO);

        /*      Read CSV file that contains headers (i.e., the features key).
                Hypothesis: the size of `type`, `header` and each CSV lines is always equal.      */
        Class<?>[] types = new Class[]{Long.class, String.class, String.class, Integer.class, Float.class};
        CSVFile csv = CSVFile.readCsv(FILE_PATH + "dataExampleHeader.csv", types);
        List<Set<OWLFeature<?>>> data = csv.getData();

        // Eventually, the headers can be specified.
        //String[] header = new String[]{"id", "code", "freq"};
        //CSVFile csv = Utility.readCsv(FILE_PATH + "dataExampleNoHeader.csv", ";", types, header);
        //Set<Set<OWLFeature<?>>> data = csv.getData();

        /*      Create an ontology and store each part.      */
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
        kernels.add(new KernelRange("freq", new Range(0,260)));
        kernels.add(new KernelPoint("weight", 80f, getKernelPointParams()));
        kernels.add(new KernelString("code", "A4-106V"));

        /*      Evaluate Affinity.      */
        for(Part p: parts){
            Affinity affinity = p.queryAffinity(kernels);
            System.out.println("Affinities found (partId, affinityDegree): " + affinity);
        }
    }

    public static List<KernelPointParam> getKernelPointParams() {
        // The `parameters` should be ordered by their `value` and their `degree` should be in [0,1]!
        List<KernelPointParam> params = new ArrayList<>();
        params.add(new KernelPointParam(-1,0));
        params.add(new KernelPointParam(0,1));
        params.add(new KernelPointParam(1,0));
        return params;
    }

    @Test
    public void testByClosingOnto(){
        OWLReferences ontology = buildOntology();
        ontology.saveOntology();
        OWLReferencesInterface.OWLReferencesContainer.removeInstance(ontology); // Delete (i.e., close) ontology reference and allow to load it again.
        queryAffinity();
    }

    @Test
    public void testWithRuntimeOnto(){
        OWLReferences ontology = buildOntology();
        ontology.synchronizeReasoner();
        queryAffinity(ontology);
    }

}
