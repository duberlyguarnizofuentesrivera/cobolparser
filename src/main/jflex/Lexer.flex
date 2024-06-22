package com.duberlyguarnizo;

import java.io.IOException;
import java.io.Reader;

/* Include Tokens class */
import static com.duberlyguarnizo.Tokens.*;

%%

/* Define regular expressions */
%class COBOLLexer
%implements com.duberlyguarnizo.Lexer // Adjust this interface as needed

/* Java code section */
%{
    private String lexeme;
    private Reader input;

    // Constructor to initialize the lexer with input
    public COBOLLexer(Reader input) {
        this.input = input;
    }

    // Method to get the next token
    public Tokens yylex() throws IOException {
        int yychar = yylexInt();
        return Tokens.values()[yychar];
    }

    // Method to actually perform lexing
    private int yylexInt() throws IOException {
        // Clear lexeme before starting
        lexeme = null;
        int yychar;

/* Ignore whitespace */
{espacio} {/*Ignorar*/}

/* Ignore comments */
( "//"(.)* ) {/*Ignorar*/}

/* Nueva línea */
( "\n" ) {return Linea;}

/* Tipos de datos */
( byte | int | char | long | float | double ) {lexeme=yytext(); return T_dato;}

/* Palabra reservada If */
( if ) {lexeme=yytext(); return If;}

/* Palabra reservada Else */
( else ) {lexeme=yytext(); return Else;}

/* Palabra reservada While */
( while ) {lexeme=yytext(); return While;}

/* Operador Igual */
( "=" ) {lexeme=yytext(); return Igual;}

/* Operador Suma */
( "+" ) {lexeme=yytext(); return Suma;}

/* Operador Resta */
( "-" ) {lexeme=yytext(); return Resta;}

/* Operador Multiplicacion */
( "*" ) {lexeme=yytext(); return Multiplicacion;}

/* Operador Division */
( "/" ) {lexeme=yytext(); return Division;}

/* Parentesis de apertura */
( "(" ) {lexeme=yytext(); return Parentesis_a;}

/* Parentesis de cierre */
( ")" ) {lexeme=yytext(); return Parentesis_c;}

/* Llave de apertura */
( "{" ) {lexeme=yytext(); return Llave_a;}

/* Llave de cierre */
( "}" ) {lexeme=yytext(); return Llave_c;}

/* Marcador de inicio de algoritmo */
( "main" ) {lexeme=yytext(); return Main;}

/* Punto y coma */
( ";" ) {lexeme=yytext(); return P_coma;}

/* Identificador */
{L}({L}|{D})* {lexeme=yytext(); return Identificador;}

/* Numero */
("(-"{D}+")")|{D}+ {lexeme=yytext(); return Numero;}

/* Error de analisis */
 . {return ERROR;}

    }
%}

/* JFlex rules section */
%%

/* Java code section (optional) */

// Example usage
public static void main(String[] args) throws IOException {
    String input = "int x = 42;";
    COBOLLexer lexer = new COBOLLexer(new StringReader(input));
    Tokens token;
    while ((token = lexer.yylex()) != Tokens.ERROR) {
        System.out.println("Token: " + token + ", Lexeme: " + lexer.yytext());
    }
}
