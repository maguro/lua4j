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
        list = getParser("abc,def").namelist();
        list = getParser("abc,  def").namelist();
        list = getParser("abc --[=[ roo ]=], def-- simple comment\n").namelist();

        LuaParser.string_return sting = getParser("[===[\nte]==]s]====]t]===]").string();

        LuaParser.number_return number = getParser("3").number();
        number = getParser("3.0").number();
        number = getParser("3.1416").number();
        number = getParser("314.16e-2").number();
        number = getParser("0.31416E1").number();
        number = getParser("0xff").number();
        number = getParser("0x56").number();

        LuaParser.stat_return stat = getParser("i, a[i] = i+1, 20").stat();

        LuaParser.exp_return exp = getParser("i+1").exp();
        exp = getParser("-i+1").exp();
        exp = getParser("(-i)+1").exp();
        exp = getParser("-(i+1)").exp();

        LuaParser.chunk_return chunk = getParser("x = -i+1").chunk();
        chunk = getParser("abc --[=[ roo ]=]= def-- simple comment\n").chunk();

        chunk = getParser("a = 1 b = 2").chunk();
        chunk = getParser("a = 1 b =\n2").chunk();
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

    public static void main(String... args) throws Exception
    {
        ANTLRInputStream input = new ANTLRInputStream(System.in);
        LuaLexer lexer = new LuaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LuaParser parser = new LuaParser(tokens);
        LuaParser.chunk_return chunk = parser.chunk();
        int i = 0;
    }
}
