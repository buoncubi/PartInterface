package com.teseotech.partsInterface;

import com.teseotech.partsInterface.core.Kernel;
import com.teseotech.partsInterface.core.Part;
import com.teseotech.partsInterface.kernelImplementation.KernelPoint;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.core.Axiom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class PartImplementationTest {
    private static OWLReferences ontoRef;

    @BeforeAll
    static void setUp() {
        Axiom.Descriptor.OntologyReference.activateAMORlogging(false); // Disabling OWLOOP and aMOE logs.
        // Creating a reference to a new ontology.
        String testName = "PartTest";
        ontoRef = Axiom.Descriptor.OntologyReference.newOWLReferencesCreatedWithPellet(
                testName,
                "src/test/resources/" + testName + ".owl",
                "http://www.semanticweb.org/" + testName,
                true
        );
        PartImplementation.setLogFeatureAdding(false);  // Disable logging on features automatically created
    }

    @Test
    void addRemovePart(){
        // define some features (shared to all parts for simplicity).
        Set<Kernel> kernels = new HashSet<>();
        kernels.add(new KernelPoint("hasFeature1",  1, ontoRef));
        kernels.add(new KernelPoint("hasFeature2", 2L, ontoRef));
        kernels.add(new KernelPoint("hasFeature3", 3f, ontoRef));

        // define some parts. They might add features (see check logs).
        Part p0 = new PartImplementation("MOTOR", null, ontoRef, kernels);
        Part p1 = new PartImplementation("MOTOR", null, ontoRef, kernels);
        Part p2 = new PartImplementation("PIPE", null, ontoRef, kernels);

        p0.removeInstance(); // attempt to remove a not existing part from the ontology (a warning should occur).
        p0.addInstance(); // add a feature to the ontology.
        p1.addInstance(); // add another part to the ontology.
        p1.addInstance(); // attempt to add an already existing part to an ontology (a warning should occur).
        p0.removeInstance(); // remove a part from the ontology.

        p2.addInstance(); // add another part to the ontology.
        // remove a part from the ontology given its identifier.
        String p3InstanceName = p2.getInstanceID();
        PartImplementation.removeInstance(ontoRef, p3InstanceName);
        System.out.println("INFO: removing part with name " + p3InstanceName + '.');

        ontoRef.saveOntology();
        System.out.println("INFO: Ontology saved in " + ontoRef.getOntologyPath() + ". " +
                "You should check that it represents only the " + p1.getInstanceID() + " part.");
    }

    @Test
    void evaluateAffinity(){
        // todo to implement
    }
}