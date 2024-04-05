grammar Crux;

literal
 : Integer
 | True
 | False
 ;

designator
 : Identifier ( Open_bracket expr0 Close_bracket )?
 ;

type
 : Identifier
 ;

op0
 : GE
 | LE
 | Not_Equal
 | Equal
 | GT
 | LT
 ;

op1
 : ADD
 | SUB
 | '||'
 ;

op2
 : MUL
 | DIV
 | '&&'
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

callExpr
 : Identifier Open_paren exprList Close_paren
 ;

exprList
 : ( expr0 ( ',' expr0 )* )?
 ;

param
 : type Identifier
 ;

paramList
 : ( param ( ',' param )* )?
 ;

varDecl
 : type Identifier SemiColon
 ;

arrayDecl
 : type Identifier Open_bracket Integer Close_bracket SemiColon
 ;

functionDefn
 : type Identifier Open_paren paramList Close_paren stmtBlock
 ;

decl
 : varDecl
 | arrayDecl
 | functionDefn
 ;

declList
 : decl*
 ;

assignStmt
 : designator Assign expr0 SemiColon
 ;

callStmt
 : callExpr SemiColon
 ;

ifStmt
 : 'if' expr0 stmtBlock ( 'else' stmtBlock )?
 ;

loopStmt
 : 'loop' stmtBlock
 ;

breakStmt
 : 'break' SemiColon
 ;

continueStmt
 : 'continue' SemiColon
 ;

returnStmt
 : 'return' expr0 SemiColon
 ;

stmt
 : varDecl
 | callStmt
 | assignStmt
 | ifStmt
 | loopStmt
 | breakStmt
 | continueStmt
 | returnStmt
 ;

stmtList
 : stmt*
 ;

stmtBlock
 : Open_brace stmtList Close_brace
 ;

program
 : declList EOF
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

Open_paren: '(';
Close_paren: ')';
Open_brace: '{';
Close_brace: '}';
Open_bracket: '[';
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