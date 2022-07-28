package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.user.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DiagnosisLogRepository extends MongoRepository<Log, String> {

}
