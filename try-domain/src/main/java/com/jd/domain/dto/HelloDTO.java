package com.jd.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HelloDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private String name;
    private String msg;
}