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
package com.toolazydogs.lua4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.junit.Test;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaWalkerTest
{
    @Test
    public void test() throws Exception
    {
        LuaParser p = getParser("i, v:car(a, b, c):cdr(1, 2, 3):bar1(args1)(z)(y){4, 5, 6, }:bar2(args2).test = i+1, 20");
        RuleReturnScope r = p.block();

        CommonTree tree = (CommonTree)r.getTree();

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        LuaWalker tp = new LuaWalker(nodes);
        tp.downup(tree);
    }

    private LuaParser getParser(String src) throws IOException
    {
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(src.getBytes()));
        LuaLexer lexer = new LuaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new LuaParser(tokens);
    }
}
