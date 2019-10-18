package com.slc.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class App {

    public static void main(String[] args) throws IOException, AgentLoadException, AgentInitializationException, AttachNotSupportedException {

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

//        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
//        server.createContext("/test", new TestHandler());
//        server.start();

       /* List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            if (vmd.displayName().endsWith("HttpAgentDemoApplication")) {
                VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                virtualMachine.loadAgent("F:\\workspace\\evcard-workspace\\slc\\slc-agent\\target\\slc-agent-1.0-SNAPSHOT.jar", "cxs");
                System.out.println("ok");
                virtualMachine.detach();
            }
        }*/
    }

   /* public  static  class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "hello world";
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }*/
}
