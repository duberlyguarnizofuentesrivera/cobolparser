package com.duberlyguarnizo;

import java_cup.runtime.Symbol;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParsingLogic {

  private static final String NEW_LINE = "\n";

  public static Object[][] doLexicalAnalysis(String text) {
    Lexer lexer = new Lexer(new StringReader(text));
    List<Object[]> result = new ArrayList<>();

    int lineNumber = 1;
    try {
      while (true) {
        Tokens token = lexer.yylex();
        if (token == null) {
          break;
        }
        result.add(new Object[]{token, lexer.lexeme, lineNumber});
        if (token == Tokens.Linea) {
          lineNumber++;
        }
      }
    } catch (IOException e) {
      log.error("Error during lexical analysis", e);
      result.add(new Object[]{"Error", "Error during lexical analysis", lineNumber});
    }
    return result.toArray(new Object[0][]);
  }


  private static void appendToken(StringBuilder result, Tokens token, String lexeme,
                                  int lineNumber) {

    switch (token) {
      case T_dato:
        result.append("<Data Type>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case If:
        result.append("<If>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Main:
        result.append("<Main Reserved>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Parentesis_a:
        result.append("<Opening Parenthesis>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Parentesis_c:
        result.append("<Closing Parenthesis>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Llave_a:
        result.append("<Opening Brace>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Llave_c:
        result.append("<Closing Brace>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Identificador:
        result.append("<Identifier>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case P_coma:
        result.append("<Semicolon>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case Numero:
        result.append("<Number>\t\t").append(lexeme).append(NEW_LINE);
        break;
      case ERROR:
        result.append("<Undefined symbol>\t\t").append(lexeme).append(NEW_LINE);
        break;
      default:
        result.append("<").append(lexeme).append(">").append(NEW_LINE);
        break;
    }
  }

  public static String doSyntaxAnalysis(String text) {
    Parser parser = new Parser(new LexerCup(new StringReader(text)));
    try {
      parser.parse();
      return "Analysis completed successfully";
    } catch (Exception e) {
      Symbol sym = parser.getS();
      int line = sym.right + 1;
      int column = sym.left + 1;
      String message = String.format("Syntax error. Line: %d Column: %d, Text: \"%s\"",
          line, column, sym.value);
      log.error("Syntax error", e);
      return message;
    }
  }
}
