package com.study.kafka.productconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.serializer.Serializer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ResponseDto {

  private int statusCode;

  private HttpStatus.Series series;

  private String reason;

  private Map<String, Object> record;

  public static ResponseDto toResponse(HttpStatus status, Map<String, Object> record){
    return new ResponseDto(status.value(), status.series(), status.getReasonPhrase(), record);
  }

  @Override
  public String toString() {
    return "ResponseDto{" +
        "statusCode=" + statusCode +
        ", series=" + series +
        ", reason='" + reason + '\'' +
        ", record=" + record +
        '}';
  }
}
