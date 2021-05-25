package com.example.features.health.gethealth;

import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
class GetHealthController {

    private GetHealthService getHealthService;
    
    public GetHealthController(GetHealthService getHealthService) {
        this.getHealthService = getHealthService;
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck(){
        if(Status.UP.getCode().equals(getHealthService.getDataSourceHealthIndicator())) {
            return new ResponseEntity<>("{\"status\" : \"UP\"}", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("{\"status\" : \"DOWN\"}", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
