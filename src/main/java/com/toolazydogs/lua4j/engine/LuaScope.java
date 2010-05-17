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
package com.toolazydogs.lua4j.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaScope implements LuaTable
{
    private final static String CLASS_NAME = LuaScope.class.getName();
    private final static Logger LOGGER = Logger.getLogger(CLASS_NAME);

    private final Map<String, LuaObject> map = new HashMap<String, LuaObject>();


    public int size() { return map.size(); }

    public boolean isEmpty() { return map.isEmpty(); }

    public boolean containsKey(Object key) { return map.containsKey(key); }

    public boolean containsValue(Object value) { return map.containsValue(value); }

    public LuaObject get(Object key) { return map.get(key); }

    public LuaObject put(String key, LuaObject value) { return map.put(key, value); }

    public LuaObject remove(Object key) { return map.remove(key); }

    public void putAll(Map<? extends String, ? extends LuaObject> m) { map.putAll(m); }

    public void clear() { map.clear(); }

    public Set<String> keySet() { return map.keySet(); }

    public Collection<LuaObject> values() { return map.values(); }

    public Set<Entry<String, LuaObject>> entrySet() { return map.entrySet(); }

    public Object getUnderlying()
    {
        return null;  //Todo change body of implemented methods use File | Settings | File Templates.
    }
}
