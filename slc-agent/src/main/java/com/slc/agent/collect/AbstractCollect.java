package com.slc.agent.collect;

import com.slc.agent.model.SourceTemplate;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public abstract class AbstractCollect implements ClassFileTransformer {

    public static void init(Instrumentation instrumentation,ClassFileTransformer classFileTransformer) {
        instrumentation.addTransformer(classFileTransformer);
    }

    public static ConcurrentHashMap<ClassLoader, ClassPool> classPoolMap = new ConcurrentHashMap<>();

    public static CtClass getCtClass(ClassLoader classLoader, String className) throws NotFoundException {
        ClassPool classPool = classPoolMap.get(classLoader);
        if(classPool == null){
            classPool = new ClassPool();
            classPool.insertClassPath(new LoaderClassPath(classLoader));
            classPoolMap.put(classLoader,classPool);
        }
        return classPool.get(className.replaceAll(SourceTemplate.LINE_SPLIT,SourceTemplate.POINT_SPLIT));
    }
}
