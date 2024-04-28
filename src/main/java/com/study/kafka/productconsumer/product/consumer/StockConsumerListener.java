package com.study.kafka.productconsumer.product.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.kafka.productconsumer.dto.ResponseDto;
import com.study.kafka.productconsumer.product.entity.Product;
import com.study.kafka.productconsumer.product.entity.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockConsumerListener implements AcknowledgingMessageListener<String, Map<String, Object>> {

  private static final String REQUEST_TOPIC = "stock-valid-request";
  private static final String RESPONSE_TOPIC = "stock-valid-response";

  private final ProductRepository productRepository;

  private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

  private final ObjectMapper objectMapper;
  @Transactional
  @KafkaListener(groupId = "order-product", topics = REQUEST_TOPIC, containerFactory = "kafkaListenerContainerFactory")
  public void onMessage(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) {
    log.info("Order Product Consumer receiveProductCheckResponse Offset -> {} " + record.offset());
    log.info("Order Product Consumer receiveProductCheckResponse Value -> {} " + record.value());
    Map<String, Object> map = record.value();
    ResponseDto response = ResponseDto.toResponse(HttpStatus.INTERNAL_SERVER_ERROR, map);
    try {
      if(map != null && map.get("orderCount") != null && map.get("productId") != null && (Integer) map.get("orderCount") > 0){
        Integer orderCount = (Integer) map.get("orderCount");
        Long productId = Long.parseLong(String.valueOf(map.get("productId")));
        Product product = productRepository.findById(productId).orElse(null);

        if (product != null && product.getProductCount() != null && product.getProductCount() >= orderCount) {
          product.updateProductCount(product.getProductCount() - orderCount);
          response = ResponseDto.toResponse(HttpStatus.OK, map);
        }
        ack.acknowledge();
      }
    }
    catch (Exception e){
      e.printStackTrace();
    }
    kafkaTemplate.send(RESPONSE_TOPIC, objectMapper.convertValue(response, Map.class));
  }

}
