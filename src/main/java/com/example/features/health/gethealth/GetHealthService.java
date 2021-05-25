package com.example.features.health.gethealth;

import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;
import org.springframework.stereotype.Service;

@Service
class GetHealthService {

    private final DataSourceHealthIndicator dataSourceHealthIndicator;
    
    public GetHealthService(DataSourceHealthIndicator dataSourceHealthIndicator) {
        this.dataSourceHealthIndicator = dataSourceHealthIndicator;
    }
    
    public String getDataSourceHealthIndicator() {
        if(dataSourceHealthIndicator == null) {
            return Status.DOWN.getCode();
        }
        
        return dataSourceHealthIndicator.getHealth(false).getStatus().getCode();
    }
}
