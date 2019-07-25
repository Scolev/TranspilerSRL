grammar ArrayInit;


parse
 : block EOF
 ;

block
 : stat
 ;

stat
 : if_stat
 | while_stat
 | OBRACE assignment* CBRACE
 | assignment
 | dotchain
 | logger
 | OTHER {System.err.println("unknown char: " + $OTHER.text);}
 ;


assignment
 : (ID | STRING | expr | VARIABLE )+ (ASSIGN | ISA)* (ID | STRING | expr | VARIABLE) (SCOL|PERIOD)?
 | expr (SCOL|PERIOD)?
 | logger
 ;


dotchain
 : (VARIABLE | ID) PERIOD (VARIABLE | ID) (PERIOD (VARIABLE | ID))*
 ;

while_stat
 : WHILE boolean_block stat_block
 ;

if_stat
 : IF boolean_block stat_block (ELSE IF else_block)* (ELSE else_block)?
 ;

else_block
 : stat else_block*
 ;

boolean_block
 : expr boold*
 ;

boold
 : (AND | OR) expr boold*
 ;

condition_block
 : expr stat_block
 ;

stat_block
 : stat*
 ;

expr
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
 ;


booleanstat
 : MINUS booleanstat                           #boolunaryMinusStat
 | NOT booleanstat                             #boolnotStat
 | booleanstat op=(MULT | DIV | MOD) booleanstat      #boolmultiplicationStat
 | booleanstat op=(PLUS | MINUS) booleanstat          #booladditiveStat
 | booleanstat op=(LTEQ | GTEQ | LT | GT | NEQ | DIFF) booleanstat #boolrelationalStat
 | booleanstat OR booleanstat                         #boolorStat
 ;


booleanexpr
 : booleanexpr ASSIGN booleanexpr                 #boolassignExpr
 | atom                                           #boolstatexpr
 ;


atom
 : OPAR expr CPAR #parExpr
 | (INT | FLOAT)  #numberAtom
 | (TRUE | FALSE) #booleanAtom
 | ID             #idAtom
 | STRING         #stringAtom
 | NULL           #nullAtom
 ;

logger
 : (LOGDEBUG | LOGFORMEL | LOGFORMELST | LOGFORMELSL) OPAR (expr|STRING)* CPAR SCOL
 ;


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