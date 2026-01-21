package com.edubot.backend;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsultationRepository extends MongoRepository<Consultation, String> {}
