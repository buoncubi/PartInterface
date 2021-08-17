import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.core.Axiom;
import it.emarolab.owloop.descriptor.utility.classDescriptor.FullClassDesc;
import it.emarolab.owloop.descriptor.utility.individualDescriptor.FullIndividualDesc;
import it.emarolab.owloop.descriptor.utility.objectPropertyDescriptor.FullObjectPropertyDesc;

public class Test {

    public static void main(String[] args) {

        // Disabling 'internal logs' (so that our console is clean)
        Axiom.Descriptor.OntologyReference.activateAMORlogging(false);

        // Creating an object that is 'a reference to an ontology'
        OWLReferences ontoRef = Axiom.Descriptor.OntologyReference.newOWLReferencesCreatedWithPellet(
                "robotAtHomeOntology",
                "src/main/resources/robotAtHomeOntology.owl",
                "http://www.semanticweb.org/robotAtHomeOntology",
                true
        );

        // Creating some 'classes in the ontology'
        FullClassDesc location = new FullClassDesc("LOCATION", ontoRef);
        location.addSubClass("CORRIDOR");
        location.addSubClass("ROOM");
        location.writeAxioms();
        FullClassDesc robot = new FullClassDesc("ROBOT", ontoRef);
        robot.addDisjointClass("LOCATION");
        robot.writeAxioms();

        // Creating some 'object properties in the ontology'
        FullObjectPropertyDesc isIn = new FullObjectPropertyDesc("isIn", ontoRef);
        isIn.addDomainClassRestriction("ROBOT");
        isIn.addRangeClassRestriction("LOCATION");
        isIn.writeAxioms();
        FullObjectPropertyDesc isLinkedTo = new FullObjectPropertyDesc("isLinkedTo", ontoRef);
        isLinkedTo.addDomainClassRestriction("CORRIDOR");
        isLinkedTo.addRangeClassRestriction("ROOM");
        isLinkedTo.writeAxioms();

        // Creating some 'individuals in the ontology'
        FullIndividualDesc corridor1 = new FullIndividualDesc("Corridor1", ontoRef);
        corridor1.addObject("isLinkedTo", "Room1");
        corridor1.addObject("isLinkedTo", "Room2");
        corridor1.writeAxioms();
        FullIndividualDesc robot1 = new FullIndividualDesc("Robot1", ontoRef);
        robot1.addObject("isIn", "Room1");
        robot1.writeAxioms();

        // Saving axioms from in-memory ontology to the the OWL file located in 'src/main/resources'
        ontoRef.saveOntology();
    }
}