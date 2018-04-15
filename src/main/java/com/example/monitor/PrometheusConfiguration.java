package com.example.monitor;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;

@Configuration
@ConditionalOnClass(CollectorRegistry.class)
public class PrometheusConfiguration {
    @Bean
    @ConditionalOnMissingBean
    CollectorRegistry metricRegistry() {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;
        registry.register(new StandardExports());
        registry.register(new MemoryPoolsExports());
        return registry;
    }

    @Bean
    ServletRegistrationBean registerPrometheusExporterServlet(CollectorRegistry metricRegistry) {
          return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/metrics");
    }
}
