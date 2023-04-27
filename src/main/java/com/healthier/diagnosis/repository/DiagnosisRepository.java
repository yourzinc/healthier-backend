package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.diagnosis.Diagnosis;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosisRepository extends MongoRepository<Diagnosis, String> {
    Optional<Diagnosis> findById(@Param("id") String id);
    Optional<Diagnosis> findByNewId(@Param("newId") int id);
}
