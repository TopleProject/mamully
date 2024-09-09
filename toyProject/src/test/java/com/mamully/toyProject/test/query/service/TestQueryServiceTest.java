package com.mamully.toyProject.test.query.service;

import com.mamully.toyProject.test.command.services.TestDTO;
import com.mamully.toyProject.test.query.mappers.TestMapper;
import com.mamully.toyProject.test.query.services.TestQueryService;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class TestQueryServiceTest {

    @Mock
    private TestMapper testMapper; // MyBatis 매퍼 모의 객체

    @InjectMocks
    private TestQueryService testQueryService;

    // 모든 Test 데이터를 조회하는 메서드 테스트
    @org.junit.jupiter.api.Test
    public void testGetAllTests() {
        // Mock 데이터를 설정
        List<TestDTO> mockTests = Arrays.asList(new TestDTO("Test 1"), new TestDTO("Test 2"));
        given(testMapper.findAllTests()).willReturn(mockTests);  // MyBatis 매퍼 모의 처리

        // 테스트 실행
        List<TestDTO> result = testQueryService.getAllTests();

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
        TestDTO result = testQueryService.getTestById(1L);

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
            testQueryService.getTestById(1L);
        });

        // 예외 메시지 검증
        assertEquals("해당 ID의 데이터가 존재하지 않습니다 : 1", exception.getMessage());
    }
}
