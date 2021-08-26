package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;


/*
 * A simple executable to run a `KernelString` evaluation.
 */
class KernelStringTest {
    @Test
    public void testAffinity(){
        // Define some features and target kernels based on the functions below.
        OWLReferences ontology = OntologyBootstrapper.createOntology(KernelPointTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> target = getKernels();

        // For all feature, test the kernel's evaluation.
        for(BaseKernel<?,?> k: target){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            if(affinity != null)
                System.out.println("The evaluation of " + k.toDescription() + " is: " + affinity + ". You should check manually if it is correct.");
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }

    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelString("hasFeature1", "f1 ", 5));
        kernels.add(new KernelString("hasFeature2", "???", 1));
        kernels.add(new KernelString("hasFeature3", "F3 ", 5));
        return kernels;
    }
    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontology) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", "F1 ", ontology));
        features.add(new OWLFeature<>("hasFeature2", "F2", ontology));
        features.add(new OWLFeature<>("hasFeature3", "F3", ontology));
        return features;
    }
}