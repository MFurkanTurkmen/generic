package com.mft.uageneric.repository;

import com.mft.uageneric.entity.SozlesmeBilgi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SozlesmeBilgiRepository extends JpaRepository<SozlesmeBilgi,Long> {
    SozlesmeBilgi findByFaaliyetNo(String faaliyetNo);
}
