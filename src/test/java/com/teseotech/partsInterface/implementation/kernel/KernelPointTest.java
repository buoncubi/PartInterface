package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/*
 * A simple executable to run a `KernelPoint` evaluation.
 */
public class KernelPointTest {
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
            if(affinity != null) {
                System.out.println("The evaluation of " + k.toDescription() + " is: " + affinity + '.');
                assertEquals(affinity, 1.0f);
            } else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }

    public static Set<OWLFeature<?>> getFeatures(OWLReferences ontology) {
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLFeature<>("hasFeature1", 1, ontology));
        features.add(new OWLFeature<>("hasFeature2", 2L, ontology));
        features.add(new OWLFeature<>("hasFeature3", 3f, ontology));
        return features;
    }
    public static Set<BaseKernel<?,?>> getKernels() {
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelPoint("hasFeature1", 1, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature2", 2L, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature3", 3f, getKernelParams()));
        return kernels;
    }
    public static List<KernelPointParam> getKernelParams() {
        // The `parameters` defines the fuzzy membership function by point with a `value` and a `degree`.
        // They must be ordered with a ascendant `value`, and their `degree` should be in [0,1].
        List<KernelPointParam> params = new ArrayList<>();
        params.add(new KernelPointParam(-1,0));
        params.add(new KernelPointParam(0,1));
        params.add(new KernelPointParam(1,0));
        return params;
    }

}