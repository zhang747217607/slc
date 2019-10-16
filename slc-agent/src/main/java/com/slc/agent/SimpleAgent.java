package com.slc.agent;

import com.slc.agent.collect.HttpServiceCollect;
import com.slc.agent.collect.ServiceCollect;

import java.lang.instrument.Instrumentation;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/14
 */
public class SimpleAgent {

  /*  public static void premain(String arg, Instrumentation instrumentation) {
        instrumentation.addTransformer(new ServiceCollect());
        instrumentation.addTransformer(new HttpServiceCollect());
    }*/

    public static void agentmain(String arg, Instrumentation instrumentation){
        instrumentation.addTransformer(new ServiceCollect());
        instrumentation.addTransformer(new HttpServiceCollect());
    }
}
