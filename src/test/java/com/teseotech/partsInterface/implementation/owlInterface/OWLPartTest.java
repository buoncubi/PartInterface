package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.core.BasePart;
import com.teseotech.partsInterface.implementation.Part;
import com.teseotech.partsInterface.implementation.kernel.Range;
import com.teseotech.partsInterface.utility.OntologyBootstrapper;
import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import org.junit.jupiter.api.Test;

import java.util.*;

/*
 * A simple executable to test the add (and remove) of `OWLPart` from (to) the ontology.
 * It also test the automatic generation of Part identifiers.
 */
public class OWLPartTest {
    @Test
    void addRemovePart() {  // Test the manipulation of the ontology.
        // Create a new ontology and set logging level.
        StaticLogger.setLevel(StaticLogger.INFO);
        OWLReferences ontology = OntologyBootstrapper.createOntology(OWLPartTest.class.getSimpleName(), OntologyBootstrapper.ONTO_TEST_FILE_PATH);

        // Define the Features of a Part.
        Set<OWLFeature<?>> features = new HashSet<>();
        features.add(new OWLRangeFeature("hasFeature1", new Range(2,3), ontology));
        features.add(new OWLFeature<>("hasFeature2", 2L, ontology));
        features.add(new OWLFeature<>("hasFeature3", 3f, ontology));
        features.add(new OWLFeature<>("hasFeature4", "F4", ontology));
        features.add(new OWLFeature<>("hasFeature5", true, ontology));

        // Define some Parts, all with the same features for simplicity.
        OWLPart p0 = new Part("MOTOR", features, ontology);  // Autogenerate Part identifier.
        OWLPart p1 = new Part("MOTOR", features, ontology);  // Autogenerate Part identifier.
        OWLPart p2 = new Part("Ii", "PIPE", features, ontology);  // Explicitly set Part identifier.

        // The `addInstance()` and `removeInstance()` operations might add OWLDataProperty on the ontology,
        // because all features needs tobe defined with a specific Datatype.
        // If it already exists a WARNING (but labelled as `StaticLogger.VERBOSE` occurs).
        p0.removeInstance(); // Attempt to remove a not existing part from the ontology (a warning should occur).
        p0.addInstance(); // Add a feature to the ontology.
        p1.addInstance(); // Add another part to the ontology.
        p1.addInstance(); // Attempt to add an already existing part to an ontology (a warning should occur).
        p0.removeInstance(); // Remove a part from the ontology.

        // Remove a part from the ontology given its identifier.
        p2.addInstance(); // Add another part to the ontology.
        String p3InstanceName = p2.getID();
        OWLPart.removeInstance(p3InstanceName, ontology);
        System.out.println("INFO: removing part with name " + p3InstanceName + '.');

        ontology.saveOntology();
        System.out.println("INFO: Ontology saved in " + ontology.getOntologyPath() + ". " +
                "You should check that it represents only the '" + p1.getID() + "' part.");
    }

    @Test
    public void createIdentifier(){
        // Test the creation of identifiers of the Part instance `I`.
        for(int i = 0; i < 20; i++)
            System.out.println(BasePart.createIdentifier());
    }
}