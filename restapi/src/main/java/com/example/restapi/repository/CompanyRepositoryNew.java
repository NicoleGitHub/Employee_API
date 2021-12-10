package com.example.restapi.repository;

import com.example.restapi.object.entity.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepositoryNew extends MongoRepository<Company, String> {


}
