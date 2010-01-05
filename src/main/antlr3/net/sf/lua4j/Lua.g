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
	EXPLIST;
	NAMELIST;
	PARAMETERS;
	PAREN;
	TABLEREF;
	VARLIST;
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
    : (stat ';'?)* (laststat ';'?)?
    ;

block
    : chunk
    ;

stat
    : varlist '='^ explist
	| functioncall
    | 'do' block 'end'
    | 'while' exp 'do' block 'end'
	| 'repeat' block 'until' exp
	| 'if' exp 'then' block ('elseif' exp 'then' block)* ('else' block)? 'end'
	| 'for' NAME '=' exp ',' exp (',' exp)? 'do' block 'end'
	| 'for' namelist 'in' explist 'do' block 'end'
	| 'function' funcname funcbody
	| 'local' namelist ('=' explist)?
	| 'local' 'function' NAME funcbody
	;

laststat
	: 'return' explist?
	| 'break'
    ;

funcname
    : NAME ('.' NAME)* (':' NAME)?
    ;

varlist
    : var (',' var)* -> ^(VARLIST var+)
    ;

var
    : NAME varEnd*
    | '(' exp ')' varEnd+
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
    : varOrExp nameAndArgs+
    ;

varOrExp 
	: var
	| '(' exp ')' -> ^(PAREN exp)
	;

nameAndArgs
	: (':' NAME)? args
	;	

args
    : '(' explist? ')'
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
    : '{' fieldlist? '}'
    ;

fieldlist
    : field (fieldsep field)* fieldsep?
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