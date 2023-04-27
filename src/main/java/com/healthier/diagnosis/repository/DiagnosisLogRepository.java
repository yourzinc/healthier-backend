package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.log.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosisLogRepository extends MongoRepository<Log, String> {

}
