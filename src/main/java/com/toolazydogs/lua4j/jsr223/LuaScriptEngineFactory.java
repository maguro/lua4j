/**
 *
 * Copyright 2010-2011 (C) Alan D. Cabrera
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
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScriptEngineFactory implements ScriptEngineFactory
{
    private final static String CLASS_NAME = LuaScriptEngineFactory.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final Properties properties = new Properties();
    private final ScriptEngine scriptEngine = new LuaScriptEngine(this, new Bindings()
    {
        public boolean containsKey(Object o) { return properties.containsKey(o); }

        public Object get(Object o) { return properties.get(o); }

        public Object put(String s, Object o) { throw new UnsupportedOperationException(); }

        public void putAll(Map<? extends String, ?> map) { throw new UnsupportedOperationException(); }

        public Object remove(Object o) { throw new UnsupportedOperationException(); }

        public int size() { return properties.size(); }

        public boolean isEmpty() { return properties.isEmpty(); }

        public boolean containsValue(Object value) { return properties.containsValue(value); }

        public void clear() { throw new UnsupportedOperationException(); }

        public Set<String> keySet()
        {
            Set<String> keys = new HashSet<String>();

            for (Object key : properties.keySet())
            {
                keys.add((String)key);
            }

            return Collections.unmodifiableSet(keys);
        }

        public Collection<Object> values()
        {
            return Collections.unmodifiableSet(new HashSet<Object>(properties.values()));
        }

        public Set<Entry<String, Object>> entrySet()
        {
            Map<String, Object> map = new HashMap<String, Object>();

            for (Entry<Object, Object> entry : properties.entrySet())
            {
                map.put((String)entry.getKey(), entry.getValue());
            }

            return Collections.unmodifiableMap(map).entrySet();
        }
    });

    public LuaScriptEngineFactory()
    {
        properties.setProperty(ScriptEngine.NAME, getEngineName());
        properties.setProperty(ScriptEngine.LANGUAGE, getLanguageName());
        properties.setProperty(ScriptEngine.LANGUAGE_VERSION, getLanguageVersion());
        properties.setProperty("THREADING", "STATELESS");

        try
        {
            properties.load(LuaScriptEngineFactory.class.getResourceAsStream("lua4j.properties"));
        }
        catch (IOException ioe)
        {
            LOGGER.log(Level.WARNING, "Unable to load engine properties", ioe);
        }
    }

    public String getEngineName()
    {
        return "lua4j";
    }

    public String getEngineVersion()
    {
        return properties.getProperty("net.toolazydogs.lua4j.version");
    }

    public List<String> getExtensions()
    {
        return Collections.singletonList("lua");
    }

    public List<String> getMimeTypes()
    {
        return Collections.singletonList("application/lua");
    }

    public List<String> getNames()
    {
        return Arrays.asList("lua", "lua4j");
    }

    public String getLanguageName()
    {
        return "lua";
    }

    public String getLanguageVersion()
    {
        return "5.1";
    }

    public Object getParameter(String s)
    {
        if (ScriptEngine.ENGINE.equals(s)) return getScriptEngine();

        return properties.getProperty(s);
    }

    public String getMethodCallSyntax(String object, String method, String... arguments)
    {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        builder.append(object).append(".").append(method).append("(");
        for (String argument : arguments)
        {
            if (first) first = false;
            else builder.append(", ");

            builder.append(argument);
        }
        builder.append(")");

        return builder.toString();
    }

    public String getOutputStatement(String s)
    {
        return "print(" + s + ")";
    }

    public String getProgram(String... statements)
    {
        StringBuilder builder = new StringBuilder();

        for (String statement : statements)
        {
            builder.append(statement).append("; ");
        }

        return builder.toString();
    }

    public ScriptEngine getScriptEngine()
    {
        return scriptEngine;
    }
}
