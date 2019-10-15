package com.slc.service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class App {

    public static void main(String[] args) throws IOException {

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
