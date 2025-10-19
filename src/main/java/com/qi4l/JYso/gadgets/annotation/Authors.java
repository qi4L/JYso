package com.qi4l.JYso.gadgets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedElement;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authors {

    String FROHOFF = "frohoff";

    String PWNTESTER = "pwntester";

    String CSCHNEIDER4711 = "cschneider4711";

    String MBECHLER = "mbechler";

    String JACKOFMOSTTRADES = "JackOfMostTrades";

    String MATTHIASKAISER = "matthias_kaiser";

    String GEBL = "gebl";

    String JACOBAINES = "jacob-baines";

    String JASINNER = "jasinner";

    String KULLRICH = "kai_ullrich";

    String TINT0 = "_tint0";

    String SCRISTALLI = "scristalli";

    String HANYRAX = "hanyrax";

    String EDOARDOVIGNATI = "EdoardoVignati";

    String JANG = "Jang";

    String ARTSPLOIT  = "artsploit";
    String CCKUAILONG = "CCKUAILONG";

    String QI4L       = "QI4L";
    String PEIQIF4CK = "peiqiF4ck";

    String Jayl1n = "Jayl1n";

    String N1ght = "N1ght";

    String KILLER = "killer";

    String UNAM4   = "Unam4";
    String JIECUB3 = "jiecub3";

    String JSJCW = "jsjcw";

    String[] value() default {};

    class Utils {

        public static String[] getAuthors(AnnotatedElement annotated) {
            Authors authors = annotated.getAnnotation(Authors.class);
            if (authors != null && authors.value() != null) {
                return authors.value();
            } else {
                return new String[0];
            }
        }
    }
}
