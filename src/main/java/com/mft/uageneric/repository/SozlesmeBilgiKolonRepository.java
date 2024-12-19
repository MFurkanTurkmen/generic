package com.mft.uageneric.repository;

import com.mft.uageneric.entity.SozlesmeBilgiKolon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SozlesmeBilgiKolonRepository extends JpaRepository<SozlesmeBilgiKolon,Long> {
    SozlesmeBilgiKolon findByFaaliyetNo(String faaliyetNo);
}
