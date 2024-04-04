grammar Crux;
program
 : declList EOF
 ;

declList
 : decl*
 ;

decl
 : varDecl
 | functionDefn
 // | arrayDecl
 ;

varDecl
 : type Identifier SemiColon
 ;

functionDefn
: type Identifier Open_paren paramList Close_paren stmtBlock
;

//arrayDecl
// :
// ;

type
 : Identifier
 ;

literal
 : Integer
 | True
 | False
 ;

designator
: Identifier ( Open_braket expr0 Close_bracket )?
;

paramList
: ( param ( ',' param )* )?
;

param: type Identifier;

stmtBlock
: Open_brace stmtList Close_brace
;

stmtList
: stmt*
;

stmt
: varDecl
| callStmt
;

callStmt
: callExpr SemiColon
;

callExpr
: Identifier Open_paren exprList Close_paren
;

exprList
: ( expr0 ( ',' expr0 )* )?
;

expr0
: expr1 ( op0 expr1 )?
;

expr1
: expr2
| expr1 op1 expr2
;

expr2
: expr3
| expr2 op2 expr3
;

expr3
: '!' expr3
| Open_paren expr0 Close_paren
| designator
| callExpr
| literal
;

SemiColon: ';';

Integer
 : '0'
 | [1-9] [0-9]*
 ;

True: 'true';
False: 'false';

Identifier
 : [a-zA-Z] [a-zA-Z0-9_]*
 ;

WhiteSpaces
 : [ \t\r\n]+ -> skip
 ;

Comment
 : '//' ~[\r\n]* -> skip
 ;

AND: '&&';
OR: '||';
NOT: '!';
IF: 'if';
ELSE: 'else';
FOR: 'for';
BREAK: 'break';
RETURN: 'return';

Open_paren: '(';
Close_paren: ')';
Open_brace: '{';
Close_brace: '}';
Open_braket: '[';
Close_bracket: ']';
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
GE: '>=';
LE: '<=';
Not_Equal: '!=';
Equal: '==';
GT: '>';
LT: '<';
Assign: '=';
Comma: ',';

op0: '>=' | '<=' | '!=' | '==' | '>' | '<' ;
op1: '+' | '-' | '||';
op2: '*' | '/' | '&&';