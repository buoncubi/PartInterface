package com.teseotech.partsInterface.implementation.owlInterface.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.Classes;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinkSet;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinks;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.IndividualExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.IndividualGround;
import it.emarolab.owloop.descriptor.utility.classDescriptor.HierarchicalClassDesc;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.List;

/*
 * The OWLOOP descriptor concerning the Feature values of an Part instance `I`,
 * and its clarification on a Type class, e.g., `MOTOR`.
 */
public class PartIndividualDescr extends IndividualGround
        implements IndividualExpression.Type<HierarchicalClassDesc>,  IndividualExpression.DataLink<FeaturePropertyDescr>{

    private Classes classes = new Classes();
    private DataLinkSet dataLinks = new DataLinkSet();

    /* Constructors from class: IndividualGround */
    public PartIndividualDescr(OWLNamedIndividual instance, OWLReferences onto) {
        super(instance, onto);
    }
    public PartIndividualDescr(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public PartIndividualDescr(OWLNamedIndividual instance, String ontoName) {
        super(instance, ontoName);
    }
    public PartIndividualDescr(OWLNamedIndividual instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public PartIndividualDescr(OWLNamedIndividual instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public PartIndividualDescr(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public PartIndividualDescr(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public PartIndividualDescr(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }

    /* Overriding methods in class: IndividualGround */
    // To read axioms from an ontology
    @Override
    public List<MappingIntent> readAxioms() {
        List<MappingIntent> r = IndividualExpression.Type.super.readAxioms();
        r.addAll( IndividualExpression.DataLink.super.readAxioms());
        return r;
    }
    // To write axioms to an ontology
    @Override
    public List<MappingIntent> writeAxioms() {
        List<MappingIntent> r = IndividualExpression.Type.super.writeAxioms();
        r.addAll( DataLink.super.writeAxioms());
        return r;
    }

    /* Overriding methods in classes: Individual and IndividualExpression */
    // Is used by the descriptors's build() method. It's possible to change the return type based on need.
    @Override
    public HierarchicalClassDesc getNewType(OWLClass instance, OWLReferences ontology) {
        return new HierarchicalClassDesc(instance, ontology);
    }
    // It returns classes from the EntitySet (after being read from the ontology)
    @Override
    public Classes getTypes() {
        return classes;
    }

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
        return getClass().getSimpleName() + "->" + getGround() + ":∈ " + classes + "," + dataLinks;
    }
}
