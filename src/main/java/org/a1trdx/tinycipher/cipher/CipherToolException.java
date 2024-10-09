package org.a1trdx.tinycipher.cipher;

public class CipherToolException extends RuntimeException {

    public CipherToolException() {
    }

    public CipherToolException(String message) {
        super(message);
    }

    public CipherToolException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipherToolException(Throwable cause) {
        super(cause);
    }
}
