/**
 *
 * Copyright 2009 (C) The original author or authors
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
package net.sf.lua4j.engine.vm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.sf.lua4j.engine.LuaObject;

/**
 * @version $Revision: $ $Date: $
 */
public class Frame
{
    private final static String CLASS_NAME = Frame.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);
    private final LuaObject[] registers;
    private final LuaObject[] constants;

    public Frame(int numRegisters, int numConstants)
    {
        registers = new LuaObject[numRegisters];
        constants = new LuaObject[numConstants];
    }

    public LuaObject[] getRegisters()
    {
        return registers;
    }

    public LuaObject[] getConstants()
    {
        return constants;
    }
}
