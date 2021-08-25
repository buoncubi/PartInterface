package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

class KernelBooleanTest {
    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelBoolean("hasFeature1", true, 5));
        kernels.add(new KernelBoolean("hasFeature2", false, 1));
        kernels.add(new KernelBoolean("hasFeature3", true, 5));
        return kernels;
    }
    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontoRef) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", true, ontoRef));
        features.add(new OWLFeature<>("hasFeature2", true, ontoRef));
        features.add(new OWLFeature<>("hasFeature3", 5, ontoRef));  // As boolean is considered to be true if > 0.
        return features;
    }

    @Test
    public void testAffinity(){
        // Define some features and target kernels.
        OWLReferences ontology = Configurer.createOntology(KernelPointTest.class.getSimpleName(), Configurer.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> kernels = getKernels();

        // For all feature, test the kernel's evaluation.
        for(BaseKernel<?,?> k: kernels){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            if(affinity != null)
                System.out.println("The evaluation of " + k.toDescription() + " is: " + affinity + ". You should check manually if it is correct.");
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }
}