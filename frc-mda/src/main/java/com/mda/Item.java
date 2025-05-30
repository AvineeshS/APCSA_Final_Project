package com.mda;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.ObjectMapper;

public record Item(String title, BigDecimal price, String location, String url) {
@SuppressWarnings("unused")
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    


}


