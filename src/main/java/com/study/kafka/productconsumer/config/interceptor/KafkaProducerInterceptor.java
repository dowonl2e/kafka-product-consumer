package com.study.kafka.productconsumer.config.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaProducerInterceptor implements ProducerInterceptor<String, Map<String, Object>> {
  @Override
  public ProducerRecord<String, Map<String, Object>> onSend(ProducerRecord<String, Map<String, Object>> producerRecord) {
    log.info("ProducerInterceptor.onSend()");
    log.info("Message Header -> " + producerRecord.headers());
    log.info("Message Body -> " + producerRecord.value());
    return producerRecord;
  }

  @Override
  public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
    log.info("ProducerInterceptor.onAcknowledgement()");
    log.info("Send Result Topic -> " + recordMetadata.topic());
    log.info("Send Result Partition -> " + recordMetadata.partition());
    log.info("Send Result -> " + (e == null ? "Success" : "Fail"));
    if(e != null) log.info("Send Result Exception -> " + e);
  }

  @Override
  public void close() {
    log.info("ProducerInterceptor.close()");
  }

  @Override
  public void configure(Map<String, ?> map) {
    log.info("ProducerInterceptor.configure()");
    log.info("Configuration -> " + map);
  }
}