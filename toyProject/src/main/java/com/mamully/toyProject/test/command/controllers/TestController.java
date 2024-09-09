package com.mamully.toyProject.test.command.controllers;

import com.mamully.toyProject.test.command.services.TestDTO;
import com.mamully.toyProject.test.command.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private TestService testService;

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
