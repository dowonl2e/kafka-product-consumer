package com.study.kafka.productconsumer.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductDto {

  private Long productId;

  private String productName;

  private Integer productCount;

  private LocalDateTime inputDate = LocalDateTime.now();

  private LocalDateTime updateDate;



}
