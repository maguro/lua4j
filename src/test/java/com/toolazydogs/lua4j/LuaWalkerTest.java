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

import java.io.UnsupportedEncodingException;

import com.toolazydogs.aunit.AntlrTestRunner;
import com.toolazydogs.aunit.Configuration;
import static com.toolazydogs.aunit.CoreOptions.lexer;
import static com.toolazydogs.aunit.CoreOptions.options;
import static com.toolazydogs.aunit.CoreOptions.parser;
import com.toolazydogs.aunit.Option;
import com.toolazydogs.aunit.Work;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @version $Revision: $ $Date: $
 */
@RunWith(AntlrTestRunner.class)
public class LuaWalkerTest
{
    @Configuration
    public static Option[] configure()
    {
        return options(
                lexer(LuaLexer.class),
                parser(LuaParser.class)
        );
    }

    @Test
    public void test() throws Exception
    {
        LuaParser p = Work.generateParser("i, v:car(a, b, c):cdr(1, 2, 3):bar(args1)'str':foo'str2'(z)(y){4, 5, 6, }:bar2(args2).test = i+1, 20");

        RuleReturnScope r = print(p.chunk());

        CommonTree tree = (CommonTree)r.getTree();

        CommonTreeNodeStream nodes = new CommonTreeNodeStream(tree);
        LuaWalker tp = new LuaWalker(nodes);
        tp.downup(tree);
    }

    private static <T extends RuleReturnScope> T print(T scope)
    {
        CommonTree t = (CommonTree)scope.getTree();
        System.out.println(t.toStringTree());

        return scope;
    }
}
