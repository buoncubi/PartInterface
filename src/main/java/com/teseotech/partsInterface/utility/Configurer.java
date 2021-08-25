package com.teseotech.partsInterface.utility;

import com.teseotech.partsInterface.utility.StaticLogger;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.core.Axiom;

import java.util.logging.Level;

public class Configurer {
    private Configurer(){}  // this class is not constructable, i.e., only static

    public static final String ONTO_TEST_FILE_PATH = "src/test/resources/";
    static {
        setLogging(Level.INFO);
        Axiom.Descriptor.OntologyReference.activateAMORlogging(false); // Disabling OWLOOP and aMOE logs.
    }

    public static void setLogging(Level logging) {
        StaticLogger.setLogger(logging);  // SEVERE (i.e., error), WARNING, INFO, FINE (i.e., verbose debug).
    }

    public static OWLReferences createOntology(String ontoName, String filepath) {
        // Creating a reference to a new ontology.
        return Axiom.Descriptor.OntologyReference.newOWLReferencesCreatedWithPellet(
                ontoName,
                filepath + ontoName + ".owl",
                "http://www.semanticweb.org/" + ontoName,
                true
        );
    }

    public static OWLReferences loadOntology(String ontoName, String filePath){
        // Loading existing ontology as a reference.
        return Axiom.Descriptor.OntologyReference.newOWLReferenceFromFileWithPellet(
                ontoName,
                filePath + ontoName + ".owl",
                "http://www.semanticweb.org/" + ontoName,
                true
        );
    }
}


