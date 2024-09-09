package com.mamully.toyProject.test.controllers;

import com.mamully.toyProject.test.services.TestDTO;
import com.mamully.toyProject.test.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private TestService testService;

    // 모든 Test 데이터를 조회
    @GetMapping
    public ResponseEntity<List<TestDTO>> getAllTests() {
        List<TestDTO> tests = testService.getAllTests();
        return ResponseEntity.ok(tests);
    }

    // 특정 ID로 Test 데이터를 조회
    @GetMapping("/{id}")
    public ResponseEntity<TestDTO> getTestById(@PathVariable Long id) {
        TestDTO test = testService.getTestById(id);
        return ResponseEntity.ok(test);
    }

    // POST
    @PostMapping
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO testDTO) {
        // 서비스 호출하여 테스트 생성
        TestDTO createdTestDTO = testService.createTest(testDTO.getContent());
        return ResponseEntity.ok(createdTestDTO);
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<TestDTO> updateTest(@PathVariable Long id, @RequestBody TestDTO testDTO) {
        TestDTO updatedTestDTO = testService.updateTest(id, testDTO.getContent());
        if (updatedTestDTO != null) {
            return ResponseEntity.ok(updatedTestDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        boolean isDeleted = testService.deleteTest(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
