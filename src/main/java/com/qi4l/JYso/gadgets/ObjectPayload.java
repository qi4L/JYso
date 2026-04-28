package com.qi4l.JYso.gadgets;

import com.qi4l.JYso.LdapServer;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.Random;
import java.util.Set;

import static com.qi4l.JYso.Starter.caseInsensitiveObjectPayloadMap;

public interface ObjectPayload<T> {

    /*
     * return armed payload object to be serialized that will execute specified
     * command on deserialization
     */
    T getObject(String command) throws Exception;

    class Utils {

        // get payload classes by classpath scanning
        public static Set<Class<? extends ObjectPayload<?>>> getPayloadClasses() {
            final Reflections reflections = new Reflections(ObjectPayload.class.getPackage().getName());
            @SuppressWarnings("unchecked")
            final Set<Class<? extends ObjectPayload<?>>> payloadTypes =
                    (Set<Class<? extends ObjectPayload<?>>>) (Set<?>)
                            reflections.getSubTypesOf(ObjectPayload.class);
            payloadTypes.removeIf(pc -> pc.isInterface() || Modifier.isAbstract(pc.getModifiers()));
            return payloadTypes;
        }


        @SuppressWarnings("unchecked")
        public static Class<? extends ObjectPayload<?>> getPayloadClass(final String className) {
            Class<? extends ObjectPayload<?>> clazz = null;
            try {
                clazz = (Class<? extends ObjectPayload<?>>) Class.forName(className);
            } catch (Exception ignored) {
            }
            if (clazz == null) {
                try {
                    return (Class<? extends ObjectPayload<Object>>) Class.forName(LdapServer.class.getPackage().getName() + ".gadgets." + className);
                } catch (NoClassDefFoundError | Exception e) {
                    clazz = caseInsensitiveObjectPayloadMap.get(LdapServer.class.getPackage().getName() + ".gadgets." + className);
                }
            }
            if (clazz != null && !ObjectPayload.class.isAssignableFrom(clazz)) {
                clazz = null;
            }
            return clazz;
        }


        public static void releasePayload(ObjectPayload<?> payload, Object object) throws Exception {
            if (payload instanceof ReleaseableObjectPayload) {
                ((ReleaseableObjectPayload<?>) payload).release(object);
            }
        }


        //生成随机字符
        public static String generateRandomString(int length) {
            String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            StringBuilder sb = new StringBuilder();

            Random random = new Random();
            for (int i = 0; i < length; i++) {
                int index = random.nextInt(characters.length());
                char randomChar = characters.charAt(index);
                sb.append(randomChar);
            }

            return sb.toString();
        }
    }
}