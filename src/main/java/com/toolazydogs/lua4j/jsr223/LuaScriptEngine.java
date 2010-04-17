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
package com.toolazydogs.lua4j.jsr223;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.AbstractScriptEngine;
import java.io.Reader;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScriptEngine extends AbstractScriptEngine implements ScriptEngine, Compilable, Invocable
{
    private final static String CLASS_NAME = LuaScriptEngine.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ScriptEngineFactory factory;

    public LuaScriptEngine()
    {
        this.factory = null;
    }

    public LuaScriptEngine(ScriptEngineFactory factory)
    {
        this.factory = factory;
    }

    public LuaScriptEngine(Bindings bindings)
    {
        super(bindings);
        this.factory = null;
    }

    public LuaScriptEngine(ScriptEngineFactory factory, Bindings bindings)
    {
        super(bindings);
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

    public Bindings createBindings()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public ScriptEngineFactory getFactory()
    {
        return factory;
    }

    public CompiledScript compile(String s) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public CompiledScript compile(Reader reader) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object invokeMethod(Object o, String s, Object... objects) throws ScriptException, NoSuchMethodException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object invokeFunction(String s, Object... objects) throws ScriptException, NoSuchMethodException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getInterface(Class<T> tClass)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public <T> T getInterface(Object o, Class<T> tClass)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}
