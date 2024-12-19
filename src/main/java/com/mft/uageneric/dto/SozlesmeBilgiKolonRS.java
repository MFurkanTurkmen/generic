package com.mft.uageneric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SozlesmeBilgiKolonRS {
    private String label;
    private String code;
    private String type;
    private String hidden;
    private String category;
    private Object value;
    private String entity;
    private List<Map<String, Object>> options;
}
