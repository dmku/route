package org.kutnyuk.route.eh;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RouteNotFoundException extends ResponseStatusException {

    public RouteNotFoundException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public RouteNotFoundException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    public RouteNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
