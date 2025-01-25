package com.galacsh;

public class PassportMissingException extends RuntimeException {
    public PassportMissingException() {
        super("Passport is required");
    }
}
