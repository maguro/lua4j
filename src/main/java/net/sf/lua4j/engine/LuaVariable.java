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
package net.sf.lua4j.engine;

/**
 * @version $Revision: $ $Date: $
 */
public class LuaVariable implements LuaObject
{
    private LuaObject value;

    public LuaObject getValue()
    {
        return value;
    }

    public void setValue(LuaObject value)
    {
        this.value = value;
    }

    public Object getUnderlying()
    {
        return (value == null ? null : value.getUnderlying());
    }
}