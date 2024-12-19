package com.mft.uageneric.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SozlesmeBilgiKolon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String faaliyetNo;
    @Column(length = 5000)
    private String kolonlar;

}
