package com.teseotech.partsInterface.implementation.kernel;

import com.teseotech.partsInterface.core.BaseFeature;
import com.teseotech.partsInterface.core.BaseKernel;
import com.teseotech.partsInterface.utility.StaticLogger;

import java.util.ArrayList;
import java.util.List;

/*
 * It extends `BaseKernel` to implement a Kernel comparing two Features with a `Number` Datatype.
 * The comparison is done with a fuzzy membership function defined through a set of points, i.e.,
 * the parameter `List<KernelPointParam>>`, which must be ordered with an ascending `value`.
 * The fuzzy function is then represented with a linear interpolation between point, and the
 * degree given an `actual` Feature value is computed.
 * This kernel requires at least two point given as parameters.
 */
public class KernelPoint extends BaseKernel<Number, List<KernelPointParam>> {
    public KernelPoint(String targetKey, Number targetValue, List<KernelPointParam> parameter) {
        super(targetKey, targetValue, parameter);
        checkParameterSize(parameter);
    }
    public KernelPoint(String targetKey, Number targetValue, List<KernelPointParam> parameter, float weight) {
        super(targetKey, targetValue, parameter, weight);
        checkParameterSize(parameter);
    }
    private void checkParameterSize(List<KernelPointParam> parameter) {
        if(getParameters().size() <= 1)
            StaticLogger.logError("Cannot evaluate " + this.getClass().getSimpleName() + " with parameters " + parameter + '.');
    }

    @Override
    public <X extends BaseFeature<?>> Float evaluateChecked(X actual) {
        // `this` is the target, `actual` is the value from the ontology.
        Number actualValue = (Number) actual.getValue();
        // compute distance
        float distance = (actualValue.floatValue() - this.getValue().floatValue()) / this.getValue().floatValue();

        // compute the output based on intervals given as parameters (p0,p1,...pn)
        ArrayList<KernelPointParam> paramCopy = new ArrayList<>(getParameters());
        KernelPointParam p0 = getParameters().get(0);
        // If it is below the first interval `p0`.
        if (distance < p0.getValue())
            return p0.getDegree();
        else {
            KernelPointParam pn = getParameters().get(getParameters().size() - 1);
            // If it is above the last interval `pn`.
            if (distance > pn.getValue())
                return pn.getDegree();
            else {
                // If it is within an interval p(i-1) (i.e., `pPrevious`) and p(i) (i.e., `pNext`) use linear interpolation.
                paramCopy.remove(p0);
                paramCopy.remove(pn);
                KernelPointParam pPrevous = p0;
                for(KernelPointParam pNext: paramCopy){
                    if(distance >= pPrevous.getValue() & distance < pNext.getValue())
                        return linearInterpolation(distance, pPrevous, pNext);
                    pPrevous = pNext;
                }
                if(distance >= pPrevous.getValue() & distance < pn.getValue())
                    return linearInterpolation(distance, pPrevous, pn);
            }
        }
        return null;
    }

    private float linearInterpolation(float x, KernelPointParam p1, KernelPointParam p2){
        // y = y1 + (x - x1)/(x2 - x1) * (y2 - y1) where:
        //   x1 = p1.getValue(),  y2 = p2.getValue();
        //   x2 = p2.getValue(),  y2 = p2.getDegree().
        return p1.getDegree() + (((x - p1.getValue())/(p2.getValue() - p1.getValue())) * (p2.getDegree() - p1.getDegree()));
    }
}
