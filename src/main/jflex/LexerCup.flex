package com.duberlyguarnizo;

import java_cup.runtime.Symbol;

/* Include Java Cup symbol class */
import static com.duberlyguarnizo.parser.sym.*;

%%
%class FlexCup
%type java_cup.runtime.Symbol
%cup
%unicode
%line
%column
L=[a-zA-Z_]
D=[0-9]
espacio=[ \t\r\n]+

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}

%%

/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
"//*".* {/*Ignore*/}

/* Nueva línea */
\n { return symbol(Linea); }

/* Tipos de datos */
"byte"    { return symbol(T_dato, yytext()); }
"char"    { return symbol(T_dato, yytext()); }
"long"    { return symbol(T_dato, yytext()); }
"float"   { return symbol(T_dato, yytext()); }
"double"  { return symbol(T_dato, yytext()); }

/* Palabra reservada If */
"if"    { return symbol(If, yytext()); }

/* Palabra reservada Else */
"else"  { return symbol(Else, yytext()); }

/* Palabra reservada While */
"while" { return symbol(While, yytext()); }

/* Operador Igual */
"="   { return symbol(Igual, yytext()); }

/* Operador Suma */
"+"   { return symbol(Suma, yytext()); }

/* Operador Resta */
"-"   { return symbol(Resta, yytext()); }

/* Operador Multiplicacion */
"*"   { return symbol(Multiplicacion, yytext()); }

/* Operador Division */
"/"   { return symbol(Division, yytext()); }

/* Parentesis de apertura */
"("   { return symbol(Parentesis_a, yytext()); }

/* Parentesis de cierre */
")"   { return symbol(Parentesis_c, yytext()); }

/* Llave de apertura */
"{"   { return symbol(Llave_a, yytext()); }

/* Llave de cierre */
"}"   { return symbol(Llave_c, yytext()); }

/* Marcador de inicio de algoritmo */
"main" { return symbol(Main, yytext()); }

/* Punto y coma */
";"   { return symbol(P_coma, yytext()); }

/* Identificador */
{L}({L}|{D})* { return symbol(Identificador, yytext()); }

/* Numero */
("(-"{D}+")")|{D}+ { return symbol(Numero, yytext()); }

/* Error de analisis */
.    { return symbol(ERROR, yytext()); }
