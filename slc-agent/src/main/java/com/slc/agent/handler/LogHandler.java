package com.slc.agent.handler;

import com.slc.agent.context.MyClassLoader;
import com.slc.agent.model.BaseBean;
import com.slc.agent.model.HttpServiceBean;
import com.slc.agent.model.ServiceBean;
import com.slc.agent.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/17
 */
public class LogHandler {

    private ExecutorService executorService;

    public LogHandler(){
        executorService = Executors.newCachedThreadPool(new ThreadFactory() {
            private AtomicInteger threadIndex = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "LogHandlerExecutor_" + this.threadIndex.incrementAndGet());
            }
        });

    }

    public  void execute(BaseBean baseBean){
        executorService.submit(new LogTask(baseBean));
    }

    public  void shutdown(){
        if(executorService != null){
            executorService.shutdown();
        }
    }

    public class LogTask implements Runnable{

        private BaseBean baseBean;

        public LogTask(BaseBean baseBean) {
            this.baseBean = baseBean;
        }

        @Override
        public void run() {
            Class<?> aClass = null;
            try {
                aClass = Class.forName("com.slc.agent.handler.SlcLoggerFactory");
                Method method = null;
                if(baseBean instanceof HttpServiceBean){
                    method = aClass.getDeclaredMethod("getHttpLogger");
                    method.invoke(JsonUtil.toJsonStr(baseBean));
                }else if (baseBean instanceof ServiceBean){
                    method = aClass.getDeclaredMethod("getServiceLogger");
                }
                if(method != null){
                    Object o = method.invoke(null);
                    ((Logger)o).info(JsonUtil.toJsonStr(baseBean));
                }
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

        }
    }

}
