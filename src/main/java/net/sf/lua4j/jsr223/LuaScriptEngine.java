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

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import java.io.Reader;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScriptEngine implements ScriptEngine
{
    private final static String CLASS_NAME = LuaScriptEngine.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ScriptEngineFactory factory;

    LuaScriptEngine(ScriptEngineFactory factory)
    {
        this.factory = factory;
    }

    public Object eval(String s, ScriptContext scriptContext) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object eval(String s) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object eval(Reader reader) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object eval(String s, Bindings bindings) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object eval(Reader reader, Bindings bindings) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void put(String s, Object o)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object get(String s)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Bindings getBindings(int i)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setBindings(Bindings bindings, int i)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Bindings createBindings()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public ScriptContext getContext()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setContext(ScriptContext scriptContext)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public ScriptEngineFactory getFactory()
    {
        return factory;
    }
}
