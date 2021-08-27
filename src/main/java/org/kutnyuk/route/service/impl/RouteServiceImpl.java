package org.kutnyuk.route.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kutnyuk.route.data.Country;
import org.kutnyuk.route.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RouteServiceImpl implements RouteService {

    private String countriesUrl;
    private List<Country> countries;
    ObjectMapper objectMapper;

    public RouteServiceImpl(ObjectMapper objectMapper, @Value("${application.countriesUrl}") String countriesUrl) {
        this.objectMapper = objectMapper;
        this.countriesUrl = countriesUrl;
        init();
    }

    private void init(){
        try {
            countries = objectMapper.readValue(new URL(countriesUrl), new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalStateException("Can not parse source json", e);
        }
    }

    @Override
    public List<String> evaluateRoute(String origin, String destination) {

        Map<String, Country> graph = new HashMap<>();
        graph.putAll(countries.stream()
                .collect(Collectors.toMap(Country::getName, Function.identity())));


        return null;
    }

}
