package org.kutnyuk.route.eh;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CountryNotFoudException extends ResponseStatusException {
    public CountryNotFoudException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public CountryNotFoudException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public CountryNotFoudException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
