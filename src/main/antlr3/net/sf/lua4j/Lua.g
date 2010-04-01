/**
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
grammar Lua;
options
{
    output=AST;
    backtrack=true;
}

tokens {
    ARGS;
    ARGSWITHSELF;
    BLOCK;
    BREAK;
    ELSEIF;
    EXPLIST;
    FOR;
    FORIN;
    FNAME;
    FNAMETHIS;
    FUNCALL;
    FUNCTION;
    IF;
    LOCAL;
    NAMELIST;
    PARAMETERS;
    PATH;
    REPEAT;
    RETURN;
    STATEMENT;
    TBLCTOR;
    TBLIDX;
    TBLREF;
    VAR;
    VARLIST;
    WHILE;
}

@header
{
package net.sf.lua4j;
}
@lexer::header
{
/**
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
}

chunk
    : (stat ';'? -> ^(STATEMENT stat))* (laststat ';'?-> ^(STATEMENT laststat))?
    ;

block
    : chunk -> ^(BLOCK chunk)
    ;

stat
    : varlist '='^ explist
    | functioncall
    | 'do' block 'end' -> block
    | 'while' exp 'do' block 'end' -> ^(WHILE exp block)
    | 'repeat' block 'until' exp -> ^(REPEAT block exp)
    | 'if' exp 'then' ifblock=block 'end' -> ^(IF exp block)
    | 'if' exp 'then' ifblock=block 'else' elseblock=block 'end' -> ^(IF exp $ifblock $elseblock)
    | 'if' exp 'then' ifblock=block elseif+ 'end' -> ^(IF exp $ifblock elseif+)
    | 'if' exp 'then' ifblock=block elseif+ 'else' elseblock=block 'end' -> ^(IF exp $ifblock elseif+ $elseblock)
    | 'for' NAME '=' exp1=exp ',' exp2=exp 'do' block 'end' -> ^(FOR NAME $exp1 $exp2 block)
    | 'for' NAME '=' exp1=exp ',' exp2=exp ',' exp3=exp 'do' block 'end' -> ^(FOR NAME $exp1 $exp2 $exp3 block)
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
    : NAME varEnd* -> ^(VAR NAME varEnd*)
    | '(' exp ')' varEnd+ -> ^(VAR '(' exp ')' varEnd+)
    ;

varEnd
    : nameAndArgs* '[' exp ']'
    | nameAndArgs* '.' NAME
    ;

namelist
    : NAME (',' NAME)* -> ^(NAMELIST NAME+)
    ;

explist
    : exp (',' exp)* -> ^(EXPLIST exp+)
    ;

exp
    : ('nil' | 'false' | 'true'
        | number
        | string
        | function
        | prefixexp
        | tableconstructor
        | '...'
        | unopExp
    )
    (binop^ exp)*
    ;

unopExp
    : unop^ ('nil' | 'false' | 'true'
        | number
        | string
        | function
        | prefixexp
        | tableconstructor
        | '...'
    )
    ;

prefixexp
    : varOrExp nameAndArgs*
    ;

functioncall
    : varOrExp nameAndArgs+ -> ^(FUNCALL varOrExp nameAndArgs+)
    ;

varOrExp
    : var
    | '(' exp ')' -> exp
    ;

nameAndArgs
    : args -> ^(ARGS args)
    | ':' NAME args -> ^(ARGSWITHSELF NAME args)
    ;

args
    : '(' explist? ')' -> explist
    | tableconstructor
    | string
    ;

function
    : 'function' funcbody
    ;

funcbody
    : '(' parlist+ ')' block 'end'
	;

parlist
    : namelist -> ^(PARAMETERS namelist)
    | namelist ',' '...' -> ^(PARAMETERS namelist '...')
    | '...' -> ^(PARAMETERS '...')
    ;

tableconstructor
    : '{' fieldlist? '}' -> ^(TBLCTOR fieldlist)
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

binop
    : '+'
    | '-'
    | '*'
    | '/'
    | '^'
    | '%'
    | '..'
    | '<'
    | '<='
    | '>'
    | '>='
    | '=='
    | '~='
    | 'and'
    | 'or'
    ;

unop
    : '-'
    | 'not'
    | '#'
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
DIGIT
	: ('0'..'9')
	;

string
    : NORMAL_STRING
    | CHAR_STRING
    | LONG_STRING
    ;

NORMAL_STRING
    :  '"' ( ESCAPE_SEQUENCE | ~('\\'|'"') )* '"'
    ;

CHAR_STRING
   :	'\'' ( ESCAPE_SEQUENCE | ~('\''|'\\') )* '\''
   ;

LONG_STRING
	: LONG_BRACKET
    ;

fragment
ESCAPE_SEQUENCE
    :   '\\' ('b' | 't' | 'n' | 'f' | 'r' | '\"' | '\'' | '\\')
    |   OCTAL_ESCAPE
    |   UNICODE_ESCAPE
    ;

fragment
OCTAL_ESCAPE
    :   '\\' ('0'..'3') ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7') ('0'..'7')
    |   '\\' ('0'..'7')
    ;

fragment
UNICODE_ESCAPE
    :   '\\' 'u' HEX_DIGIT HEX_DIGIT HEX_DIGIT HEX_DIGIT
    ;

fragment
HEX_DIGIT
	: ('0'..'9' | 'a'..'f' | 'A'..'F')
	;

LONG_COMMENT
    : '--' LONG_BRACKET { skip(); }
	;

fragment
LONG_BRACKET
@init { int n = 0; }
 	: ('['('=' {++n;})*'[') ({isLongBracketOpen(n)}? => .)* { matchLongBracketClose(n); }
    ;

LINE_COMMENT
	: '--' ~('\n' | '\r')* '\r'? '\n' { skip(); }
	;

WS
	:  (' ' | '\t' | '\u000C') { skip(); }
    ;

NEWLINE
	: ('\r')? '\n' { skip(); }
	;
