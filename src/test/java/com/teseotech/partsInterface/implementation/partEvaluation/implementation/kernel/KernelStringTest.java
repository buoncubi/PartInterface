package com.teseotech.partsInterface.implementation.partEvaluation.implementation.kernel;

import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseKernel;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.Part;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface.OWLFeatureTest;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KernelStringTest {
    private static ArrayList<Float> target = new ArrayList<>();

    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelString("hasFeature1", "f1 ", 5));
        target.add(1.0f);
        kernels.add(new KernelString("hasFeature2", "???", 1));
        target.add(0.0f);
        kernels.add(new KernelString("hasFeature3", "F3 ", 5));
        target.add(1.0f);
        return kernels;
    }
    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontoRef) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", "F1 ", ontoRef));
        features.add(new OWLFeature<>("hasFeature2", "F2", ontoRef));
        features.add(new OWLFeature<>("hasFeature3", "F3", ontoRef));
        return features;
    }

    @Test
    public void testAffinity(){
        // Define some features and target kernels.
        OWLReferences ontology = OWLFeatureTest.setupTestOntology(KernelPointTest.class.getSimpleName());
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> kernels = getKernels();

        // For all feature, test the kernel's evaluation.
        List<Float> affinities = new ArrayList<>();
        for(BaseKernel<?,?> k: kernels){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            affinities.add(affinity);
            if(affinity != null)
                System.out.println("The evaluation of " + k + " is: " + affinity + '.');
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
        assertEquals(affinities, target); // The target is associated with `getKernels()` setups.
    }
}