package com.teseotech.partsInterface.implementation.owlInterface;

import com.teseotech.partsInterface.core.BasePart;
import com.teseotech.partsInterface.implementation.kernel.Range;
import com.teseotech.partsInterface.utility.owloopDescriptor.ClassifiedIndividualDescr;
import com.teseotech.partsInterface.utility.owloopDescriptor.IndividualDataDescr;
import com.teseotech.partsInterface.utility.owloopDescriptor.PartIndividualDescr;
import com.teseotech.partsInterface.utility.StaticLogger;
import com.teseotech.partsInterface.implementation.Part;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinks;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class OWLPart extends BasePart<OWLFeature<?>> {
    private final String partType;
    static private boolean LOG_FEATURE_ADDING = true;
    private final PartIndividualDescr indDescriptor;

    public OWLPart(String identifier, String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(identifier, features);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
        this.setOntologyFeatures(ontology);  // It configure `this.getFeature()`, which should be already set.
    }
    public OWLPart(String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(null, features);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
        this.setOntologyFeatures(ontology);  // It configure `this.getFeature()`, which should be already set.
    }
    private PartIndividualDescr createOWLDescriptor(OWLReferences ontology){
        try {
            PartIndividualDescr indDescr = new PartIndividualDescr(getID(), ontology);
            indDescr.readAxioms();
            indDescr.addTypeIndividual(this.getType());
            return indDescr;
        } catch (Exception e){
            StaticLogger.logError("Cannot use ontology '" + ontology + "'!");
            //e.printStackTrace();
            return null;
        }
    }

    private void setOntologyFeatures(OWLReferences ontology){
        for(OWLFeature<?> f: this.getFeatures())
            f.setOWLDescriptor(ontology);
    }

    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void addPart() {}
    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void removePart() {}

    @Override
    public Boolean exists() { // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        return !this.indDescriptor.getDataProperties().isEmpty();
    }

    @Override
    public void addInstance() {
        if(this.shouldAdd(LOG_FEATURE_ADDING)) {
            for (OWLFeature<?> f : this.getFeatures()) {
                f.addFeature();  // It might make some changes to the TBox.
                if(f instanceof OWLRangeFeature) {
                    OWLRangeFeature rf = (OWLRangeFeature) f;
                    this.indDescriptor.addData(rf.getKey(), rf.getValue().getMin());
                    this.indDescriptor.addData(rf.getKey(), rf.getValue().getMax());
                } else this.indDescriptor.addData(f.getKey(), f.getValue());
            }
            this.indDescriptor.writeAxioms();
        }
    }
    @Override
    public void removeInstance() {
        if(this.shouldRemove(LOG_FEATURE_ADDING))
            removeInstance(getID(), indDescriptor.getOntologyReference()); // Added `Features` are not removed.
    }

    static public void removeInstance(String instanceName, OWLReferences ontoRef){
        ontoRef.removeIndividual(instanceName);
    }

    public String getType() {
        return partType;
    }
    public PartIndividualDescr getOWLDescriptor() {
        return this.indDescriptor;
    }

    @Override
    public String toString() {
        return  getID()  + ':' + partType + "->" + getFeatures();
    }

    public static void setLogFeatureAdding(boolean shouldLog){
        LOG_FEATURE_ADDING = shouldLog;
    }

    public static Set<Part> readParts(String partType, OWLReferences ontology){
        // Find all IDs of the parts with a given type (e.g., "MOTOR").
        ClassifiedIndividualDescr clsDescr = new ClassifiedIndividualDescr(partType, ontology);
        clsDescr.readAxioms();
        Set<String> partsId = new HashSet<>();
        for(OWLNamedIndividual p: clsDescr.getIndividuals()){
            partsId.add(ontology.getOWLObjectName(p));
        }
        // find the Features related to each part.
        Set<Part> parts = new HashSet<>();
        for(String id: partsId){
            IndividualDataDescr p = new IndividualDataDescr(id, ontology);
            p.readAxioms();
            Set<OWLFeature<?>> features = new HashSet<>();
            for(DataLinks d: p.getDataProperties()){
                String key = ontology.getOWLObjectName(d.getExpression());
                int valueNumber = d.getValues().size();
                if(valueNumber == 1) {  // Manage primitive feature values.
                    Object value = readPrimitiveFeature(d.getValues().iterator().next());
                    features.add(new OWLFeature<>(key, value, ontology));
                } else if(valueNumber == 2){  // Manage ranges feature values
                    Iterator<OWLLiteral> it = d.getValues().iterator();
                    Range value = readRangeFeature(it.next(), it.next());
                    features.add(new OWLRangeFeature(key, value, ontology));
                } else StaticLogger.logError("Cannot read part (i.e., " + p.getGroundInstanceName() + ") with " + valueNumber + " equal features.");

            }
            parts.add(new Part(id, "MOTOR", features, ontology));
        }
        return parts;
    }
    private static Object readPrimitiveFeature(OWLLiteral v){
        if (v.getDatatype().isDouble())
            return v.parseDouble();
        else if (v.getDatatype().isFloat())
            return v.parseFloat();
        else if (v.getDatatype().isInteger())
            return v.parseInteger();
        else if (v.getDatatype().isString())
            return v.getLiteral();
        else if (v.getDatatype().toString().equals("xsd:long"))
            return Long.valueOf(v.getLiteral());
        else if (v.isBoolean())
            return v.parseBoolean();
        StaticLogger.logError("Cannot parse value " + v + ".");
        return null;
    }
    private static Range readRangeFeature(OWLLiteral literal1, OWLLiteral literal2){
        Float v1 = readOneRangeFeature(literal1);
        Float v2 = readOneRangeFeature(literal2);
        if(v1 != null & v2 != null)
            return new Range(v1, v2);
        StaticLogger.logError("Cannot parse range values " + literal1 + ", " + literal2 + '.');
        return null;
    }
    private static Float readOneRangeFeature(OWLLiteral literal){
        if (literal.getDatatype().isDouble())
            return ((Double) literal.parseDouble()).floatValue();
        else if (literal.getDatatype().isFloat())
            return literal.parseFloat();
        else if (literal.getDatatype().isInteger())
            return  ((Integer) literal.parseInteger()).floatValue();
        else if (literal.getDatatype().toString().equals("xsd:long"))
            return Long.valueOf(literal.getLiteral()).floatValue();
        return null;
    }
}