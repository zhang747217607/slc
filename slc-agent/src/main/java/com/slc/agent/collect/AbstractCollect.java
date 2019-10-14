package com.slc.agent.collect;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

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
public abstract class AbstractCollect {

    public static final String LINE_SPLIT = "/";

    public static final String POINT_SPLIT = ".";

    public static final String METHOD_AGENT_SUFFIX =  "$agent";

    private static String startTemplate ;

    private static String errorTemplate ;

    private static String endTemplate ;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("com.slc.agent.model.ServiceBean serviceBean = new com.slc.agent.model.ServiceBean();");
        stringBuilder.append("serviceBean.setStartAgent(\"%s\",\"%s\");");
        startTemplate = stringBuilder.toString();
        errorTemplate = "serviceBean.setErrorAgent(e);";
        endTemplate = "serviceBean.setEndAgent();";
    }

    public abstract void init(Instrumentation instrumentation);

    public static ConcurrentHashMap<ClassLoader, ClassPool> classPoolMap = new ConcurrentHashMap<>();

    public static CtClass getCtClass(ClassLoader classLoader, String className) throws NotFoundException {
        ClassPool classPool = classPoolMap.get(classLoader);
        if(classPool == null){
            classPool = new ClassPool();
            classPool.insertClassPath(new LoaderClassPath(classLoader));
            classPoolMap.put(classLoader,classPool);
        }
        return classPool.get(className.replaceAll(LINE_SPLIT,POINT_SPLIT));
    }

    public static String buildMethodBody(String className, CtMethod ctMethod) throws NotFoundException {
        String template = "void".equals(ctMethod.getReturnType().getName()) ? VOID_SOURCE_TEMPLATE : SOURCE_TEMPLATE;
        String start = String.format(startTemplate, className,ctMethod.getName());
        return  String.format(template, start, ctMethod.getName(),errorTemplate,endTemplate);
    }

    public final static String SOURCE_TEMPLATE  = "{\n"
            + "%s"
            + "        Object result=null;\n"
            + "       try {\n"
            + "            result=($w)%s$agent($$);\n"
            + "        } catch (Throwable e) {\n"
            + "%s"
            + "            throw e;\n"
            + "        }finally{\n"
            + "%s"
            + "        }\n"
            + "        return ($r) result;\n"
            + "}\n";

    private final static String VOID_SOURCE_TEMPLATE = "{\n"
            + "%s"
            + "        try {\n"
            + "            %s$agent($$);\n"
            + "        } catch (Throwable e) {\n"
            + "%s"
            + "            throw e;\n"
            + "        }finally{\n"
            + "%s"
            + "        }\n"
            + "}\n";





}
