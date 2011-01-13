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
import javax.script.ScriptContext;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScriptContext implements ScriptContext
{
    private final static String CLASS_NAME = LuaScriptContext.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);

    public void setBindings(Bindings bindings, int i)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Bindings getBindings(int i)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setAttribute(String s, Object o, int i)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAttribute(String s, int i)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object removeAttribute(String s, int i)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Object getAttribute(String s)
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public int getAttributesScope(String s)
    {
        return 0;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Writer getWriter()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Writer getErrorWriter()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setWriter(Writer writer)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setErrorWriter(Writer writer)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public Reader getReader()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public void setReader(Reader reader)
    {
        //Todo change body of implemented methods use File | Settings | File Templates.
    }

    public List<Integer> getScopes()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}
