/**
 *
 * Copyright 2009-2010 (C) Alan D. Cabrera
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

import com.toolazydogs.aunit.AntlrTestRunner;
import static com.toolazydogs.aunit.Assert.assertToken;
import static com.toolazydogs.aunit.Assert.assertTree;
import com.toolazydogs.aunit.Configuration;
import static com.toolazydogs.aunit.CoreOptions.lexer;
import static com.toolazydogs.aunit.CoreOptions.options;
import static com.toolazydogs.aunit.CoreOptions.parser;
import com.toolazydogs.aunit.Option;
import com.toolazydogs.aunit.Work;
import static com.toolazydogs.aunit.Work.parse;
import static com.toolazydogs.aunit.Work.rule;
import static com.toolazydogs.aunit.Work.scan;
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
    public void testString() throws Exception
    {
        assertToken(LuaLexer.NORMAL_STRING, "abc", scan("\"abc\""));
        assertToken(LuaLexer.CHAR_STRING, "abc", scan("'abc'"));

        assertToken(LuaLexer.NORMAL_STRING, "alo", scan("\"\\97lo\""));
        assertToken(LuaLexer.CHAR_STRING, "alo", scan("'\\97lo'"));

        assertToken(LuaLexer.NORMAL_STRING, "Zalo", scan("\"Z\\97lo\""));
        assertToken(LuaLexer.CHAR_STRING, "Zalo", scan("'Z\\97lo'"));

        assertToken(LuaLexer.NORMAL_STRING, "alo\n123\"", scan("\"alo\\n123\\\"\""));
        assertToken(LuaLexer.CHAR_STRING, "alo\n123\"", scan("'alo\\n123\"'"));
        assertToken(LuaLexer.LONG_STRING, "alo\n123\"", scan("[[alo\n123\"]]"));
        assertToken(LuaLexer.LONG_STRING, "alo\n123\"", scan("[==[alo\n123\"]==]"));

        assertTree(LuaParser.STRING, "(STRING 'alo\n123\"')", parse("\"alo\\n123\\\"\"", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'alo\n123\"')", parse("'alo\\n123\"'", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'alo\n123\"')", parse("[[alo\n123\"]]", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'alo\n123\"')", parse("[==[alo\n123\"]==]", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'alo\n123\"')", parse("[==[\nalo\n123\"]==]", rule("string")));

        assertTree(LuaParser.STRING, "(STRING 'te]==]s]====]t')", parse("[===[\nte]==]s]====]t]===]", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'foo')", parse("\"foo\"", rule("string")));
        assertTree(LuaParser.STRING, "(STRING 'foo')", parse("'foo'", rule("string")));
    }

    @Test
    public void testNumbers() throws Exception
    {
        assertTree(LuaParser.INTEGER, "3", parse("3", rule("number")));
        assertTree(LuaParser.FLOAT, "3.0", parse("3.0", rule("number")));
        assertTree(LuaParser.FLOAT, "3.1416", parse("3.1416", rule("number")));
        assertTree(LuaParser.EXPONENT, "314.16e-2", parse("314.16e-2", rule("number")));
        assertTree(LuaParser.EXPONENT, "0.31416E1", parse("0.31416E1", rule("number")));
        assertTree(LuaParser.HEX, "0xff", parse("0xff", rule("number")));
        assertTree(LuaParser.HEX, "0x56", parse("0x56", rule("number")));
    }

    @Test
    public void testExpressions() throws Exception
    {
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (+ (VAR i) 1)))", parse("i=i+1", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (+ (NEGATE (VAR i)) 1)))", parse("i=-i+1", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (+ (NEGATE (SINGLE (NEGATE (VAR i)))) 1)))", parse("i=-(-i)+1", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (NEGATE (SINGLE (+ (VAR i) 1)))))", parse("i=-(i+1)", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (+ 1 (NEGATE (VAR z)))))", parse("i=1+ -z", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (^ 2 2)))", parse("i=2^2", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (or (not (VAR b)) (> (VAR c) (VAR d)))))", parse("i=not b or c > d", rule("stat")));
        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i)) (EXPLIST (+ (- (/ 1 2) (% (NEGATE 2) 3)) (^ (VAR y) 314.16e-2))))", parse("i=1/2 - -2%3 + y^314.16e-2", rule("stat")));
    }

    @Test
    public void testFunctionCalls() throws Exception
    {
        print(Work.<LuaParser>generateParser("f(x,y,z)").chunk());
        print(Work.<LuaParser>generateParser("e[i]()").chunk());
        print(Work.<LuaParser>generateParser("e[i]():f(x,y,z):g(x,y,z)").chunk());
    }

    @Test
    public void test() throws Exception
    {
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc )", parse("abc", rule("namelist")));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc,def", rule("namelist")));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc,  def", rule("namelist")));
        assertTree(LuaParser.NAMELIST, "(NAMELIST abc def)", parse("abc --[=[ roo ]=], def-- simple comment\n", rule("namelist")));

        assertTree(LuaParser.ASSIGN, "(ASSIGN (VARLIST (VAR i) (VAR a (DEREF (VAR i)))) (EXPLIST (+ (VAR i) 1) 20))", parse("i, a[i] = i+1, 20", rule("stat")));

        assertTree(LuaParser.FNAMETHIS, "(FNAMETHIS foo b c d e)", parse("@foo.b.c.d:e", rule("funcname")));

        print(Work.<LuaParser>generateParser("do\n" +
                                             "  local var, limit, step = tonumber(e1), tonumber(e2), tonumber(e3)\n" +
                                             "  if not (var and limit and step) then error() end\n" +
                                             "  while (step > 0 and var <= limit) or (step <= 0 and var >= limit) do\n" +
                                             "    local v = var\n" +
                                             "    print(v)\n" +
                                             "    var = var + step\n" +
                                             "  end\n" +
                                             "end\n" +
                                             "i = i + 1").chunk());

        print(Work.<LuaParser>generateParser("x = 10                -- global variable\n" +
                                             "do                    -- new block\n" +
                                             "  local x = x         -- new 'x', with value 10\n" +
                                             "  print(x)            --> 10\n" +
                                             "  x = x+1\n" +
                                             "  do                  -- another block\n" +
                                             "    local x = x+1     -- another 'x'\n" +
                                             "    print(x)          --> 12\n" +
                                             "  end\n" +
                                             "  print(x)            --> 11\n" +
                                             "end\n" +
                                             "print(x)              --> 10  (the global one)\n").chunk());

        print(Work.<LuaParser>generateParser("a = {}\n" +
                                             "local x = 20\n" +
                                             "for i=1,10 do\n" +
                                             "  local y = 0\n" +
                                             "  a[i] = function () y=y+1; return x+y end\n" +
                                             "end").chunk());

        print(Work.<LuaParser>generateParser("do\n" +
                                             "       local var, limit, step = tonumber(e1), tonumber(e2), tonumber(e3)\n" +
                                             "       if not (var and limit and step) then error() end\n" +
                                             "       while (step > 0 and var <= limit) or (step <= 0 and var >= limit) do\n" +
                                             "         local v = var\n" +
                                             "         print(v)\n" +
                                             "         var = var + step\n" +
                                             "       end\n" +
                                             "     end").chunk());

        print(Work.<LuaParser>generateParser("i, a[i] = i+1, (f(x,y,z))").chunk());

        print(Work.<LuaParser>generateParser("do\n" +
                         "       local var, limit, step = tonumber(e1), tonumber(e2), tonumber(e3)\n" +
                         "       if not (var and limit and step) then error() end\n" +
                         "       while (step > 0 and var <= limit) or (step <= 0 and var >= limit) do\n" +
                         "         local v = var\n" +
                         "         print(v)\n" +
                         "         var = var + step\n" +
                         "       end\n" +
                         "     end").chunk());

        assertTree(LuaParser.CHUNK,
                   "(CHUNK (STATEMENTS (BLOCK (CHUNK (STATEMENTS (LOCAL (NAMELIST var limit step) (EXPLIST (VAR tonumber) (ARGS (EXPLIST (VAR e1))) (VAR tonumber) (ARGS (EXPLIST (VAR e2))) (VAR tonumber) (ARGS (EXPLIST (VAR e3))))) (IF (not (SINGLE (and (VAR var) (VAR limit) (VAR step)))) (BLOCK (CHUNK (STATEMENTS (FUNCALL (VAR error) (ARGS EXPLIST)))))) (WHILE (or (SINGLE (and (> (VAR step) 0) (<= (VAR var) (VAR limit)))) (SINGLE (and (<= (VAR step) 0) (>= (VAR var) (VAR limit))))) (BLOCK (CHUNK (STATEMENTS (LOCAL (NAMELIST v) (EXPLIST (VAR var))) (FUNCALL (VAR print) (ARGS (EXPLIST (VAR v)))) (ASSIGN (VARLIST (VAR var)) (EXPLIST (+ (VAR var) (VAR step)))))))))))))",
                   parse("do\n" +
                         "       local var, limit, step = tonumber(e1), tonumber(e2), tonumber(e3)\n" +
                         "       if not (var and limit and step) then error() end\n" +
                         "       while (step > 0 and var <= limit) or (step <= 0 and var >= limit) do\n" +
                         "         local v = var\n" +
                         "         print(v)\n" +
                         "         var = var + step\n" +
                         "       end\n" +
                         "     end", rule("chunk")));

        print(Work.<LuaParser>generateParser("line = \"Hello world!\", f() print(line)").chunk());

        assertTree(LuaParser.CHUNK,
                   "(CHUNK (STATEMENTS (ASSIGN (VARLIST (VAR line)) (EXPLIST (STRING 'Hello world!'))) (FUNCALL (VAR print) (ARGS (EXPLIST (VAR line))))))",
                   parse("line = \"Hello world!\"; print(line)", rule("chunk")));

    }

    @Test
    public void testOld() throws Exception
    {
        print(Work.<LuaParser>generateParser("i, a:foo(1, 2, 3)[i] = i+1, 20").chunk());
        print(Work.<LuaParser>generateParser("i, a:foo{1,2,3}[i] = i+1, 20").chunk());
        print(Work.<LuaParser>generateParser("i, v:car(a, b, c):cdr(1, 2, 3):bar1(args1)(z)(y){4, 5, 6, }:bar2(args2).test = i+1, 20").chunk());
        print(Work.<LuaParser>generateParser("foo or bar").exp());
        print(Work.<LuaParser>generateParser("if foo or bar then a = 1 elseif car then a = 2 elseif cdr then a = 3 else a = 4 end").chunk());

        print(Work.<LuaParser>generateParser("function t.a.b.c:f (params) i= 1 end").chunk());
    }

    private static <T extends RuleReturnScope> T print(T scope)
    {
        CommonTree t = (CommonTree)scope.getTree();
        System.out.println(t.toStringTree());

        return scope;
    }
}
