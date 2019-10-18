package com.slc.agent.context;

import com.slc.agent.collect.AbstractCollect;
import com.slc.agent.handler.LogHandler;

import java.lang.instrument.Instrumentation;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/17
 */
public class SlcContext {

    public static LogHandler logHandler;

    private Instrumentation instrumentation;

    public SlcContext(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
        logHandler = new LogHandler();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            private volatile boolean hasShutdown = false;
            @Override
            public void run() {
                synchronized (this) {
                    if (!this.hasShutdown) {
                        this.hasShutdown = true;
                        logHandler.shutdown();

                    }
                }
            }
        }, "ShutdownHook"));

    }

    public void addCollect(AbstractCollect abstractCollect){
        this.instrumentation.addTransformer(abstractCollect);
    }

}
