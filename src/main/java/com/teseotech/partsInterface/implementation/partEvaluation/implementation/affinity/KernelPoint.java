package com.teseotech.partsInterface.implementation.partEvaluation.implementation.affinity;

import com.teseotech.partsInterface.implementation.partEvaluation.core.BaseFeature;
import com.teseotech.partsInterface.implementation.partEvaluation.core.Kernel;

class KernelPointParam{}

public class KernelPoint extends Kernel<Number, KernelPointParam> {
    public KernelPoint(String key, Number target, KernelPointParam parameter) {
        super(key, target, parameter);
    }

    @Override
    public <X extends BaseFeature<?>> float evaluate(X actual) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
        return 0;
    }
}
