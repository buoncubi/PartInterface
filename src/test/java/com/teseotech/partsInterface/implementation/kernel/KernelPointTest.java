package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.implementation.owlInterface.OWLFeature;
import com.teseotech.partsInterface.implementation.owlInterface.OWLPartTest;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KernelPointTest {
    public static Set<BaseKernel<?,?>> getKernels() {  // Based on `OWLPartTest.getFeatures`
        // Define some features (shared to all parts for simplicity).
        Set<BaseKernel<?,?>> kernels = new HashSet<>();
        kernels.add(new KernelPoint("hasFeature1", 1, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature2", 2L, getKernelParams()));
        kernels.add(new KernelPoint("hasFeature3", 3f, getKernelParams()));
        return kernels;
    }
    public static List<KernelPointParam> getKernelParams() {
        // The `parameters` should be ordered by their `value` and their `degree` should be in [0,1]!
        List<KernelPointParam> params = new ArrayList<>();
        params.add(new KernelPointParam(-1,0));
        params.add(new KernelPointParam(0,1));
        params.add(new KernelPointParam(1,0));
        return params;
    }

    @Test
    public void testAffinity(){
        // Define some features and target kernels.
        OWLReferences ontology = Configurer.createOntology(KernelPointTest.class.getSimpleName(), Configurer.ONTO_TEST_FILE_PATH);
        Set<OWLFeature<?>> features = OWLPartTest.getFeatures(ontology);
        Set<BaseKernel<?, ?>> kernels = getKernels();

        // For all feature, test the kernel's evaluation.
        for(BaseKernel<?,?> k: kernels){
            OWLFeature<?> found = Part.findKernel(features, k);
            Float affinity = k.evaluate(found);
            if(affinity != null) {
                System.out.println("The evaluation of " + k.toDescription() + " is: " + affinity + '.');
                assertEquals(affinity, 1.0f);
            } else System.out.println("ERROR: cannot evaluate features with the kernel " + k.getClass().getSimpleName() + '.');
        }
    }
}