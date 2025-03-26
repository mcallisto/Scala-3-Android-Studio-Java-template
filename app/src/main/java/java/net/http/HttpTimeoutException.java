package java.net.http;

import java.io.IOException;

public class HttpTimeoutException extends IOException {
    HttpTimeoutException(String message) {
        super(message);
    }
}