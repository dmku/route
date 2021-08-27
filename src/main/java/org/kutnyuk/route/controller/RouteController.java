package org.kutnyuk.route.controller;

import lombok.AllArgsConstructor;
import org.kutnyuk.route.service.RouteService;
import org.springframework.web.bind.annotation.RestController;
import org.kutnyuk.route.dto.RouteDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@AllArgsConstructor
public class RouteController implements RouteApi{

    RouteService routeService;

    @Override
    public ResponseEntity<RouteDto> findRoute(String origin,String destination){
        RouteDto routeDto = new RouteDto();
        List<String> route = routeService.evaluateRoute(origin, destination);

        if(route.isEmpty()){
            return ResponseEntity.badRequest().body(new RouteDto());
        }
        else {
            routeDto.setRoute(route);
            return ResponseEntity.ok().body(routeDto);
        }
    }
}
