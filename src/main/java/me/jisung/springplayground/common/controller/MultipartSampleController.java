package me.jisung.springplayground.common.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/multipart/sample")
public class MultipartSampleController {

    /** 방법 1: 개별 파라미터로 받기 */
    @PostMapping(value = "/1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sample1(
            @RequestParam(required = false) MultipartFile file1,
            @RequestParam(required = false) MultipartFile file2,
            @RequestParam(required = false) MultipartFile file3,
            @RequestParam(required = false) MultipartFile file4,
            @RequestParam(required = false) MultipartFile file5
    ) {
        // 각 파일을 개별 파라미터로 받는 가장 단순한 방법

        // 장점: 직관적이고 이해하기 쉬음, @RequestParam(required=true/false)로 필수/선택 지정 가능
        // 단점: 파라미터로 받는 파일 갯수가 늘어날수록 보일러 플레이트 코드 증가, 유지보수 어려움
    }


    /** 방법 2: Map으로 받기 */
    @PostMapping(value = "/2", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sample2(@RequestParam Map<String, MultipartFile> files) {
        // 서로 다른 이름의 파일들을 Map으로 한 번에 받는 방법

        // 장점: 다양한 이름의 파일을 한 번에 받을 수 있음, 파일명(key)으로 직접 접근 가능, 파라미터 고정 가능
        // 단점: 같은 이름의 파일이 여러 개면 마지막 것만 저장, @RequestParam 옵션 사용 불가, 수동 null 체크

        // 클라이언트 요청 예시
        // curl --location 'http://localhost:8080/multipart/sample/2' \
        // --form 'image1=@"image1.jpg"' \
        // --form 'image2=@"image2.jpg"' \
        // --form 'document=@"document.pdf"'
    }


    /** 방법 3: 배열 또는 리스트로 받기 */
    @PostMapping(value = "/3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sample3(
            @RequestParam(value = "file", required = false) MultipartFile[] fileArray,
            @RequestParam(value = "file", required = false) MultipartFile[] fileList
    ) {
        // 같은 파라미터명의 파일들을 배열 또는 리스트로 받는 방법

        // 장점: 같은 이름의 파일 여러 개를 한 번에 받을 수 있음, @RequestParam 옵션 사용 가능
        // 단점: 반드시 같은 파라미터명이어야 함, @RequestParam 옵션은 배열 자체만 체크, 최소/최대 길이 제한 불가

        // 클라이언트 요청 예시
        // curl --location 'http://localhost:8080/multipart/sample/3' \
        // --form 'file=@"file1.jpg"' \
        // --form 'file=@"file2.jpg"' \
        // --form 'file=@"file3.jpg"'
    }

    /** 방법 4: MultiValueMap 사용 */
    @PostMapping(value = "/5", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sample5(@RequestParam MultiValueMap<String, Object> formData) {
        // 파일과 다른 폼 데이터를 함께 받을 수 있는 가장 유연한 방법. 모든 multipart 데이터를 한 번에 처리할 수 있음.

        // 장점: 같은 이름의 파일도 모두 받을 수 있음, 다른 폼 데이터(텍스트 필드 등)도 함께 받을 수 있음, 모든 multipart 데이터를 한 번에 처리 가능
        // 단점:
        // 타입 안정성 없음 - 모든 값이 Object로 반환되어 타입 캐스팅 필요
        // 유효성 검증 불가 - @RequestParam(required), @NotNull, @Size 등 모두 사용 불가
        // 모든 검증을 수동으로 해야 함 (타입 체크, null 체크, 크기 체크 등)
        // 코드 가독성 저하

        // 클라이언트 요청 예시
        // curl --location 'http://localhost:8080/multipart/sample/5' \
        // --form 'title="제목"' \
        // --form 'description="설명"' \
        // --form 'files=@"file1.jpg"' \
        // --form 'files=@"file2.jpg"' \
        // --form 'image=@"image.jpg"'
    }


    /** 방법 6: DTO 객체와 @ModelAttribute 사용 */
    @PostMapping(value = "/6", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void sample6(@Valid @ModelAttribute FileUploadRequest request) {
        // 파일과 다른 폼 데이터를 DTO 객체로 받는 가장 깔끔하고 강력한 방법

        // 장점
        // 타입 안정성 확보 - 컴파일 타임에 타입 체크 가능
        // 완벽한 유효성 검증 - @Valid와 Bean Validation(@NotNull, @NotEmpty, @Size 등) 사용 가능
        // 필드별 세밀한 제어 - 각 필드마다 개별적으로 검증 규칙 적용 가능
        // 필수/선택 구분 가능 - @NotNull으로 필수 필드 지정, 없으면 선택 필드
        // 최소/최대 개수 제한 가능 - @Size(min=1, max=10) 등으로 제한 가능
        // 코드 가독성 향상 - 의미 있는 객체명과 필드명으로 이해하기 쉬움
        // 재사용 가능 - DTO를 여러 엔드포인트에서 재사용 가능
        // 테스트 작성 용이 - 명확한 구조로 테스트 코드 작성이 쉬움

        // 주의사항: DTO에 기본 생성자 필요, 필드에 Setter 필요

        // 클라이언트 요청 예시
        // curl --location 'http://localhost:8080/multipart/sample/6' \
        // --form 'title="제목"' \
        // --form 'description="설명"' \
        // --form 'files=@"file1.jpg"' \
        // --form 'files=@"file2.jpg"' \
        // --form 'thumbnail=@"thumbnail.jpg"'
    }



    // 파일 업로드용 DTO 클래스
    // DTO 방식의 유효성 검증 예시: @NotNull(필수 필드), @NotEmpty(null이 아니고 비어있지 않아야 함), @Size(최소/최대 크기 지정)
    // 배열/Map/List 방식과 달리 각 필드별로 세밀한 제어가 가능
    @Data
    public static class FileUploadRequest {
        
        // 필수 필드 - 제목
        @NotNull(message = "제목은 필수입니다.")
        @NotEmpty(message = "제목은 비어있을 수 없습니다.")
        private String title;
        
        // 선택 필드 - 설명 (검증 어노테이션 없음)
        private String description;
        
        // 필수 필드 - 파일 리스트 (최소 1개, 최대 10개)
        @NotEmpty(message = "최소 1개 이상의 파일이 필요합니다.")
        @Size(min = 1, max = 10, message = "파일은 1개 이상 10개 이하여야 합니다.")
        private List<MultipartFile> files;
        
        // 선택 필드 - 썸네일 (검증 어노테이션 없음)
        private MultipartFile thumbnail;
    }
}
