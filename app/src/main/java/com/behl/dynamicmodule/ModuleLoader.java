package com.behl.dynamicmodule;

import java.io.File;

import dalvik.system.DexClassLoader;

public class ModuleLoader {
    private final String cacheDir;

    public ModuleLoader(String cacheDir) {
        this.cacheDir = cacheDir;
    }

    public DynamicModuleInterface load(File dex) {
         String className="com.behl.dynamicmodule.DynamicModule";
        DexClassLoader dexClassLoader = new DexClassLoader(dex.getAbsolutePath(), cacheDir,
                null, this.getClass().getClassLoader());
        try {
            Class<?> aClass = dexClassLoader.loadClass(className);
            if (DynamicModuleInterface.class.isAssignableFrom(aClass)) {
                return ((DynamicModuleInterface) aClass.newInstance());
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
