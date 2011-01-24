/**
 * Copyright 2009-2011 (C) Alan D. Cabrera
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
grammar Lua;
options
{
    output=AST;
    backtrack=true;
}

tokens {
    ARGS;
    ARGSWITHSELF;
    ASSIGN;
    BLOCK;
    BREAK;
    CHUNK;
    ELSEIF;
    EXPLIST;
    DEREF;
    FOR;
    FORIN;
    FNAME;
    FNAMETHIS;
    FUNCALL;
    FUNCTION;
    IF;
    LOCAL;
    NAMELIST;
    NEGATE;
    NUMBER;
    PARAMETERS;
    PATH;
    REPEAT;
    RETURN;
    SINGLE;
    STATEMENTS;
    STRING;
    TBLCTOR;
    TBLIDX;
    TBLREF;
    VAR;
    VARLIST;
    WHILE;
}

@header
{
package com.toolazydogs.lua4j;
}
@lexer::header
{
/**
 * Copyright 2009-2011 (C) Alan D. Cabrera
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
}
@lexer::members
{
protected boolean isLongBracketOpen(int length)
{
    if (input.LA(1) != ']') return true;

    for (int i = 0; i != length; ++i)
    {
        if (input.LA(i + 2) != '=') return true;
    }

    return (input.LA(length + 2) != ']');
}

protected void matchLongBracketClose(int length) throws MismatchedTokenException
{
    StringBuilder builder = new StringBuilder();

    builder.append(']');
    for (int i = 0; i != length; ++i) builder.append('=');
    builder.append(']');

    match(builder.toString());
}

protected String toAscii(String... d) throws RecognitionException
{
    StringBuilder sb = new StringBuilder(d[0]);
    for (int i=1; i<d.length; i++) sb.append(d[i]);
        
    byte[] b = new byte[1];

    b[0] = (byte)Integer.parseInt(sb.toString());

    try
    {
        return new String(b, "ASCII");
    }
    catch (UnsupportedEncodingException e)
    {
        throw new RecognitionException();
    }
}
}

chunk
    : (stat ';'? -> ^(CHUNK ^(STATEMENTS stat+)))* (laststat ';'?-> ^(CHUNK ^(STATEMENTS stat+ laststat)))?
    ;

block
    : chunk -> ^(BLOCK chunk)
    ;

stat
    : varlist '=' explist -> ^(ASSIGN varlist explist)
    | functioncall
    | 'do' block 'end' -> block
    | 'while' exp 'do' block 'end' -> ^(WHILE exp block)
    | 'repeat' block 'until' exp -> ^(REPEAT block exp)
    | 'if' exp 'then' block 'end' -> ^(IF exp block)
    | 'if' exp 'then' ifblock=block 'else' elseblock=block 'end' -> ^(IF exp $ifblock $elseblock)
    | 'if' exp 'then' ifblock=block elseif+ 'end' -> ^(IF exp $ifblock elseif+)
    | 'if' exp 'then' ifblock=block elseif+ 'else' elseblock=block 'end' -> ^(IF exp $ifblock elseif+ $elseblock)
    | 'for' NAME '=' exp1=exp ',' exp2=exp ',' exp3=exp 'do' block 'end' -> ^(FOR NAME $exp1 $exp2 $exp3 block)
    | 'for' NAME '=' exp1=exp ',' exp2=exp 'do' block 'end' -> ^(FOR NAME $exp1 $exp2 block)
    | 'for' namelist 'in' explist 'do' block 'end' -> ^(FORIN namelist explist block)
    | 'function' funcname funcbody -> ^(FUNCTION funcname funcbody)
    | 'local' namelist ('=' explist)? -> ^(LOCAL namelist explist?)
    | 'local' 'function' NAME funcbody
    ;

elseif
    : ('elseif' exp 'then' block)+ -> ^(ELSEIF exp block)+
    ;

laststat
    : 'return' -> ^(RETURN)
    | 'return' explist -> ^(RETURN explist)
    | 'break' -> BREAK
    ;

funcname
@init{boolean hasThis = false;}
    : f=NAME ('.' p+=NAME)* (':' t=NAME {hasThis = true;})?
        -> {hasThis}? ^(FNAMETHIS $f $p* $t)
        -> ^(FNAME $f $p*)
    ;

varlist
    : var (',' var)* -> ^(VARLIST var+)
    ;

var
    : NAME varEnd+ -> ^(VAR NAME varEnd+)
    | NAME -> ^(VAR NAME)
    | '(' exp ')' varEnd+ -> ^(VAR '(' exp ')' varEnd+)
    ;

varEnd
    : nameAndArgs* '[' exp ']' -> ^(DEREF nameAndArgs* exp)
    | nameAndArgs* '.' NAME -> ^(DEREF nameAndArgs* NAME)
    ;

namelist
    : NAME (',' NAME)* -> ^(NAMELIST NAME+)
    ;

explist
    : exp (',' exp)* -> ^(EXPLIST exp+)
    ;

exp
    : or ('or' or)+ -> ^('or' or+)
    | or
    ;

or
    : and ('and' and)+ -> ^('and' and+)
    | and
    ;

and
    : compare (compare_op^ compare)*
    ;

compare
    : concatenation ('..' concatenation)+ -> ^('..' concatenation+)
    | concatenation
    ;

concatenation
    : add_sub (add_sub_op^ add_sub)*
    ;
	
add_sub
    : b (b_op^ b)*
    ;

b
    : unary_op^ unary
    | unary
    ;

unary : atom ('^'^ atom)* ;

atom 	: 'nil'
        | 'false'
        | 'true'
        | number
        | string
        | function
        | prefixexp
        | tableconstructor
        | '...'
	;

unary_op : 'not' | '#' | '-' -> NEGATE ;	

b_op : '*' | '/' | '%' ;
	 		
compare_op : '<' | '<=' | '>' | '>=' | '==' | '~=' ;
	
add_sub_op : '+' | '-' ;

prefixexp
    : varOrExp nameAndArgs*
    ;

functioncall
    : varOrExp nameAndArgs+ -> ^(FUNCALL varOrExp nameAndArgs+)
    ;

varOrExp
    : var
    | '(' exp ')' -> ^(SINGLE exp)
    ;

nameAndArgs
    : args -> ^(ARGS args)
    | ':' NAME args -> ^(ARGSWITHSELF NAME args)
    ;

args
    : '(' ')' -> ^(EXPLIST)
    | '(' explist ')' -> explist
    | tableconstructor
    | string
    ;

function
    : 'function' funcbody
    ;

funcbody
    : '(' parlist? ')' block 'end'
	;

parlist
    : namelist -> ^(PARAMETERS namelist)
    | namelist ',' '...' -> ^(PARAMETERS namelist '...')
    | '...' -> ^(PARAMETERS '...')
    ;

tableconstructor
    : '{' fieldlist? '}' -> ^(TBLCTOR fieldlist?)
    ;

fieldlist
    : field (fieldsep! field)* fieldsep!?
    ;

field
    : '[' exp ']' '=' exp
    | NAME '=' exp
    | exp
    ;

fieldsep
    : ','
    | ';'
    ;

NAME
	: ('a'..'z' | 'A'..'Z' | '_') ('a'..'z' | 'A'..'Z' | '_' | '0'..'9')*
	;

number
    : INTEGER<IntNode>
    | FLOAT<FloatNode>
    | EXPONENT<FloatNode>
    | HEX<IntNode>
    ;

INTEGER
    : DIGIT+
    ;

FLOAT
    : DIGIT+ '.' DIGIT+
    ;

EXPONENT
    : (INTEGER | FLOAT) ('e' | 'E') '-'? INTEGER
    ;

HEX
    : '0x' ('0'..'9' | 'a'..'f')+
    ;

fragment
DIGIT : ('0'..'9') ;

string
    : s=NORMAL_STRING -> ^(STRING $s)
    | s=CHAR_STRING -> ^(STRING $s)
    | s=LONG_STRING -> ^(STRING $s)
    ;

NORMAL_STRING         
@init{StringBuilder sb = new StringBuilder();}
    :   
        '"' 
        ( escaped=ESCAPE_SEQUENCE { sb.append(getText()); } | 
          normal=~('"' | '\\')    { sb.appendCodePoint(normal); } )* 
        '"'     
        { setText(sb.toString()); }
    ;

CHAR_STRING         
@init{ StringBuilder sb = new StringBuilder(); }
    :   
        '\'' 
        ( escaped=ESCAPE_SEQUENCE { sb.append(getText()); } | 
             normal=~('\'' | '\\')    { sb.appendCodePoint(normal); } )* 
        '\''     
        { setText(sb.toString()); }
    ;

LONG_STRING
    : LONG_BRACKET
    ;

fragment
ESCAPE_SEQUENCE
    : '\\' 
    	( 'a'  { setText("\0007"); }
    	| 'b'  { setText("\b"); }
    	| 'f'  { setText("\f"); }
    	| 'n'  { setText("\n"); }
    	| 'r'  { setText("\r"); }
    	| 't'  { setText("\t"); }
    	| 'v'  { setText("\013"); }
    	| '"'  { setText("\""); }
    	| '\'' { setText("\'"); }
    	| '\\' { setText("\\"); }
    	)
    | ASCII_ESCAPE
    ;

fragment
ASCII_ESCAPE
    : '\\' d1=DIGIT d2=DIGIT d3=DIGIT { setText(toAscii(d1.getText(), d2.getText(), d3.getText())); }
    | '\\' d1=DIGIT d2=DIGIT { setText(toAscii(d1.getText(), d2.getText())); }
    | '\\' d1=DIGIT { setText(toAscii(d1.getText())); }
    ;

LONG_COMMENT
    : '--' LONG_BRACKET { skip(); }
    ;

fragment
LONG_BRACKET
@init { int n = 0; }
    : ('['('=' {++n;})*'[') ({isLongBracketOpen(n)}? => .)* 
    { 
        matchLongBracketClose(n);
        String text = getText().substring(n+2, getText().length()-(n+2));
        if (text.charAt(0) == '\n') text = text.substring(1, text.length());
        setText(text);
    }
    ;

LINE_COMMENT
    : '--' ~('\n' | '\r')* '\r'? '\n' { skip(); }
    ;

WS :  (' ' | '\t' | '\u000C') { skip(); } ;

NEWLINE : ('\r')? '\n' { skip(); } ;
