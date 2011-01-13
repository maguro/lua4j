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
package com.toolazydogs.lua4j.engine;

import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaCompiledScript extends CompiledScript
{
    private final static String CLASS_NAME = LuaCompiledScript.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final ScriptEngine scriptEngine;

    public LuaCompiledScript(ScriptEngine scriptEngine)
    {
        this.scriptEngine = scriptEngine;
    }

    @Override
    public Object eval(ScriptContext scriptContext) throws ScriptException
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ScriptEngine getEngine()
    {
        return scriptEngine;
    }
}
