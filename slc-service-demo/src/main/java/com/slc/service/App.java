package com.slc.service;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class App {

    public static void main(String[] args) {

     /*   ClassPool cp = ClassPool.getDefault();
        try {
            CtClass cc = cp.get("com.slc.service.UserServiceImpl");
            CtMethod m = cc.getDeclaredMethod("getUser");
            m.insertBefore("System.out.println(System.currentTimeMillis());");
            Class c = cc.toClass();
            UserServiceImpl h = (UserServiceImpl)c.newInstance();
            h.getUser("zzz");
        } catch (NotFoundException | IllegalAccessException | InstantiationException | CannotCompileException e) {
            e.printStackTrace();
        }
*/
        new UserServiceImpl().getUser("11");

    }
}
