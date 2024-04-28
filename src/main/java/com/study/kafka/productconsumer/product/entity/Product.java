package com.study.kafka.productconsumer.product.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity(name = "products")
@Getter
@Comment("제품")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

  @Id
  @Comment("PK")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Comment("제품명")
  private String productName;

  @Comment("제품개수")
  private Integer productCount;

  @Comment("등록일시")
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime inputDate = LocalDateTime.now();

  @Comment("수정일시")
  @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime updateDate;

  @Builder
  public Product(
      Long productId, String productName, Integer productCount,
      LocalDateTime inputDate
  ){
    this.productId = productId;
    this.productName = productName;
    this.productCount = productCount;
    this.inputDate = inputDate;
  }

  public void updateProductCount(Integer productCount){
    this.productCount = productCount;
    this.updateDate = LocalDateTime.now();
  }
}
