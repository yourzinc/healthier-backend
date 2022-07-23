package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.question.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(@Param("id") String id);
    Optional<Question> findBySiteid(@Param("siteid") int site_id);
}
