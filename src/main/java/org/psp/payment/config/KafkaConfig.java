package org.psp.payment.config;/* admin created on 4/2/2025  */

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    @Bean
    public NewTopic searchTopic() {
        return TopicBuilder.name("search-events").partitions(3).replicas(1).build();
    }
}
