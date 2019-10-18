package com.slc.agent.collect;

import com.slc.agent.model.SourceTemplate;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * <p>
 * http 服务性能日志收集
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class HttpServiceCollect extends AbstractCollect {

    private static final String SERVICE_FLAG = "javax.servlet.http.HttpServlet";

    private static final String P1_CLASS_NAME = "HttpServletRequest";

    private static final String P2_CLASS_NAME = "HttpServletResponse";

    private static String startTemplate ;

    private static String errorTemplate ;

    private static String endTemplate ;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("com.slc.agent.model.HttpServiceBean serviceBean = new com.slc.agent.model.HttpServiceBean();\r\n");
        stringBuilder.append("serviceBean.setStartAgent($1);\r\n");
        startTemplate = stringBuilder.toString();
        errorTemplate = "serviceBean.setErrorAgent(e);\r\n";
        stringBuilder = new StringBuilder();
        stringBuilder.append("serviceBean.setEndAgent($2);\r\n");
        stringBuilder.append("com.slc.agent.context.SlcContext.logHandler.execute(serviceBean);  \r\n");
        endTemplate = stringBuilder.toString();
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if(SERVICE_FLAG.equals(className.replaceAll(SourceTemplate.LINE_SPLIT,SourceTemplate.POINT_SPLIT))){
            try {
                CtClass ctClass = getCtClass(loader, className);
                CtMethod ctMethod = ctClass.getDeclaredMethod("service");
                CtClass[] parameterTypes = ctMethod.getParameterTypes();
                if(P1_CLASS_NAME.equals(parameterTypes[0].getSimpleName()) &&
                        P2_CLASS_NAME.equals(parameterTypes[1].getSimpleName()) ){
                    //copy一个新的方法改名
                    CtMethod agentMethod = CtNewMethod.copy(ctMethod, ctMethod.getName(), ctClass, null);
                    agentMethod.setName(ctMethod.getName() + SourceTemplate.METHOD_AGENT_SUFFIX);
                    ctClass.addMethod(agentMethod);
                    //重新设置方式体
                    ctMethod.setBody(buildMethodBody(ctMethod));
                    return ctClass.toBytecode();
                }
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String buildMethodBody(CtMethod ctMethod) throws NotFoundException {
        String template = "void".equals(ctMethod.getReturnType().getName()) ? SourceTemplate.VOID_SOURCE_TEMPLATE : SourceTemplate.SOURCE_TEMPLATE;
        return  String.format(template, startTemplate, ctMethod.getName(),errorTemplate,endTemplate);
    }

}
