/**
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
tree grammar LuaWalker;
options
{
    tokenVocab=Lua;
    ASTLabelType=CommonTree;
    filter = true;
}
@header
{
package net.sf.lua4j;
}
@members
{ 
    void print(String s) { System.out.print(s); }
    void println(String s) { System.out.println(s); }
}

topdown : enterBlock | enterStatement ;
bottomup : exitBlock ;

enterBlock
	: BLOCK { println("enter"); }
	;

exitBlock
	: BLOCK { println("exit"); }
	;


enterStatement
	: STATEMENT { println("statement"); }
	;
