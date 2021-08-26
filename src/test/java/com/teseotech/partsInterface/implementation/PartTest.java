package com.teseotech.partsInterface.implementation;

import com.teseotech.partsInterface.implementation.kernel.*;
import com.teseotech.partsInterface.core.Affinity;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPart;
import com.teseotech.partsInterface.implementation.owlInterface.OWLRangeFeature;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/*
 * A simple executable to test the evaluation of affinity between a Part and target Kernels.
 */
class PartTest {
    @Test
    public void testAffinity(){
        // Define the Type of the Part. In this implementation it is a required parameter, but it might be
        // inferred using some OWL-based reasoning.
        String partType = "MOTOR";

        // Define some parts in a new ontology based on the function below. All the parts are equal for simplicity.
        OWLReferences ontology = OntologyBootstrapper.createOntology(PartTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        new Part(partType, features, ontology).addInstance();
        new Part(partType, features, ontology).addInstance();
        new Part(partType, features, ontology).addInstance();

        // Retrieve some parts from the ontology.
        ontology.synchronizeReasoner();
        Set<Part> parts = OWLPart.readParts(partType, ontology);
        for(Part p: parts){
            Affinity affinity = p.evaluateAffinity(getKernels());
            System.out.println("Affinities found (partId, affinityDegree): " + affinity
                    + ". You should check manually that te results are correct!");
        }
    }

    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontology) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLRangeFeature("hasFeature1", new Range(2,3), ontology));
        features.add(new OWLFeature<>("hasFeature2", 2L, ontology));
        features.add(new OWLFeature<>("hasFeature3", 3f, ontology));
        features.add(new OWLFeature<>("hasFeature4", "F4", ontology));
        features.add(new OWLFeature<>("hasFeature5", true, ontology));
        return features;
    }
    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelRange("hasFeature1", new Range(0,5)));
        kernels.add(new KernelPoint("hasFeature2", 2L, KernelPointTest.getKernelParams()));
        kernels.add(new KernelPoint("hasFeature3", 3f, KernelPointTest.getKernelParams()));
        kernels.add(new KernelString("hasFeature4", "f4"));
        kernels.add(new KernelBoolean("hasFeature5", true));
        return kernels;
    }

}