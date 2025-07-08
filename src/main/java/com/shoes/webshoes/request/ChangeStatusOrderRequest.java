package com.shoes.webshoes.request;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class ChangeStatusOrderRequest {
    @Min(value = 1 , message ="status not null")
	private int status;
}
