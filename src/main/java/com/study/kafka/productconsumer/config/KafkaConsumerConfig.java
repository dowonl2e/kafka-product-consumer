package com.study.kafka.productconsumer.config;

import jdk.jfr.ValueDescriptor;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.apache.kafka.common.IsolationLevel.READ_COMMITTED;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Value("${spring.kafka.consumer.auto-offset-reset}")
  private String autoOffsetReset;

  @Value("${spring.kafka.consumer.key-deserializer}")
  private String keyDeserializer;

  @Value("${spring.kafka.consumer.value-deserializer}")
  private String valueDeserializer;

  /**
   * Kafka Listener Container를 생성하고 구성하는 인터페이스로, Kafka Consumer의 동작을 제어하기 위해 사용됩니다.
   * @return
   */
  @Bean
  public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Map<String, Object>>> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Map<String, Object>> factory = new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    factory.setConcurrency(2);
    factory.getContainerProperties().setPollTimeout(3000); // 3초의 지연 설정
    factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
    return factory;
  }

  @Bean
  public ConsumerFactory<String, Map<String, Object>> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(consumerConfig());
  }

  @Bean
  public Map<String, Object> consumerConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
    props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, READ_COMMITTED.toString().toLowerCase(Locale.ROOT));
    return props;
  }
}