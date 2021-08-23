package com.teseotech.partsInterface.implementation.partEvaluation.implementation.owlInterface;

import com.teseotech.partsInterface.implementation.partEvaluation.core.BasePart;
import com.teseotech.partsInterface.implementation.partEvaluation.core.utility.StaticLogger;
import com.teseotech.partsInterface.implementation.partEvaluation.implementation.Part;
import it.emarolab.amor.owlInterface.OWLReferences;
import it.emarolab.owloop.descriptor.construction.descriptorEntitySet.DataLinks;
import it.emarolab.owloop.descriptor.utility.classDescriptor.FullClassDesc;
import it.emarolab.owloop.descriptor.utility.individualDescriptor.FullIndividualDesc;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import java.util.HashSet;
import java.util.Set;

// Hypothesis1: all the `Features` of a part as a unique `key`.  // todo make it inconsistent in the ontology?
// Hypothesis2: each instance of `Part` has a single annotation, which is added by this app.
public abstract class OWLPart extends BasePart<OWLFeature<?>> {
    private final String partType;
    static private boolean LOG_FEATURE_ADDING = true;
    private final FullIndividualDesc indDescriptor;  // TODO use dedicated OWLOOP descriptors (see also elsewhere)

    public OWLPart(String identifier, String partType, Set<? extends OWLFeature<?>> features, OWLReferences ontology) {
        super(identifier, features);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
    }
    public OWLPart(String partType, Set<OWLFeature<?>> features, OWLReferences ontology) {
        super(null, features);
        this.partType = partType;
        this.indDescriptor = createOWLDescriptor(ontology);
    }
    private FullIndividualDesc createOWLDescriptor(OWLReferences ontology){
        try {
            FullIndividualDesc indDescr = new FullIndividualDesc(getID(), ontology);
            indDescr.readAxioms();
            indDescr.addTypeIndividual(this.getType());
            return indDescr;
        } catch (Exception e){
            StaticLogger.logError("Cannot use ontology '" + ontology + "'!");
            //e.printStackTrace();
            return null;
        }
    }

    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void addPart() {}
    @Override @Deprecated  // it can be used if a PART needs some general definition in the TBox.
    public void removePart() {}

    @Override
    public boolean exists() { // Used by `AddRemoveChecker.shouldAdd()` and `AddRemoveChecker.shouldRemove()`.
        return !this.indDescriptor.getDataProperties().isEmpty();
    }

    @Override
    public void addInstance() {
        if(this.shouldAdd(LOG_FEATURE_ADDING)) {
            for (OWLFeature<?> f : this.getFeatures()) {
                f.addFeature();  // It might make some changes to the TBox. // todo add flag to disable it and make example to add them through `Features`
                this.indDescriptor.addData(f.getKey(), f.getValue());
            }
            this.indDescriptor.writeAxioms();
        }
    }
    @Override
    public void removeInstance() {
        if(this.shouldRemove(LOG_FEATURE_ADDING))
            removeInstance(indDescriptor.getOntologyReference(), getID()); // Added `Features` are not removed.
    }

    static public void removeInstance(OWLReferences ontoRef, String instanceName){
        ontoRef.removeIndividual(instanceName);
    }

    public String getType() {
        return partType;
    }
    public FullIndividualDesc getOWLDescriptor() {
        return this.indDescriptor;
    }

    @Override
    public String toString() {
        return  getID()  + ':' + partType + "->" + getFeatures();
    }

    static public void setLogFeatureAdding(boolean shouldLog){
        LOG_FEATURE_ADDING = shouldLog;
    }

    static public Set<Part> readParts(String partType, OWLReferences ontology){
        // Find all IDs of the parts with a given type (e.g., "MOTOR").
        FullClassDesc clsDescr = new FullClassDesc(partType, ontology);
        clsDescr.readAxioms();
        Set<String> partsId = new HashSet<>();
        for(OWLNamedIndividual p: clsDescr.getIndividuals()){
            partsId.add(ontology.getOWLObjectName(p));
        }
        // find the Features related to each part.
        Set<Part> parts = new HashSet<>();
        for(String id: partsId){
            FullIndividualDesc p = new FullIndividualDesc(id, ontology);
            p.readAxioms();
            Set<OWLFeature<?>> features = new HashSet<>();
            for( DataLinks d: p.getDataProperties()){
                String key = ontology.getOWLObjectName(d.getExpression());
                Object value = null;
                for(OWLLiteral v: d.getValues()){
                    if(v.getDatatype().isDouble())
                        value = v.parseDouble();
                    else if(v.getDatatype().isFloat())
                        value = v.parseFloat();
                    else if(v.getDatatype().isInteger())
                        value = v.parseInteger();
                    else if(v.getDatatype().isString())
                        value = v.getLiteral();
                    else if(v.getDatatype().toString().equals("xsd:long"))
                        value = new Long(v.getLiteral());
                    else if(v.isBoolean())
                        value = v.parseBoolean();
                    if(value == null)
                        StaticLogger.logError("Cannot parse value " + v + ".");
                    break;  // Hypothesis: each feature of a part has a unique `key`.
                }
                features.add(new OWLFeature<>(key, value, ontology));
            }
            parts.add(new Part(id, "MOTOR", features, ontology));
        }
        return parts;
    }
}