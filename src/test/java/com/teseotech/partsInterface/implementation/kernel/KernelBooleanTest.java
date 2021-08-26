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
 * A simple executable to run a `KernelBoolean` evaluation.
 */
class KernelBooleanTest {
    @Test
    public void testKernelEvaluation(){ // Test the evaluation of a single affinity component.
        // Define some features and target kernels based on the functions below.
        OWLReferences ontology = OntologyBootstrapper.createOntology(KernelPointTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = getFeatures(ontology);
        Set<BaseKernel<?, ?>> target = getKernels();

        // For all feature, test the kernel's comparison.
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
        kernels.add(new KernelBoolean("hasFeature1", true, 5));
        kernels.add(new KernelBoolean("hasFeature2", false, 1));
        kernels.add(new KernelBoolean("hasFeature3", true, 5));
        return kernels;
    }
    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontology) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", true, ontology));
        features.add(new OWLFeature<>("hasFeature2", true, ontology));
        features.add(new OWLFeature<>("hasFeature3", 5, ontology));  // As boolean is considered to be true if > 0.
        return features;
    }
}