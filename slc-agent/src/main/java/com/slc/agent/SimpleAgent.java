package com.slc.agent;

import com.slc.agent.collect.HttpServiceCollect;
import com.slc.agent.collect.InnerServiceCollect;

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
        new InnerServiceCollect().init(instrumentation);
        //new HttpServiceCollect().init(instrumentation);
    }
}
