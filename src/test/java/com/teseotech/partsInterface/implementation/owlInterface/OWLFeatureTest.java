package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

/*
 * A simple executable to test the add (and remove) of `OWLFeatures` from (to) the ontology.
 */
public class OWLFeatureTest {
    @Test
    void addRemoveFeature(){
        // Create a new ontology and set logging level.
        StaticLogger.setLevel(StaticLogger.INFO);
        OWLReferences ontology = OntologyBootstrapper.createOntology(OWLFeatureTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);

        // Define two features. The `value` is only used to retrieve its dataType, i.e., the actual value is not considered.
        OWLFeature<Number> f1 = new OWLFeature<>("hasFeature1", 0, ontology);
        String hasFeature2 = "hasFeature2";
        OWLFeature<Number> f2 = new OWLFeature<>(hasFeature2, 0L, ontology);
        String hasFeature3 = "hasFeature3";
        OWLFeature<Number> f3 = new OWLFeature<>(hasFeature3, 0f, ontology);

        // Attempt to remove a not existing feature from the ontology (a warning should occur).
        f1.removeFeature();
        // Add a feature to the ontology.
        f1.addFeature();
        // Add another feature to the ontology.
        f2.addFeature();
        // Attempt to add an already existing feature to the ontology (a warning should occur).
        f2.addFeature();
        // Remove a feature from the ontology.
        f1.removeFeature();

        // Add another feature to the ontology.
        f3.addFeature();
        // Redefine a feature as a new object.
        OWLFeature<?> k3bis = new OWLFeature<>(hasFeature3, 0f, ontology);
        // Remove tha feature from the ontology.
        k3bis.removeFeature();

        ontology.saveOntology();
        System.out.println("INFO: Ontology saved in " + ontology.getOntologyPath() + ". " +
                "You should check that it represents only the '" + hasFeature2 + "' feature.");
    }
}