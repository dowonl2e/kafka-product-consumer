package com.study.kafka.productconsumer.config;

import com.study.kafka.productconsumer.config.interceptor.KafkaProducerInterceptor;
import com.study.kafka.productconsumer.config.listener.KafkaProducerListener;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.producer.key-serializer}")
  private String keySerializer;

  @Value("${spring.kafka.producer.value-serializer}")
  private String valueSerializer;

  @Value("${spring.kafka.producer.transactional.id}")
  private String transactionalId;


  private final KafkaProducerInterceptor producerInterceptor;
  private final KafkaProducerListener producerListener;

  @Bean
  public ProducerFactory<String, Map<String, Object>> producerFactory() {
    Map<String,Object> configs = new HashMap<>();
    configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
    configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
    configs.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalId);
    DefaultKafkaProducerFactory<String, Map<String, Object>> producerFactory = new DefaultKafkaProducerFactory<>(configs);
    return producerFactory;
  }

  @Bean
  public KafkaTemplate<String, Map<String,Object>> kafkaTemplate() {
    KafkaTemplate<String, Map<String,Object>> kafkaTemplate = new KafkaTemplate<>(producerFactory());
    kafkaTemplate.setProducerInterceptor(producerInterceptor);
    kafkaTemplate.setProducerListener(producerListener);
    kafkaTemplate.setTransactionIdPrefix(transactionalId+"-tx-");
    return kafkaTemplate;
  }
}
