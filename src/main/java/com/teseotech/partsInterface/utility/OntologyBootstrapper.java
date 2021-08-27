package com.teseotech.partsInterface.utility;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.core.Axiom;

/*
 * A static class for creating a new ontology or load it from file.
 * It disable OWLOOP and AMOR logging.
 */
public class OntologyBootstrapper {
    private OntologyBootstrapper(){}  // This class is not constructable, i.e., only static functions.

    public static final String ONTO_TEST_FILE_PATH = "src/test/resources/";

    public static OWLReferences createOntology(String ontoName, String filepath) {
        StaticLogger.activateOWLOOPLogger(false);
        // Creating a reference to a new ontology.
        return Axiom.Descriptor.OntologyReference.newOWLReferencesCreatedWithPellet(
                ontoName,
                filepath + ontoName + ".owl",
                "http://www.partAffinity.org/" + ontoName,
                true
        );
    }

    public static OWLReferences loadOntology(String ontoName, String filePath){
        StaticLogger.activateOWLOOPLogger(false);
        // Loading existing ontology as a reference.
        return Axiom.Descriptor.OntologyReference.newOWLReferenceFromFileWithPellet(
                ontoName,
                filePath + ontoName + ".owl",
                "http://www.partAffinity.org/" + ontoName,
                true
        );
    }
}


