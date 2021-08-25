package com.teseotech.partsInterface.utility.owloopDescriptor;

import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.Restrictions;
import it.emarolab.owloop.descriptor.construction.descriptorExpression.DataPropertyExpression;
import it.emarolab.owloop.descriptor.construction.descriptorGround.DataPropertyGround;
import org.semanticweb.owlapi.model.OWLDataProperty;

import java.util.List;

public class FeaturePropertyDescr extends DataPropertyGround implements DataPropertyExpression.Range {

    private Restrictions rangeRestrictions = new Restrictions();

    /* Constructors from class: DataPropertyGround */
    public FeaturePropertyDescr(OWLDataProperty instance, OWLReferences onto) {
        super(instance, onto);
    }
    public FeaturePropertyDescr(String instanceName, OWLReferences onto) {
        super(instanceName, onto);
    }
    public FeaturePropertyDescr(OWLDataProperty instance, String ontoName) {
        super(instance, ontoName);
    }
    public FeaturePropertyDescr(OWLDataProperty instance, String ontoName, String filePath, String iriPath) {
        super(instance, ontoName, filePath, iriPath);
    }
    public FeaturePropertyDescr(OWLDataProperty instance, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instance, ontoName, filePath, iriPath, bufferingChanges);
    }
    public FeaturePropertyDescr(String instanceName, String ontoName) {
        super(instanceName, ontoName);
    }
    public FeaturePropertyDescr(String instanceName, String ontoName, String filePath, String iriPath) {
        super(instanceName, ontoName, filePath, iriPath);
    }
    public FeaturePropertyDescr(String instanceName, String ontoName, String filePath, String iriPath, boolean bufferingChanges) {
        super(instanceName, ontoName, filePath, iriPath, bufferingChanges);
    }

    /* Overriding methods in class: DataPropertyGround */
    // To read axioms from an ontology
    @Override
    public List<MappingIntent> readAxioms() {
        return DataPropertyExpression.Range.super.readAxioms();
    }
    // To write axioms to an ontology
    @Override
    public List<MappingIntent> writeAxioms() {
        return DataPropertyExpression.Range.super.writeAxioms();
    }

    /* Overriding methods in classes: DataProperty and DataPropertyExpression */
    // It returns rangeRestrictions from the EntitySet (after being read from the ontology)
    @Override
    public Restrictions getRangeRestrictions() {
        return rangeRestrictions;
    }

    /* Overriding method in class: Object */
    // To show internal state of the Descriptor
    @Override
    public String toString() {
        return getClass().getSimpleName() + "->" + getGround() + ":" + rangeRestrictions;
    }
}
