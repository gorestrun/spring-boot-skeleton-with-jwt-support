package com.example.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({"status", "type", "id", "dateTime", "message", "code", "data"})
public class ResponseDto implements Serializable {

	private static final long serialVersionUID = -7377277424125144224L;
	
	private String status; //success, fail or error
	
	@JsonProperty("responseType")
	private String type;
	
	@JsonProperty("responseId")
	private String id;
	
	@JsonProperty("responseDateTime")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime dateTime; //LocalDateTime.now()
	private Map<String, Object> data;
	private String message;
	private String code;
}
