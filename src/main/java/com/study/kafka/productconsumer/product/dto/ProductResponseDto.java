package com.study.kafka.productconsumer.product.dto;

import com.study.kafka.productconsumer.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponseDto {

  private Long productId;

  private String productName;

  private Integer productCount;

  private LocalDateTime inputDate;

  private LocalDateTime updateDate;

  public ProductResponseDto(Product product){
    productId = product.getProductId();
    productName = product.getProductName();
    productCount = product.getProductCount();
    inputDate = product.getInputDate();
    updateDate = product.getUpdateDate();
  }
}
