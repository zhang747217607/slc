package com.slc.agent.model;

/**
 * <p>
 *
 * </p>
 *
 * @author zhangrenhua
 * @date 2019/10/15
 */
public class SourceTemplate {

    public static final String LINE_SPLIT = "/";

    public static final String POINT_SPLIT = ".";

    public static final String METHOD_AGENT_SUFFIX =  "$agent";

    public final static String SOURCE_TEMPLATE  = "{\n"
            + "%s"
            + "        Object result=null;\n"
            + "       try {\n"
            + "            result=($w)%s$agent($$);\n"
            + "        } catch (Throwable e) {\n"
            + "%s"
            + "            throw e;\n"
            + "        }finally{\n"
            + "%s"
            + "        }\n"
            + "        return ($r) result;\n"
            + "}\n";


    public final static String VOID_SOURCE_TEMPLATE = "{\n"
            + "%s"
            + "        try {\n"
            + "            %s$agent($$);\n"
            + "        } catch (Throwable e) {\n"
            + "%s"
            + "            throw e;\n"
            + "        }finally{\n"
            + "%s"
            + "        }\n"
            + "}\n";


}
