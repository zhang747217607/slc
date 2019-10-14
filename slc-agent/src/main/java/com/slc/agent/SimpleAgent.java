package com.slc.agent;

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

    public static void premain(String arg, Instrumentation instrumentation) {
        ServiceCollect.init(instrumentation);
    }
}
