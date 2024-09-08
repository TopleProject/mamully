package com.mamully.toyProject.test.services;

import com.mamully.toyProject.test.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    public TestDTO createTest(String content){
        Test test = new Test(content);
        Test savedTest = testRepository.save(test);
    }


}
