package com.mamully.toyProject.test.command.controllers;

import com.mamully.toyProject.common.s3.FileUploadUtil;
import com.mamully.toyProject.test.command.services.TestDTO;
import com.mamully.toyProject.test.command.services.TestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private FileUploadUtil fileUploadUtil;

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

    /* 파일 업로드 테스트 컨트롤러 */
    /* consumes 속성을 통해 해당 요청을 json type에서 MultipartFormDataValue 속성으로 바꿔준다.
    *  이 속성을 사용하지 않으면 swagger에서 json type으로 값을 받아오려고 함 */
    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "파일 업로드", description = "S3에 파일을 업로드하고 URL을 반환합니다.")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestPart("file") MultipartFile file) {
        try {
            // 파일 업로드 후 S3 URL 반환 (확장자 검증도 내부에서 수행)
            String fileUrl = fileUploadUtil.uploadFile(file);

            // URL을 JSON 형태로 반환
            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 확장자 검증 실패 시 처리
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (IOException e) {
            // 파일 업로드 중 오류 발생 시 처리
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
