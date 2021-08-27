package org.kutnyuk.route.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kutnyuk.route.data.Country;
import org.kutnyuk.route.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
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

    @Override
    public List<String> evaluateRoute(String origin, String destination) {


        return countries.stream()
                .map(country -> country.getCca3()).collect(Collectors.toList());
    }

    private void init(){
        try {
            countries = objectMapper
                    .readValue(new URL(countriesUrl), new TypeReference<List<Country>>(){});
        } catch (Exception e) {
            throw new IllegalStateException("Can not parse source json", e);
        }
    }
}
