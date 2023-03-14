package com.healthier.diagnosis.repository;

import com.healthier.diagnosis.domain.headache.Question;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeadacheQuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(@Param("id") int id);
    List<Question> findByType(@Param("type") String type);
    Optional<Question> findByPainSiteContainsAndIsFirst(@Param("pain_site") String painSite, @Param("is_first") Boolean isFirst);
}