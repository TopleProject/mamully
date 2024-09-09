package com.mamully.toyProject.test.service;

import com.mamully.toyProject.test.entities.Test;
import com.mamully.toyProject.test.mappers.TestMapper;
import com.mamully.toyProject.test.repositories.TestRepository;
import com.mamully.toyProject.test.services.TestDTO;
import com.mamully.toyProject.test.services.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

public class TestServiceTest {

    @Mock
    private TestRepository testRepository;  // JPA 레포지토리 모의 객체

    @Mock
    private TestMapper testMapper;  // MyBatis 매퍼 모의 객체

    @InjectMocks
    private TestService testService;  // 실제로 테스트할 서비스 객체

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);  // 모의 객체 초기화
    }

    // 모든 Test 데이터를 조회하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testGetAllTests() {
        // Mock 데이터를 설정
        List<TestDTO> mockTests = Arrays.asList(new TestDTO("Test 1"), new TestDTO("Test 2"));
        given(testMapper.findAllTests()).willReturn(mockTests);  // MyBatis 매퍼 모의 처리

        // 테스트 실행
        List<TestDTO> result = testService.getAllTests();

        // 결과 검증
        assertEquals(2, result.size());
        assertEquals("Test 1", result.get(0).getContent());
        assertEquals("Test 2", result.get(1).getContent());
    }

    // 특정 ID로 Test 데이터를 조회하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testGetTestById() {
        // Mock 데이터를 설정
        TestDTO mockTest = new TestDTO("Test 1");
        given(testMapper.findTestById(1L)).willReturn(mockTest);  // MyBatis 매퍼 모의 처리

        // 테스트 실행
        TestDTO result = testService.getTestById(1L);

        // 결과 검증
        assertNotNull(result);
        assertEquals("Test 1", result.getContent());
    }

    @org.junit.jupiter.api.Test
    public void testGetTestById_NotFound() {
        // MyBatis 매퍼에서 null을 반환하도록 설정
        given(testMapper.findTestById(1L)).willReturn(null);

        // 테스트 실행 및 예외 발생 검증
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testService.getTestById(1L);
        });

        // 예외 메시지 검증
        assertEquals("해당 ID의 데이터가 존재하지 않습니다 : 1", exception.getMessage());
    }

    // 새로운 Test 데이터를 생성하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testCreateTest() {
        // Mock 데이터를 설정
        Test mockTest = new Test("New Test");
        given(testRepository.save(mockTest)).willReturn(mockTest);  // JPA 저장 모의 처리

        // 테스트 실행
        TestDTO result = testService.createTest("New Test");

        // 결과 검증
        assertNotNull(result);
        assertEquals("New Test", result.getContent());
    }

    // Test 데이터를 업데이트하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testUpdateTest() {
        // Mock 데이터를 설정
        Test mockTest = new Test("Original Test");
        given(testRepository.findById(1L)).willReturn(Optional.of(mockTest));  // JPA 조회 모의 처리
        mockTest.setContent("Updated Test");
        given(testRepository.save(mockTest)).willReturn(mockTest);  // JPA 업데이트 모의 처리

        // 테스트 실행
        TestDTO result = testService.updateTest(1L, "Updated Test");

        // 결과 검증
        assertNotNull(result);
        assertEquals("Updated Test", result.getContent());
    }

    @org.junit.jupiter.api.Test
    public void testUpdateTest_NotFound() {
        // JPA에서 ID 조회 시 데이터가 없을 때 예외 발생을 모의 처리
        given(testRepository.findById(1L)).willReturn(Optional.empty());

        // 테스트 실행 및 예외 발생 검증
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            testService.updateTest(1L, "Updated Test");
        });

        // 예외 메시지 검증
        assertEquals("해당 ID의 데이터가 존재하지 않습니다 : 1", exception.getMessage());
    }

    // Test 데이터를 삭제하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testDeleteTest() {
        // Mock 설정
        given(testRepository.existsById(1L)).willReturn(true);  // 존재 여부 모의 처리
        doNothing().when(testRepository).deleteById(1L);  // 삭제 모의 처리

        // 테스트 실행
        boolean result = testService.deleteTest(1L);

        // 결과 검증
        assertTrue(result);
    }

    @org.junit.jupiter.api.Test
    public void testDeleteTest_NotFound() {
        // 존재하지 않는 ID에 대한 삭제 테스트
        given(testRepository.existsById(1L)).willReturn(false);  // 존재하지 않음 모의 처리

        // 테스트 실행
        boolean result = testService.deleteTest(1L);

        // 결과 검증
        assertFalse(result);
    }
}
