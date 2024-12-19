package com.mft.uageneric.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class SozlesmeBilgiKolonRS {
    private String label;
    private String code;
    private String type;
    private String hidden;
    private String category;
    private Object value;
    private String entity;
    private Object options;
}
