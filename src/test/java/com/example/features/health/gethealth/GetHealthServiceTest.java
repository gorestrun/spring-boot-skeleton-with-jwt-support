package com.example.features.health.gethealth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.jdbc.DataSourceHealthIndicator;

@ExtendWith(MockitoExtension.class)
class GetHealthServiceTest {
    
    @Mock
    private DataSourceHealthIndicator dataSourceHealthIndicator;
    
    @InjectMocks
    private GetHealthService getHealthService;
    
    @Test
    @DisplayName("DataSourceHealthIndicatorService should return down")
    void dataSourceHealthIndicatorService_ShdReturnDown() {

        given(dataSourceHealthIndicator.getHealth(false)).willReturn(Health.down().build());
        assertThat(getHealthService.getDataSourceHealthIndicator()).isEqualTo(Status.DOWN.getCode());
        verify(dataSourceHealthIndicator).getHealth(false);
    }
    
    @Test
    @DisplayName("DataSourceHealthIndicatorService should return up")
    void getDataSourceHealthIndicator_ShdReturnUp() {
        given(dataSourceHealthIndicator.getHealth(false)).willReturn(Health.up().build());
        assertThat(getHealthService.getDataSourceHealthIndicator()).isEqualTo(Status.UP.getCode());
        verify(dataSourceHealthIndicator).getHealth(false);
    }
}
