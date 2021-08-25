package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.utility.Configurer;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;


// It tests the definition of a `Feature` in the ontology.
// This behaviour is also tested by `PartOWLTest`.
public class OWLFeatureTest {

    @Test
    void addRemoveFeature(){
        OWLReferences ontoRef = Configurer.createOntology(OWLFeatureTest.class.getSimpleName(), Configurer.ONTO_TEST_FILE_PATH);  // Create a new ontology.

        // Define two features. The `value` is only used to retrieve its dataType, i.e., the actual value is not considered.
        OWLFeature<Number> f1 = new OWLFeature<>("hasFeature1", 0, ontoRef);
        String hasFeature2 = "hasFeature2";
        OWLFeature<Number> f2 = new OWLFeature<>(hasFeature2, 0L, ontoRef);
        String hasFeature3 = "hasFeature3";
        OWLFeature<Number> f3 = new OWLFeature<>(hasFeature3, 0f, ontoRef);

        f1.removeFeature(); // Attempt to remove a not existing feature from the ontology (a warning should occur).
        f1.addFeature(); // Add a feature to the ontology.
        f2.addFeature(); // Add another feature to the ontology.
        f2.addFeature(); // Attempt to add an already existing feature to the ontology (a warning should occur).
        f1.removeFeature(); // Remove a feature from the ontology.

        f3.addFeature(); // Add another feature to the ontology.
        OWLFeature<?> k3bis = new OWLFeature<>(hasFeature3, 0f, ontoRef); // Redefine a feature as a new object.
        k3bis.removeFeature(); // Remove tha feature from the ontology.

        ontoRef.saveOntology();
        System.out.println("INFO: Ontology saved in " + ontoRef.getOntologyPath() + ". " +
                "You should check that it represents only the '" + hasFeature2 + "' feature.");
    }
}