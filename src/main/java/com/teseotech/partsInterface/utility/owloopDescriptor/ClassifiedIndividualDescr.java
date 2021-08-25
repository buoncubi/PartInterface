package com.teseotech.partsInterface.utility.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.Individuals;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.ClassExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.ClassGround;
import it.emarolab.owloop.descriptor.utility.individualDescriptor.LinkIndividualDesc;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

public class ClassifiedIndividualDescr  extends ClassGround
        implements ClassExpression.Instance<IndividualDataDescr> {

    private Individuals individuals = new Individuals();

    /* Constructors from class: ClassGround */
    public ClassifiedIndividualDescr(OWLClass instance, OWLReferences onto) {
        super(instance, onto);
    }
    public ClassifiedIndividualDescr(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public ClassifiedIndividualDescr(OWLClass instance, String ontoName) {
        super(instance, ontoName);
    }
    public ClassifiedIndividualDescr(OWLClass instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public ClassifiedIndividualDescr(OWLClass instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public ClassifiedIndividualDescr(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public ClassifiedIndividualDescr(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public ClassifiedIndividualDescr(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }

    /* Overriding methods in class: ClassGround */
    // To read axioms from an ontology
    @Override
    public List<MappingIntent> readAxioms() {
        return Instance.super.readAxioms();
    }
    // To write axioms to an ontology
    @Override
    public List<MappingIntent> writeAxioms() {
        return Instance.super.writeAxioms();
    }

    /* Overriding methods in classes: Class and ClassExpression */
    // Is used by the descriptors's build() method. It's possible to change the return type based on need.
    @Override
    public IndividualDataDescr getIndividualDescriptor(OWLNamedIndividual instance, OWLReferences ontology) {
        return new IndividualDataDescr(instance, ontology);
    }
    // It returns Individuals from the EntitySet (after being read from the ontology)
    @Override
    public Individuals getIndividuals() {
        return individuals;
    }

    /* Overriding method in class: Object */
    // To show internal state of the Descriptor
    @Override
    public String toString() {
        return getClass().getSimpleName() + "->" + getGround() + ":" + individuals;
    }
}