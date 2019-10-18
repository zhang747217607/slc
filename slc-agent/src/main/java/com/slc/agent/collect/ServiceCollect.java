package com.slc.agent.collect;

import com.slc.agent.model.SourceTemplate;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * <p>
 * 业务service 性能日志收集
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class ServiceCollect extends AbstractCollect {

    private static final String SERVICE_FLAG = "ServiceImpl";

    private static String startTemplate ;

    private static String errorTemplate ;

    private static String endTemplate ;


    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("com.slc.agent.model.ServiceBean serviceBean = new com.slc.agent.model.ServiceBean();\r\n");
        stringBuilder.append("serviceBean.setStartAgent(\"%s\",\"%s\"); \r\n");
        startTemplate = stringBuilder.toString();
        errorTemplate = "serviceBean.setErrorAgent(e); \r\n";
        stringBuilder = new StringBuilder();
        stringBuilder.append("serviceBean.setEndAgent(); \r\n");
        stringBuilder.append("com.slc.agent.context.SlcContext.logHandler.execute(serviceBean);  \r\n");
        endTemplate = stringBuilder.toString();
    }

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
                        agentMethod.setName(ctMethod.getName() + SourceTemplate.METHOD_AGENT_SUFFIX);
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

    private static String buildMethodBody(String className, CtMethod ctMethod) throws NotFoundException {
        String template = "void".equals(ctMethod.getReturnType().getName()) ? SourceTemplate.VOID_SOURCE_TEMPLATE : SourceTemplate.SOURCE_TEMPLATE;
        String start = String.format(startTemplate, className,ctMethod.getName());
        return  String.format(template, start, ctMethod.getName(),errorTemplate,endTemplate);
    }

}
