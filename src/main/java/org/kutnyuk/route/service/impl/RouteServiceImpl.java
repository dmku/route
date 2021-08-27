package org.kutnyuk.route.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kutnyuk.route.data.Country;
import org.kutnyuk.route.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
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

        LinkedList<Country> queue = new LinkedList<>();
        Country startCountry = graph.get(origin);
        startCountry.setProcessed(true);
        queue.offer(startCountry);

        while (!queue.isEmpty()){
            Country currentCountry = queue.poll();
             if(destination.equals(currentCountry.getName()))
             {
                 return reverseRoute(currentCountry);
             }

            List<Country> neighbours = currentCountry.getBorders()
                    .stream()
                    .map(countryName -> graph.get(countryName))
                    .collect(Collectors.toList());

             neighbours.forEach(neighbour -> {
                 neighbour.setProcessed(true);
                 neighbour.setPrevious(currentCountry);
                 queue.add(neighbour);
             });
        }

        return new ArrayList<>();
    }

    private List<String> reverseRoute(Country currentCountry) {
        Country previous = currentCountry.getPrevious();
        List<String> route = new ArrayList<>();
        route.add(currentCountry.getName());

        while (previous != null){
            route.add(previous.getName());
            previous = previous.getPrevious();
        }

        return route;
    }

}
