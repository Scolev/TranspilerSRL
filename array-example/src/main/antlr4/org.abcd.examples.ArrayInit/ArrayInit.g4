grammar ArrayInit;

//Hierarchy of rules in generated node tree is dependent on their placement in the grammar file. The further up the higher priority



//Lexical rules
parse  //Start of input parsing, EOF = end of file
 : block EOF
 ;

block  //Redundant?
 : stat
 ;

stat //Highest level of rules
 : if_stat
 | while_stat
 | OBRACE assignment* CBRACE
 | assignment
 | dotchain
 | logger
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;


assignment  //Assign value to variable
 : (ID | STRING | expr | VARIABLE )+ (ASSIGN | ISA)* (ID | STRING | expr | VARIABLE) (SCOL|PERIOD)?
 | expr (SCOL|PERIOD)?
 | logger
 ;


dotchain  //Special case for chaining of methods, SRL uses '.' for both chaining methods and terminating lines.
 : (VARIABLE | ID) PERIOD (VARIABLE | ID) (PERIOD (VARIABLE | ID))*
 ;

while_stat  //The composition of a while-statement
 : WHILE boolean_block stat_block
 ;

if_stat  //The composition of an if-statement
 : IF boolean_block stat_block (ELSE IF else_block)* (ELSE else_block)?
 ;

else_block  //The composition of an else-statement
 : stat else_block*
 ;

boolean_block   //Special case for determining if the current expression is a boolean, in which case certain operators are different between SRL and Java
 : expr (boold|date_block)*
 ;

boold  //Allows for chains of boolean statements
 : (AND | OR) expr (boold|date_block)*
 ;

condition_block  //
 : expr stat_block
 ;


stat_block
 : stat*
 ;

date_block
 : op=(LTEQ | GTEQ | LT | GT | NEQ | DIFF) DATE
 ;

expr //Common expressions
 : MINUS expr                           #unaryMinusExpr
 | NOT expr                             #notExpr
 | expr op=(MULT | DIV | MOD) expr      #multiplicationExpr
 | expr op=(PLUS | MINUS) expr          #additiveExpr
 | expr op=(LTEQ | GTEQ | LT | GT | NEQ | DIFF) expr #relationalExpr
 | expr ASSIGN expr                     #assignExpr
 | expr OR expr                         #orExpr
 | expr DIFF expr                      #diffExpr
 | atom                                 #atomExpr
 | dotchain                             #dotchainExpr
 | VARIABLE                             #exprVariable
 | expr ASDATE                          #exprAsdate
//| expr  op=(LTEQ | GTEQ | LT | GT | NEQ | DIFF) DATE                                 #exprDate
 ;


booleanstat  //Common boolean statements
 : MINUS booleanstat                           #boolunaryMinusStat
 | NOT booleanstat                             #boolnotStat
 | booleanstat op=(MULT | DIV | MOD) booleanstat      #boolmultiplicationStat
 | booleanstat op=(PLUS | MINUS) booleanstat          #booladditiveStat
 | booleanstat op=(LTEQ | GTEQ | LT | GT | NEQ | DIFF) booleanstat #boolrelationalStat
 | booleanstat OR booleanstat                         #boolorStat
 ;


booleanexpr  //boolean expression
 : booleanexpr ASSIGN booleanexpr                 #boolassignExpr
 | atom                                           #boolstatexpr
 ;


atom  //Otherwise common tokens
 : OPAR expr CPAR #parExpr
 | (INT | FLOAT)  #numberAtom
 | (TRUE | FALSE) #booleanAtom
 | ID             #idAtom
 | STRING         #stringAtom
 | NULL           #nullAtom
 ;

logger  //PREG Logger
 : (LOGDEBUG | LOGFORMEL | LOGFORMELST | LOGFORMELSL) OPAR (expr|STRING)* CPAR SCOL
 ;



//Tokens

OR : 'or';
AND : 'and';
NEQ : ('!=' | 'is not');
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
MOD : '%';
POW : '^';
NOT : '!';
ISA : ('is a'|'is an');
DIFF: '<>';

SCOL : ';';
ASSIGN : '=';
OPAR : '(';
CPAR : ')';
OBRACE : '{';
CBRACE : '}';

TRUE : 'true';
FALSE : 'false';
IF : 'if';
WHILE: 'while';
ELSE: 'else';
THEN: 'then' -> skip ;
DATE: ['][0-9]+ '-'('jan'|'feb'|'mar'|'apr'|'mai'|'jun'|'jul'|'aug'|'sep'|'okt'|'nov'|'des')'-' [0-9]+['] ;
LOGDEBUG: 'log_debug' ;
LOGFORMEL: 'log_formel' ;
LOGFORMELST: 'log_formel_start' ;
LOGFORMELSL: 'log_formel_slutt' ;
ASDATE: 'as a date';
ASSTRING: 'as a string' -> skip;

ID
 : [a-zA-Z_]
 ;

INT
 : [0-9]+
 ;

FLOAT
 : [0-9]+ '.' [0-9]*
 | '.' [0-9]+
 ;

STRING
 : '"' (~["\r\n] | '""')* '"'
 ;

COMMENT
 : '#' ~[\r\n]* -> skip
 ;

SPACE
 : [ \t\r\n] -> skip
 ;

PERIOD
 : .
 ;

NULL
 : 'null'
 ;

VARIABLE
 : [']? [a-zA-Z_0-9]+ [']?
 ;