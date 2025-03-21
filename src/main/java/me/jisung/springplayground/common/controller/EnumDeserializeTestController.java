package me.jisung.springplayground.common.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.annotation.ValidEnum;
import me.jisung.springplayground.common.util.JsonUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j(topic = "EnumDeserializeTestController")
public class EnumDeserializeTestController {

    /**
     * validator 적용 전
     * JSON parse error: Cannot deserialize value of type `me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code` from String \"D001\": not one of the values accepted for Enum class: [A001, C001, B001]
     *
     * validator 적용 후
     * JSON parse error: Cannot deserialize value of type `me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code` from String "D001": not one of the values accepted for Enum class: [A001, C001, B001]
     * */
    @PostMapping("/json")
    public String json(@Valid @RequestBody TestRequest request) {
        return JsonUtil.toJson(request);
    }

    /**
     * validator 적용 전
     * Failed to convert value of type 'java.lang.String' to required type 'me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code'; Failed to convert from type [java.lang.String] to type [me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code] for value [D001]
     *
     * validator 적용 후
     * Failed to convert value of type 'java.lang.String' to required type 'me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code'; Failed to convert from type [java.lang.String] to type [@me.jisung.springplayground.common.annotation.ValidEnum me.jisung.springplayground.common.controller.EnumDeserializeTestController$Code] for value [D001]
     * */
    @PostMapping("/form")
    public String form(@Valid @ModelAttribute TestRequest request) {
        return JsonUtil.toJson(request);
    }


    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    static class TestRequest {

        @NotNull(message = "이름은 필수 값 입니다.")
        @Pattern(regexp = "^[a-zA-Z]*$", message = "이름은 영문자만 가능합니다.")
        private final String name;

        @ValidEnum(enumClass = Code.class, message = "코드는 A001, B001, C001 중 하나여야 합니다.")
        private final Code code;
    }


    enum Code {
        A001, B001, C001;

        /*
         * JsonCreator 사용 시
         * application/json 타입의 요청은 일치하는 Code 가 없으면 null 처리하지만,
         * multipart/form-data 타입의 요청은 그대로 예외를 발생시킨다.
         * */
//        @JsonCreator
//        public static Code from(String val) {
//            return Arrays.stream(values())
//                    .filter(code -> code.name().equals(val))
//                    .findAny()
//                    .orElse(null);
//        }
    }


}
