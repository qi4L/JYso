package com.qi4l.JYso.gadgets.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"rawtypes", "unchecked","unused"})
public class ThirdLibsClassLoader extends URLClassLoader {
    public static final String thirdLibDir = "chains-config/third-libs";
    private static final String commonDir = "common";
    private static final Logger log = LoggerFactory.getLogger(ThirdLibsClassLoader.class);
    private static ThirdLibsClassLoader INSTANCE = null;
    private static boolean initialized = false;
    private static Map<String, URLClassLoader> pluginClassLoaderMap = new HashMap();

    static {
        initClassLoader();
    }

    public static ThirdLibsClassLoader getInstance() {
        return INSTANCE;
    }

    public static void initClassLoader() {
        try {
            if (initialized) {
                return;
            }
            try {
                INSTANCE = init();
                initDirectoryClassLoaders();
                initialized = true;
            } catch (MalformedURLException e) {
                throw new RuntimeException("Failed to initialize class loader", e);
            }
        } catch (Throwable th) {
            initialized = true;
            throw th;
        }
    }

    public static void reload() {
        INSTANCE = null;
        initialized = false;
        pluginClassLoaderMap = new HashMap();
        initClassLoader();
    }

    public ThirdLibsClassLoader(URL[] urls) {
        super(urls, Thread.currentThread().getContextClassLoader());
    }

    public ThirdLibsClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            try {
                c = findClass(name);
            } catch (ClassNotFoundException e) {
                c = super.loadClass(name, resolve);
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    private static ThirdLibsClassLoader init() throws MalformedURLException {
        ThirdLibsClassLoader loader = new ThirdLibsClassLoader(new URL[0]);
        File commonDirectory = new File(thirdLibDir, "common");
        List<URL> jarUrls = new ArrayList<>();
        findJarFiles(commonDirectory, jarUrls);
        for (URL url : jarUrls) {
            log.debug("Add common lib: {}", url);
            loader.addURL(url);
        }
        log.info("Loaded {} libraries from common directory", jarUrls.size());
        return loader;
    }

    private static void initDirectoryClassLoaders() throws MalformedURLException {
        File baseDir = new File(thirdLibDir);
        List<File> subDirs = getFirstLevelDirectories(baseDir);
        for (File dir : subDirs) {
            if (!dir.getName().equals("common")) {
                List<URL> jarUrls = new ArrayList<>();
                findJarFiles(dir, jarUrls);
                if (!jarUrls.isEmpty()) {
                    ThirdLibsClassLoader dirClassLoader = new ThirdLibsClassLoader(jarUrls.toArray(new URL[0]), INSTANCE);
                    pluginClassLoaderMap.put(dir.getName(), dirClassLoader);
                    log.info("Loaded {} libraries from directory: {}", jarUrls.size(), dir.getName());
                }
            }
        }
    }

    private static void findJarFiles(File dir, List<URL> jarUrls) throws MalformedURLException {
        File[] files;
        if (dir.exists() && dir.isDirectory() && (files = dir.listFiles()) != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    findJarFiles(file, jarUrls);
                } else if (file.getName().endsWith(".jar")) {
                    if (file.getName().startsWith("_")) {
                        log.info("Skip third lib {}", file.getName());
                    } else {
                        jarUrls.add(file.toURI().toURL());
                    }
                }
            }
        }
    }

    private static List<File> getFirstLevelDirectories(File baseDir) {
        File[] files;
        List<File> directories = new ArrayList<>();
        if (baseDir.exists() && baseDir.isDirectory() && (files = baseDir.listFiles()) != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    directories.add(file);
                }
            }
        }
        return directories;
    }

    public static Class<?> loadClass_(String className) throws ClassNotFoundException {
        return INSTANCE.loadClass(className);
    }

    public static URLClassLoader getClassLoader() {
        return INSTANCE;
    }

    public static URLClassLoader getClassLoaderForDirectory(String directoryName) {
        return pluginClassLoaderMap.get(directoryName);
    }

    public static Map<String, URLClassLoader> getPluginClassLoaderMap() {
        return pluginClassLoaderMap;
    }
}