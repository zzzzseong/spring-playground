package me.jisung.springplayground.common.controller;

import static me.jisung.springplayground.common.entity.ApiResponse.success;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.entity.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MessageConverter 테스트를 위한 Controller
 *
 * <p>
 * 테스트를 위해 {@link me.jisung.springplayground.common.config.WebMvcConfig}에 {@link me.jisung.springplayground.common.converter.CustomMessageConverter}를 추가해야 합니다.
 * </p>
 * */
@RestController
@RequestMapping("/common/converter")
@Slf4j(topic = "MessageConverterController")
public class MessageConverterController {



    @PostMapping(value = "/json", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<DTO> json(@RequestBody DTO dto) {
        return success(dto);
    }


    /**
     * multipart/form-data 요청을 처리하며, 폼 필드 값을 {@code @ModelAttribute}를 통해 DTO에 바인딩합니다.
     *
     * <p><b>📌 multipart/form-data 요청에서 {@code HttpMessageConverter}가 동작하지 않는 이유</b></p>
     *
     * <p>JSON 또는 텍스트 기반 요청은 {@link org.springframework.http.converter.HttpMessageConverter}를 통해 처리되지만,
     * multipart/form-data 요청은 Spring의 {@code WebDataBinder}를 통해 처리됩니다.
     * 이는 multipart 요청이 일반 텍스트와 바이너리 파일 등 다양한 파트를 포함하기 때문에 처리 방식이 다르기 때문입니다.</p>
     *
     * <ol>
     *     <li>{@code multipart/form-data}는 텍스트 필드, 파일 업로드 등 다양한 파트로 구성됩니다.</li>
     *     <li>Spring은 {@code MultipartResolver}를 통해 multipart 요청을 먼저 파싱합니다.</li>
     *     <li>각 파트는 {@code HttpServletRequest.getParameter(...)}와 유사하게 접근할 수 있습니다.</li>
     *     <li>파싱된 값은 {@code WebDataBinder}를 통해 DTO에 바인딩되므로, {@code HttpMessageConverter}가 개입하지 않습니다.</li>
     * </ol>
     */
    @PostMapping(value = "/form", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<DTO> form(@ModelAttribute DTO dto) {
        return success(dto);
    }


    /**
     * 📦 Jackson 역직렬화를 위한 DTO 구성 설명
     *
     * <h3>🔁 Jackson 역직렬화 방식</h3>
     * <ul>
     *     <li><b>방식 1:</b> 기본 생성자 + Setter 사용</li>
     *     <li><b>방식 2:</b> 기본 생성자 + Getter + Reflection 사용</li>
     *     <li><b>방식 3:</b> @JsonCreator + @JsonProperty 를 통한 생성자 기반 매핑</li>
     * </ul>
     *
     * <h3>❗ 기본 생성자가 반드시 필요한 이유</h3>
     * <ul>
     *     <li>Jackson은 역직렬화 시 <code>new DTO()</code>로 객체를 생성합니다.</li>
     *     <li>따라서, 기본 생성자가 없다면 Jackson은 객체를 생성할 수 없고, 필드 값을 설정할 수 없습니다.</li>
     *     <li>클래스 내에 생성자가 하나도 없다면 Java는 기본 생성자를 자동으로 추가합니다.</li>
     *     <li>그러나, 클래스 내에 생성자가 하나라도 있다면 Java는 기본 생성자를 자동으로 추가하지 않으므로 주의해야 합니다.</li>
     * </ul>
     *
     * <h3>❗ Getter 또는 필드 접근 설정이 필요한 이유</h3>
     * <ul>
     *     <li>기본적으로 Jackson은 <b>Getter 또는 public 필드</b>를 통해 필드를 인식합니다.</li>
     *     <li>Getter가 없고 필드가 private이면, Jackson의 기본 설정상 해당 필드는 무시됩니다.</li>
     *     <li>필드 접근을 허용하려면 ObjectMapper에 <code>setVisibility(PropertyAccessor.FIELD, Visibility.ANY)</code> 설정이 필요합니다.</li>
     *     <li>매핑 시 Setter를 사용한다면 Getter가 없어도 괜찮습니다.</li>
     * </ul>
     *
     * <h3>⚙️ ObjectMapper의 기본 프로퍼티 접근 전략</h3>
     * <pre>{@code
     * new VisibilityChecker.Std(
     *     Visibility.PUBLIC_ONLY,  // getter
     *     Visibility.PUBLIC_ONLY,  // is-getter
     *     Visibility.ANY,          // field
     *     Visibility.ANY,          // creator
     *     Visibility.PUBLIC_ONLY   // setter
     * );
     * }</pre>
     */
    //@Setter
    @Getter
    @NoArgsConstructor
    public static class DTO {
        private String username;
        private String password;

        public DTO(String username) {
            this.username = username;
        }
        public DTO(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }



}
