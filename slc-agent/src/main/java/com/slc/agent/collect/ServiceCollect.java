package com.slc.agent.collect;

import com.slc.agent.model.ServiceBean;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class ServiceCollect {

    private static final String LINE_SPLIT = "/";

    private static final String POINT_SPLIT = ".";

    private static final String SERVICE_FLAG = "ServiceImpl";

    private static final String METHOD_AGENT_SUFFIX =  "$agent";

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


    public static void init(Instrumentation instrumentation) {
        instrumentation.addTransformer(new ServiceClassFileTransformer());
    }


    private static ConcurrentHashMap<ClassLoader, ClassPool> classPoolMap = new ConcurrentHashMap<>();

    private static CtClass getCtClass(ClassLoader classLoader,String className) throws NotFoundException {
        ClassPool classPool = classPoolMap.get(classLoader);
        if(classPool == null){
            classPool = new ClassPool();
            classPool.insertClassPath(new LoaderClassPath(classLoader));
            classPoolMap.put(classLoader,classPool);
        }
        return classPool.get(className.replaceAll(LINE_SPLIT,POINT_SPLIT));
    }

    private static class ServiceClassFileTransformer implements ClassFileTransformer{

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if(className.contains(SERVICE_FLAG)){
                try {
                    CtClass ctClass = getCtClass(loader, className);
                    CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                    if(declaredMethods.length > 0){
                        for(CtMethod ctMethod : declaredMethods){
                            //过滤不需要处理的方法
                            if (!Modifier.isPublic(ctMethod.getModifiers())) {
                                continue;
                            }
                            if (Modifier.isStatic(ctMethod.getModifiers())) {
                                continue;
                            }
                            if (Modifier.isNative(ctMethod.getModifiers())) {
                                continue;
                            }
                            //copy一个新的方法改名
                            CtMethod agentMethod = CtNewMethod.copy(ctMethod, ctMethod.getName(), ctClass, null);
                            agentMethod.setName(ctMethod.getName() + METHOD_AGENT_SUFFIX);
                            ctClass.addMethod(agentMethod);
                            //重新设置方式体
                            ctMethod.setBody(buildMethodBody(className,ctMethod));
                        }
                    }
                    return ctClass.toBytecode();
                } catch (NotFoundException | CannotCompileException | IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    private static String buildMethodBody(String className,CtMethod ctMethod) throws NotFoundException {
        String template = "void".equals(ctMethod.getReturnType().getName()) ? VOID_SOURCE_TEMPLATE : SOURCE_TEMPLATE;
        String start = String.format(startTemplate, className,ctMethod.getName());
        return  String.format(template, start, ctMethod.getName(),errorTemplate,endTemplate);
    }

    private final static String SOURCE_TEMPLATE  = "{\n"
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
