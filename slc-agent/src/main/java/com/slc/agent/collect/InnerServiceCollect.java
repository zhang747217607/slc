package com.slc.agent.collect;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

/**
 * <p>
 * 业务service 性能日志收集
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class InnerServiceCollect extends AbstractCollect {

    private static final String SERVICE_FLAG = "ServiceImpl";

    @Override
    public  void init(Instrumentation instrumentation) {
        instrumentation.addTransformer(new ServiceClassFileTransformer());
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


}
