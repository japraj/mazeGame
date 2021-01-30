package model.tokenizer;

// Parse and tokenize user input, abstracting raw data
public class Tokenizer {

    private String token;
    private Token type;

    // EFFECTS: set current token as invalid type
    public Tokenizer() {

    }

    // MODIFIES: this
    // EFFECTS: parse given String and set it as current token
    public Token parseToken(String token) {
        return null; // stub
    }

    // TODO: add methods for various types of tokens (incl. a null token for invalid inputs)

    public String getToken() {
        return token;
    }

    public Token getTokenType() {
        return type;
    }

}
