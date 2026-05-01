package com.github.m4rcioliveira.financial_manager_v0002.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseBaseDTO <T> {

    private T data;

}
