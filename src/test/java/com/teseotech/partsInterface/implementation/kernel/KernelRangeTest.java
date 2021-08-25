package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class KernelRangeTest {
    private static final Range actualRange = new Range(0,9);

    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontoRef) {
        // Define some features (shared to all parts for simplicity).
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", actualRange, ontoRef));  // equal
        features.add(new OWLFeature<>("hasFeature2", actualRange, ontoRef));  // overlaps
        features.add(new OWLFeature<>("hasFeature3", actualRange, ontoRef));  // within
        features.add(new OWLFeature<>("hasFeature4", actualRange, ontoRef));  // overlaps min
        features.add(new OWLFeature<>("hasFeature5", actualRange, ontoRef));  // overlap max
        return features;
    }
    public static Set<BaseKernel<?,?>> getKernels() {  // Based on `OWLPartTest.getFeatures`
        // Define some features (shared to all parts for simplicity).
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        // Define target ranges.
        kernels.add(new KernelRange("hasFeature1", actualRange));
        kernels.add(new KernelRange("hasFeature2", new Range(3f,7f)));
        kernels.add(new KernelRange("hasFeature3", new Range(-1L,10L)));
        kernels.add(new KernelRange("hasFeature4", new Range(1,10f)));
        kernels.add(new KernelRange("hasFeature5", new Range(-1,7)));
        return kernels;
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
                System.out.println("The evaluation of " + k.toDescription()
                        + " target v.s. " + actualRange + " range is: " + affinity
                        + ".  You should check manually if it is correct!");
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }
}