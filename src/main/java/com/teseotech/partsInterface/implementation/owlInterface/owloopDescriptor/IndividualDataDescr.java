package com.teseotech.partsInterface.implementation.owlInterface.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinkSet;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinks;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

public class IndividualDataDescr extends IndividualGround
        implements IndividualExpression.DataLink<FeaturePropertyDescr> {

    private DataLinkSet dataLinks = new DataLinkSet();

    /* Constructors from class: IndividualGround */
    public IndividualDataDescr(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
    }
    public IndividualDataDescr(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public IndividualDataDescr(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
    }
    public IndividualDataDescr(OWLNamedIndividual instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public IndividualDataDescr(OWLNamedIndividual instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public IndividualDataDescr(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public IndividualDataDescr(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public IndividualDataDescr(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }

    /* Overriding methods in class: IndividualGround */
    // To read axioms from an ontology
    @Override
    public List<MappingIntent> readAxioms() {
        return IndividualExpression.DataLink.super.readAxioms();
    }
    // To write axioms to an ontology
    @Override
    public List<MappingIntent> writeAxioms() {
        return DataLink.super.writeAxioms();
    }

    /* Overriding methods in classes: Individual and IndividualExpression */
    // Is used by the descriptors's build() method. It's possible to change the return type based on need.
    @Override
    public FeaturePropertyDescr getNewDataProperty(DataLinks instance, OWLReferences ontology) {
        return new FeaturePropertyDescr( instance.getExpression(), ontology);
    }
    // It returns dataLinks from the EntitySet (after being read from the ontology)
    @Override
    public DataLinkSet getDataProperties() {
        return dataLinks;
    }

    /* Overriding method in class: Object */
    // To show internal state of the Descriptor
    @Override
    public String toString() {
        return getClass().getSimpleName() + "->" + getGround() + ":" + dataLinks;
    }
}
