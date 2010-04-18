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
package com.toolazydogs.lua4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.toolazydogs.aunit.AntlrTestRunner;
import static com.toolazydogs.aunit.Assert.assertToken;
import static com.toolazydogs.aunit.Assert.assertTree;
import com.toolazydogs.aunit.Configuration;
import static com.toolazydogs.aunit.CoreOptions.lexer;
import static com.toolazydogs.aunit.CoreOptions.options;
import static com.toolazydogs.aunit.CoreOptions.parser;
import com.toolazydogs.aunit.Option;
import static com.toolazydogs.aunit.Work.parse;
import static com.toolazydogs.aunit.Work.scan;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RuleReturnScope;
import org.antlr.runtime.tree.CommonTree;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @version $Revision: $ $Date: $
 */
@RunWith(AntlrTestRunner.class)
public class LuaParserTest
{
    @Configuration
    public static Option[] configure()
    {
        return options(
                lexer(LuaLexer.class).failOnError(),
                parser(LuaParser.class).failOnError()
        );
    }

    @Test
    public void test() throws Exception
    {
        assertToken(LuaLexer.NAME, "abc", scan("abc"));

        assertTree(LuaParser.NAMELIST, "(NAMELIST abc )", parse("abc", "namelist"));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc,def", "namelist"));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc,  def", "namelist"));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc --[=[ roo ]=], def-- simple comment\n", "namelist"));

        assertTree(LuaParser.STRING, "(STRING '[===[\nte]==]s]====]t]===]')", parse("[===[\nte]==]s]====]t]===]", "string"));

        assertTree(LuaParser.INTEGER, "3", parse("3", "number"));
        assertTree(LuaParser.FLOAT, "3.0", parse("3.0", "number"));
        assertTree(LuaParser.FLOAT, "3.1416", parse("3.1416", "number"));
        assertTree(LuaParser.EXPONENT, "314.16e-2", parse("314.16e-2", "number"));
        assertTree(LuaParser.EXPONENT, "0.31416E1", parse("0.31416E1", "number"));
        assertTree(LuaParser.HEX, "0xff", parse("0xff", "number"));
        assertTree(LuaParser.HEX, "0x56", parse("0x56", "number"));

        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i) (VAR a (DEREF (VAR i)))) (EXPLIST (+ (VAR i) 1) 20))", parse("i, a[i] = i+1, 20", "stat"));
    }

    @Test
    public void testOld() throws Exception
    {
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

        print(getParser("#@foo.b.c.d:e").funcname());
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
