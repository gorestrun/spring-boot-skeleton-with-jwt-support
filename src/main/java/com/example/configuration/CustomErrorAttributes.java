package com.example.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import com.example.dto.ResponseDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		 
        Map<String, Object> map = super.getErrorAttributes(webRequest, options);
        String firstDigit = map.get("status").toString().substring(0,1);
        
        //1xx 3xx 4xx 5xx
        if(!firstDigit.equals("2")) {
        	return null;
        } else {
        	ResponseDto responseDto = new ResponseDto();
        	//Set the response code
	        
	        TypeReference<Map<String, Object>> type = new TypeReference<Map<String, Object>>(){};
	        
	        return objectMapper.convertValue(responseDto, type);
        }
    }
}