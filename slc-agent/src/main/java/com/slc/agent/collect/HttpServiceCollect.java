package com.slc.agent.collect;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
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

    private static final String METHOD_AGENT_SUFFIX =  "$agent";

    @Override
    public void init(Instrumentation instrumentation) {
        instrumentation.addTransformer(new HttpClassFileTransformer());
    }

    private static class HttpClassFileTransformer implements ClassFileTransformer{

        @Override
        public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
            if(SERVICE_FLAG.equals(className)){
                try {
                    CtClass ctClass = getCtClass(loader, className);
                    CtMethod[] declaredMethods = ctClass.getDeclaredMethods();
                    if(declaredMethods.length > 0){
                        for(CtMethod ctMethod : declaredMethods){
                            if("service".equals(ctMethod.getName())){
                                //copy一个新的方法改名
                                CtMethod agentMethod = CtNewMethod.copy(ctMethod, ctMethod.getName(), ctClass, null);
                                agentMethod.setName(ctMethod.getName() + METHOD_AGENT_SUFFIX);
                                ctClass.addMethod(agentMethod);
                                //重新设置方式体
                                ctMethod.setBody(buildMethodBody(className,ctMethod));
                            }
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
}
