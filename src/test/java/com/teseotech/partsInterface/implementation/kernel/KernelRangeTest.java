package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

/*
 * A simple executable to run a `KernelRange` evaluation.
 */
class KernelRangeTest {
    private static final Range actualRange = new Range(0,9);  // It is used across many features and ranges for simplicity.

    @Test
    public void testKernelEvaluation(){  // Test the evaluation of a single affinity component.
        // Define some features and target kernels based on the functions below.
        OWLReferences ontology = OntologyBootstrapper.createOntology(KernelPointTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> target = getKernels();

        // For all feature, test the kernel's evaluation.
        for(BaseKernel<?,?> k: target){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            if(affinity != null)
                System.out.println("The evaluation of " + k.toDescription()
                        + " target v.s. " + actualRange + " range is: " + affinity
                        + ".  You should check manually if it is correct!");
            else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }

    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontology) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", actualRange, ontology));
        features.add(new OWLFeature<>("hasFeature2", actualRange, ontology));
        features.add(new OWLFeature<>("hasFeature3", actualRange, ontology));
        features.add(new OWLFeature<>("hasFeature4", actualRange, ontology));
        features.add(new OWLFeature<>("hasFeature5", actualRange, ontology));
        return features;
    }
    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelRange("hasFeature1", actualRange));
        kernels.add(new KernelRange("hasFeature2", new Range(3f,7f)));   // Based on the related feature, it implies RangeEval.OVERLAPS.
        kernels.add(new KernelRange("hasFeature3", new Range(-1L,10L))); // Based on the related feature, RangeEval.WITHIN.
        kernels.add(new KernelRange("hasFeature4", new Range(1,10f)));   // Based on the related feature, RangeEval.OVERLAPS_MIN.
        kernels.add(new KernelRange("hasFeature5", new Range(-1,7)));    // Based on the related feature, RangeEval.OVERLAPS_MAX.
        return kernels;
    }
}