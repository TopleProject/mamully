package com.mamully.toyProject.test.services;

import com.mamully.toyProject.test.entities.Test;
import com.mamully.toyProject.test.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    /* Test Post 작업*/
    public TestDTO createTest(String content){

        Test test = new Test(content);
        Test savedTest = testRepository.save(test);
        return new TestDTO(savedTest.getContent());
    }

    /* Test Update 작업*/
    public TestDTO updateTest(Long id, String content) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID 값이 없습니다 : " + id));

        test.setContent(content);  // Update the content
        Test updatedTest = testRepository.save(test);  // Save the updated entity
        return new TestDTO(updatedTest.getContent());  // Return the updated DTO
    }

    // Test Delete
    public boolean deleteTest(Long id) {
        if (testRepository.existsById(id)) {
            testRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}
