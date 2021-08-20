package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.coreInterface.Feature;
import com.teseotech.partsInterface.coreInterface.KernelInterface;
import com.teseotech.partsInterface.implementation.kernelEvaluation.KernelPoint;
import com.teseotech.partsInterface.utility.LoggerInterface;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.core.Axiom;
import org.junit.jupiter.api.Test;

import java.util.logging.Level;

class KernelOWLTest {  // It tests the definition of a `Feature` in the ontology.
    static OWLReferences createOntology() {
        LoggerInterface.configure(Level.FINE);
        Axiom.Descriptor.OntologyReference.activateAMORlogging(false); // Disabling OWLOOP and aMOE logs.
        // Creating a reference to a new ontology.
        String testName = "KernelPointTest";
        return Axiom.Descriptor.OntologyReference.newOWLReferencesCreatedWithPellet(
                testName,
                "src/test/resources/testOntology/" + testName + ".owl",
                "http://www.semanticweb.org/" + testName,
                true
        );
    }

    @Test
    void addRemoveFeature(){
        OWLReferences ontoRef = createOntology();  // create a new ontology.

        // define two features. The `value` is only used to retrieve its dataType, i.e., the actual value is not considered.
        Feature<Number> f = new Feature<>("hasFeature1", 0);
        KernelInterface<?,?> k1 = new KernelPoint(f, ontoRef);
        String hasFeature2 = "hasFeature2";
        KernelInterface<?,?> k2 = new KernelPoint(hasFeature2, 0L, ontoRef);
        String hasFeature3 = "hasFeature3";
        KernelInterface<?,?> k3 = new KernelPoint(hasFeature3, 0f, ontoRef);

        k1.removeFeature(); // attempt to remove a not existing feature from the ontology (a warning should occur).
        k1.addFeature(); // add a feature to the ontology.
        k2.addFeature(); // add another feature to the ontology.
        k2.addFeature(); // attempt to add an already existing feature to the ontology (a warning should occur).
        k1.removeFeature(); // remove a feature from the ontology.

        k3.addFeature(); // add another feature to the ontology.
        KernelInterface<?,?> k3bis = new KernelPoint(new Feature<>(hasFeature3, 0f), ontoRef); // redefine a feature as a new object.
        k3bis.removeFeature(); // remove tha feature from the ontology.

        ontoRef.saveOntology();
        System.out.println("INFO: Ontology saved in " + ontoRef.getOntologyPath() + ". " +
                "You should check that it represents only the " + hasFeature2 + " feature.");
    }
}