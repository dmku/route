package org.kutnyuk.route.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.kutnyuk.route.data.Country;
import org.kutnyuk.route.eh.CountryNotFoudException;
import org.kutnyuk.route.eh.RouteNotFoundException;
import org.kutnyuk.route.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RouteServiceImpl implements RouteService {

    private String countriesUrl;
    private ObjectMapper objectMapper;
    private Map<String, Country> graph = new HashMap<>();

    public RouteServiceImpl(ObjectMapper objectMapper, @Value("${application.countriesUrl}") String countriesUrl) {
        this.objectMapper = objectMapper;
        this.countriesUrl = countriesUrl;
        init();
    }

    private void init(){
        try {
            List<Country> countries = objectMapper.readValue(new URL(countriesUrl), new TypeReference<>() {});

            graph.putAll(countries.stream()
                    .collect(Collectors.toMap(Country::getName, Function.identity())));




        } catch (Exception e) {
            throw new IllegalStateException("Can not parse source json", e);
        }
    }

    @Override
    public List<String> evaluateRoute(String origin, String destination) {

        log.info("origin=?, destination=?", destination, origin);


        Map<String, Country> clonedGraph = SerializationUtils.clone((HashMap<String, Country>) graph);
        LinkedList<Country> queue = new LinkedList<>();
        int counter = 1;

        Country startCountry = findCountry(clonedGraph, origin);
        log.info("startCountry=" + startCountry);

        Country destinationCountry = findCountry(clonedGraph, destination);
        log.info("destinationCountry=" + destinationCountry);

        startCountry.setProcessed(true);
        queue.add(startCountry);


        while (!queue.isEmpty()){
            log.info("Iteration #" + counter);

            Country currentCountry = queue.poll();
            log.info("currentCountry=" + currentCountry);

            if(destination.equals(currentCountry.getName()))
             {
                 return reverseRoute(currentCountry);
             }

            List<Country> neighbours = currentCountry.getBorders()
                    .stream()
                    .map(countryName -> findCountry(clonedGraph, countryName))
                    .collect(Collectors.toList());


             neighbours.forEach(neighbour -> {
                 log.info("neighbour=" + neighbour);
                 if(!neighbour.isProcessed()){
                     neighbour.setProcessed(true);
                     neighbour.setPrevious(currentCountry);
                     queue.add(neighbour);
                 }
             });

            log.info("QUEUE:");
            queue.forEach(q -> log.info(q.toString()));

            counter++;
        }

        throw new RouteNotFoundException("Route was not found");
    }

    private Country findCountry(Map<String, Country> clonedGraph, String key) {

        return Optional.ofNullable(clonedGraph.get(key))
                  .orElseThrow( () -> new CountryNotFoudException("Country was not found by key=" + key));
    }

    private List<String> reverseRoute(Country currentCountry) {
        Country previous = currentCountry.getPrevious();
        List<String> route = new LinkedList<>();

        route.add(currentCountry.getName());


        while (previous != null){
            route.add(previous.getName());
            previous = previous.getPrevious();
        }

        Collections.reverse(route);

        return route;
    }

}
