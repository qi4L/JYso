package com.qi4l.JYso.gadgets;

import java.util.Comparator;

public class UtilFactory {

    // 实现接口中的所有方法
    public Object makeHashCodeTrigger(Object o1) throws Exception {
        return JDKUtil.makeMap(o1, o1);
    }

    public Object makeEqualsTrigger(Object tgt, Object sameHash) throws Exception {
        return JDKUtil.makeMap(tgt, sameHash);
    }

    public Object makeToStringTriggerUnstable ( Object obj ) throws Exception {
        return ToStringUtil.makeSpringAOPToStringTrigger(obj);
    }

    public Object makeToStringTriggerStable(Object obj) throws Exception {
        return ToStringUtil.makeToStringTrigger(obj);
    }

    public Object makeIteratorTrigger(Object it) throws Exception {
        return JDKUtil.makeIteratorTriggerNative(this, it);
    }

    public Object makeComparatorTrigger(Object tgt, Comparator<?> cmp) throws Exception {
        return JDKUtil.makeTreeMap(tgt, cmp);
    }
}
