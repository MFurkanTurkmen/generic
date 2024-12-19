package com.mft.uageneric.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SozlesmeBilgi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sozlesmeNo;
    private String faaliyetNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sbfIl")
    private Il sbfIl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sbfIlce")
    private Ilce sbfIlce;
}
