package org.kutnyuk.route.service;

import java.util.List;

public interface RouteService {
    List<String> evaluateRoute(String origin, String destination);
}
