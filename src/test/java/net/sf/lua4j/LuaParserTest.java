/**
 *
 * Copyright 2009-2010 (C) The original author or authors
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
package net.sf.lua4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.junit.Test;


/**
 * @version $Revision: $ $Date: $
 */
public class LuaParserTest
{
    @Test
    public void test() throws Exception
    {
        LuaParser.namelist_return list = getParser("abc").namelist();
        print(list = getParser("abc,def").namelist());
        print(list = getParser("abc,  def").namelist());
        print(list = getParser("abc --[=[ roo ]=], def-- simple comment\n").namelist());

        LuaParser.string_return sting = print(getParser("[===[\nte]==]s]====]t]===]").string());

        LuaParser.number_return number = print(getParser("3").number());
        print(number = getParser("3.0").number());
        print(number = getParser("3.1416").number());
        print(number = getParser("314.16e-2").number());
        print(number = getParser("0.31416E1").number());
        print(number = getParser("0xff").number());
        print(number = getParser("0x56").number());

        LuaParser.stat_return stat = print(getParser("i, a[i] = i+1, 20").stat());

        LuaParser.exp_return exp = print(getParser("i+1").exp());
        exp = print(getParser("-i+1").exp());
        print(exp = getParser("(-i)+1").exp());
        print(exp = getParser("-(i+1)").exp());

        LuaParser.chunk_return chunk = print(getParser("x = -i+1").chunk());
        print(chunk = getParser("abc --[=[ roo ]=]= def-- simple comment\n").chunk());
        print(chunk = getParser("abc --[=[ roo ]=], dog= def,cat-- simple comment\n").chunk());

        print(chunk = getParser("a = 1 b = 2").chunk());
        print(chunk = getParser("a, b = b, a b = 2").chunk());
        print(chunk = getParser("a = 1 b =\n2").chunk());
        print(chunk = getParser("i, a:foo(1, 2, 3)[i] = i+1, 20").chunk());
        print(chunk = getParser("i, a:foo{1,2,3}[i] = i+1, 20").chunk());
        print(chunk = getParser("i, v:car(a, b, c):cdr(1, 2, 3):bar1(args1)(z)(y){4, 5, 6, }:bar2(args2).test = i+1, 20").chunk());
        print(getParser("foo or bar").exp());
        print(getParser("if foo or bar then a = 1 elseif car then a = 2 elseif cdr then a = 3 else a = 4 end").chunk());

        print(getParser("foo.b.c.d:e").funcname());
    }

    private <T extends RuleReturnScope> T print(T scope)
    {
        CommonTree t = (CommonTree)scope.getTree();
        System.out.println(t.toStringTree());

        return scope;
    }

    private LuaParser getParser(String src) throws IOException
    {
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(src.getBytes()));
        LuaLexer lexer = new LuaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new LuaParser(tokens);
    }

    private LuaParser getParser(File src) throws Exception
    {
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(src));
        LuaLexer lexer = new LuaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new LuaParser(tokens);
    }
}
