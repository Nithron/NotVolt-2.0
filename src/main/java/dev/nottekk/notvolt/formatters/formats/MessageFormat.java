package dev.nottekk.notvolt.formatters.formats;

public class MessageFormat {

    public String message;
    public boolean bold;
    public boolean italic;
    public boolean underline;

    public MessageFormat(String message, boolean bold, boolean italic, boolean underline) {
        this.message = message;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
    }

}
