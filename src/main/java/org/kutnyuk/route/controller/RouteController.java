package org.kutnyuk.route.controller;

import lombok.AllArgsConstructor;
import org.kutnyuk.route.service.RouteService;
import org.springframework.web.bind.annotation.RestController;
import org.kutnyuk.route.dto.RouteDto;
import org.springframework.http.ResponseEntity;

@RestController
@AllArgsConstructor
public class RouteController implements RouteApi{

    RouteService routeService;

    @Override
    public ResponseEntity<RouteDto> findRoute(String origin,String destination){
        RouteDto routeDto = new RouteDto();
        routeDto.setRoute(routeService.evaluateRoute(origin, destination));
        return ResponseEntity.ok().body(routeDto);
    }
}
