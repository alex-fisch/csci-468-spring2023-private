package edu.montana.csci.csci468.tokenizer;

import static edu.montana.csci.csci468.tokenizer.TokenType.*;

public class CatScriptTokenizer {

    TokenList tokenList;
    String src;
    int position = 0;
    int line = 1;
    int lineOffset = 0;

    public CatScriptTokenizer(String source) {
        src = source;
        tokenList = new TokenList(this);
        tokenize();
    }

    private void tokenize() { //done
        while (!tokenizationEnd()) {
            consumeWhitespace();
            scanToken();
        }
        tokenList.addToken(EOF, "<EOF>", position, position, line, lineOffset);
    }

    private void scanToken() { //done
        if(scanNumber()) {
            return;
        }
        if(scanString()) {
            return;
        }
        if(scanIdentifier()) {
            return;
        }
        scanSyntax();
    }

    private boolean scanString() { //done
        if (peek() != '\"') return false;
        char str_delimiter = takeChar();

        int start = position;
        while (peek() != '\"' && !tokenizationEnd()) {
            if (peek() == '\\') {
                matchAndConsume('\\');
                if (tokenizationEnd()) break;
            }
            takeChar();
        }
        String value = src.substring(start, position);

        if (!tokenizationEnd()) {
            takeChar();
            tokenList.addToken(STRING, value, start, position, line, lineOffset);
        } else {
            tokenList.addToken(ERROR, value, start, position, line, lineOffset);
        }
        return true;
    }

    private boolean scanIdentifier() { //done
        if (!isAlpha(peek())) return false;

        int start = position;
        while (isAlphaNumeric(peek())) takeChar();

        String value = src.substring(start, position);
        TokenType type = KEYWORDS.containsKey(value) ? KEYWORDS.get(value) : IDENTIFIER;
        tokenList.addToken(type, value, start, position, line, lineOffset);

        return true;
    }

    private boolean scanNumber() { //done
        int start = position;
        if (!isDigit(peek())) return false;
        while (isDigit(peek())) takeChar();
        tokenList.addToken(INTEGER, src.substring(start, position), start, position, line, lineOffset);
        return true;
    }

    private void scanSyntax() { //done
        int start = position;

        if(matchAndConsume('(')) tokenList.addToken(LEFT_PAREN, "(", start, position, line, lineOffset);
        else if(matchAndConsume(')')) tokenList.addToken(RIGHT_PAREN, ")", start, position, line, lineOffset);
        else if(matchAndConsume('{')) tokenList.addToken(LEFT_BRACE, "{", start, position, line, lineOffset);
        else if(matchAndConsume('}')) tokenList.addToken(RIGHT_BRACE, "}", start, position, line, lineOffset);
        else if(matchAndConsume('[')) tokenList.addToken(LEFT_BRACKET, "[", start, position, line, lineOffset);
        else if(matchAndConsume(']')) tokenList.addToken(RIGHT_BRACKET, "]", start, position, line, lineOffset);
        else if(matchAndConsume(':')) tokenList.addToken(COLON, ":", start, position, line, lineOffset);
        else if(matchAndConsume(',')) tokenList.addToken(COMMA, ",", start, position, line, lineOffset);
        else if(matchAndConsume('.')) tokenList.addToken(DOT, ".", start, position, line, lineOffset);
        else if(matchAndConsume('-')) tokenList.addToken(MINUS, "-", start, position, line, lineOffset);
        else if(matchAndConsume('+')) tokenList.addToken(PLUS, "+", start, position, line, lineOffset);

        else if(matchAndConsume('/')) {
            if (matchAndConsume('/')) {

                while (peek() != '\n' && !tokenizationEnd()) {
                    takeChar();
                }
            } else {
                tokenList.addToken(SLASH, "/", start, position, line, lineOffset);
            }
        }
        else if(matchAndConsume('*')) tokenList.addToken(STAR, "*", start, position, line, lineOffset);
        else if (matchAndConsume('!')) {
            if (matchAndConsume('=')) {
                tokenList.addToken(BANG_EQUAL, "!=", start, position, line, lineOffset);
            }
        }
        else if(matchAndConsume('=')) {
            if (matchAndConsume('=')) {
                tokenList.addToken(EQUAL_EQUAL, "==", start, position, line, lineOffset);
            } else {
                tokenList.addToken(EQUAL, "=", start, position, line, lineOffset);
            }
        }
        else if(matchAndConsume('>')) {
            if (matchAndConsume('=')) {
                tokenList.addToken(GREATER_EQUAL, ">=", start, position, line, lineOffset);
            } else {
                tokenList.addToken(GREATER, ">", start, position, line, lineOffset);
            }
        }
        else if(matchAndConsume('<')) {
            if (matchAndConsume('=')) {
                tokenList.addToken(LESS_EQUAL, "<=", start, position, line, lineOffset);
            } else {
                tokenList.addToken(LESS, "<", start, position, line, lineOffset);
            }
        }
        else tokenList.addToken(ERROR, "<Unexpected Token: [" + takeChar() + "]>", start, position, line, lineOffset);
    }
//complete
    private void consumeWhitespace() { //done
        while (!tokenizationEnd() && (peek() == ' ' || peek() == '\r' || peek() == '\t' || peek() == '\n')) {
            char c = peek();
            position++;
            if (c == '\n') {
                line++;
                lineOffset = 0;
            } else {
                lineOffset++;
            }
        }
    }

    private char peek() { //done
        if (tokenizationEnd()) return '\0';
        return src.charAt(position);
    }

    private boolean isAlpha(char c) { //done
        return Character.isLetter(c) || c == '_';
    }

    private boolean isAlphaNumeric(char c) { //done
        return Character.isLetterOrDigit(c);
    }

    private boolean isDigit(char c) {//done
        return Character.isDigit(c);
    }

    private char takeChar() { //done
        char c = src.charAt(position);
        position++;
        lineOffset++;
        return c;
    }

    private boolean tokenizationEnd() { //done
        return !(position < src.length());
    }

    public boolean matchAndConsume(char c) { //done
        return (peek() == c) && takeChar() != '\0';
    }

    public TokenList getTokens() { //done
        return tokenList;
    }

    @Override
    public String toString() { //done
        StringBuilder sb = new StringBuilder(src);
        if (!tokenizationEnd()) {
            int start = position;
            int end = Math.min(position + 1, src.length());
            sb.insert(end, "<--");
            sb.insert(start, "[" + peek() + "]-->");
        }
        return sb.toString();
    }
}

