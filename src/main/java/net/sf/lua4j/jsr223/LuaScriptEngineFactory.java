/**
 *
 * Copyright 2010 (C) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.lua4j.jsr223;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.util.List;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScriptEngineFactory implements ScriptEngineFactory
{
    private final static String CLASS_NAME = LuaScriptEngineFactory.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public String getEngineName()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getEngineVersion()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getExtensions()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getMimeTypes()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public List<String> getNames()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getLanguageName()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getLanguageVersion()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object getParameter(String s)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getMethodCallSyntax(String s, String s1, String... strings)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getOutputStatement(String s)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public String getProgram(String... strings)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public ScriptEngine getScriptEngine()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}
