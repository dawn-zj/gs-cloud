package com.gs.schedule.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PackageUtil {
    public static List<Class<?>> getClasses(String packageName) throws Exception {
        ArrayList<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace(".", "/");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File directory = new File(classLoader.getResource(path).getFile());
        if (directory.exists()) {
            File[] files = directory.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.endsWith(".class")) {

                    String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                    Class<?> aClass = Class.forName(className);
                    classes.add(aClass);
                }

            }
        }
        return classes;
    }
}
