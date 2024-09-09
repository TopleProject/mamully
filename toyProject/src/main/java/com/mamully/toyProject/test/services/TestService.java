package com.mamully.toyProject.test.services;

import com.mamully.toyProject.test.entities.Test;
import com.mamully.toyProject.test.mappers.TestMapper;
import com.mamully.toyProject.test.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestMapper testMapper;

    // 모든 Test 데이터를 조회하는 메서드
    public List<TestDTO> getAllTests() {
        return testMapper.findAllTests();
    }

    // 특정 ID로 Test 데이터를 조회하는 메서드
    public TestDTO getTestById(Long id) {
        TestDTO testDTO = testMapper.findTestById(id);
        if (testDTO == null) {
            throw new IllegalArgumentException("해당 ID의 데이터가 존재하지 않습니다 : " + id);
        }
        return testDTO;
    }

    /* Test Post 작업*/
    public TestDTO createTest(String content){

        Test test = new Test(content);
        Test savedTest = testRepository.save(test);
        return new TestDTO(savedTest.getContent());
    }

    public TestDTO updateTest(Long id, String content) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터가 존재하지 않습니다 : " + id));

        test.setContent(content);  // 업데이트된 content 설정
        Test updatedTest = testRepository.save(test);  // 업데이트 후 저장
        return new TestDTO(updatedTest.getContent());  // 업데이트된 DTO 반환
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
