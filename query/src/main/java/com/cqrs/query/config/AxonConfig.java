package com.cqrs.query.config;

import com.cqrs.query.version.HolderCreationEventV1;
import org.axonframework.config.EventProcessingConfigurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.axonframework.eventhandling.async.SequentialPerAggregatePolicy;
import org.axonframework.serialization.upcasting.event.EventUpcasterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Autowired
    public void configure(EventProcessingConfigurer configurer) {
        configurer.registerTrackingEventProcessor(
                "accounts",
                org.axonframework.config.Configuration::eventStore,
                c -> TrackingEventProcessorConfiguration.forSingleThreadedProcessing()
                        .andBatchSize(100)
        );

        configurer.registerSequencingPolicy("accounts",
                configuration -> SequentialPerAggregatePolicy.instance());
    }

    @Bean
    public EventUpcasterChain eventUpcasterChain(){
        return new EventUpcasterChain(
                new HolderCreationEventV1()
        );
    }
}
