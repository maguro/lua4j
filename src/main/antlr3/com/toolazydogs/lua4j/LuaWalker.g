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

topdown 
        : enterArgs 
        | enterAssign 
        | enterBlock 
        | enterChunk 
        | enterDeref 
        | enterExplist 
        | enterFuncall 
        | enterS 
        | enterStatement 
        | enterString 
        | enterVar 
        | enterVarlist
        ;

bottomup 
         : exitArgs 
         | exitAssign 
         | exitBlock 
         | exitChunk 
         | exitDeref 
         | exitExplist 
         | exitFuncall 
         | exitStatement 
         | exitString 
         | exitVar 
         | exitVarlist
         ;

enterArgs
	: ARGS { println("enter args"); }
	;

exitArgs
	: ARGS { println("exit args"); }
	;

enterAssign
	: ASSIGN { println("enter assign"); }
	;

exitAssign
	: ASSIGN { 
		println("exit assign");
		println("\tadd code to copy expressions to var list"); 
	}
	;

enterBlock
	: BLOCK { println("enter block"); }
	;

exitBlock
	: BLOCK { println("exit block"); }
	;

enterChunk
	: CHUNK { 
		println("enter chunk"); 
		println("\tadd code to push scope on the scope stack"); 
	}
	;

exitChunk
	: CHUNK {
		println("exit chunk");
		println("\tadd code to pop scope on the scope stack"); 
	}
	;

enterDeref
	: DEREF { println("enter deref"); }
	;

exitDeref
	: DEREF { println("exit deref"); }
	;

enterExplist
	: EXPLIST { println("enter explist"); }
	;

exitExplist
	: EXPLIST { println("exit explist"); }
	;

enterFuncall
	: FUNCALL { println("enter funcall"); }
	;

exitFuncall
	: FUNCALL { println("exit funcall"); }
	;

enterStatement
	: STATEMENTS { println("enter statement"); }
	;

exitStatement
	: STATEMENTS { println("exit statement"); }
	;

enterString
	: ^(STRING s=.) { println("enter string '" + $s.getText() + "'"); }
	;

exitString
	: ^(STRING s=.) { println("exit string '" + $s.getText() + "'"); }
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
