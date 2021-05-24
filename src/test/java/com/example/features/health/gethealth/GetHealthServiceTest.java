package com.example.features.health.gethealth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Status;

@ExtendWith(MockitoExtension.class)
class GetHealthServiceTest {
    
    @Test
    @DisplayName("DataSourceHealthIndicatorService should return down")
    void dataSourceHealthIndicatorService_ShdReturnDown() {
        GetHealthService getHealthService = new GetHealthService(null);
        assertThat(getHealthService.getDataSourceHealthIndicator()).isEqualTo(Status.DOWN.getCode());
    }
}
