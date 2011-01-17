/**
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
tree grammar LuaWalker;
options
{
    tokenVocab=Lua;
    ASTLabelType=CommonTree;
    filter = true;
}
@header
{
package com.toolazydogs.lua4j;
}
@members
{ 
    void print(String s) { System.out.print(s); }
    void println(String s) { System.out.println(s); }
}

topdown : enterAssign | enterBlock | enterChunk | enterDeref | enterS | enterStatement | enterVar | enterVarlist;
bottomup : exitAssign | exitBlock | exitChunk | exitDeref | exitStatement | exitVar | exitVarlist;

enterAssign
	: ASSIGN { println("enter assign"); }
	;

exitAssign
	: ASSIGN { println("exit assign"); }
	;

enterBlock
	: BLOCK { println("enter block"); }
	;

exitBlock
	: BLOCK { println("exit block"); }
	;

enterChunk
	: CHUNK { println("enter chunk"); }
	;

exitChunk
	: CHUNK { println("exit chunk"); }
	;

enterDeref
	: DEREF { println("enter deref"); }
	;

exitDeref
	: DEREF { println("exit deref"); }
	;

enterStatement
	: STATEMENTS { println("enter statement"); }
	;

exitStatement
	: STATEMENTS { println("exit statement"); }
	;

enterS
	: ^(VAR v=NAME ^(DEREF (args+=.)+)) { println("enter deref " + $v.text + " args " +$args.size()); }
	;
	
enterVar
	: ^(VAR v=NAME) { println("enter var '" + $v.text + "'" ); }
	;

exitVar
	: ^(VAR v=NAME) { println("exit var '" + $v.text + "'" ); }
	;

enterVarlist
	: VARLIST { println("enter varlist"); }
	;

exitVarlist
	: VARLIST { println("exit varlist"); }
	;
