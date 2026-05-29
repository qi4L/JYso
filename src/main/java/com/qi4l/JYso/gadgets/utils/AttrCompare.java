package com.qi4l.JYso.gadgets.utils;

import org.w3c.dom.Attr;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings("rawtypes")
public class AttrCompare implements Comparator, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1 instanceof Attr && o2 instanceof Attr) {
            Attr a1 = (Attr) o1;
            Attr a2 = (Attr) o2;

            int ns = compareNullable(a1.getNamespaceURI(), a2.getNamespaceURI());
            if (ns != 0) {
                return ns;
            }

            int localName = compareNullable(a1.getLocalName(), a2.getLocalName());
            if (localName != 0) {
                return localName;
            }

            int name = compareNullable(a1.getName(), a2.getName());
            if (name != 0) {
                return name;
            }

            return compareNullable(a1.getValue(), a2.getValue());
        }
        return o1.toString().compareTo(o2.toString());
    }

    private static int compareNullable(String s1, String s2) {
        if (s1 == s2) {
            return 0;
        }
        if (s1 == null) {
            return -1;
        }
        if (s2 == null) {
            return 1;
        }
        return s1.compareTo(s2);
    }
}
