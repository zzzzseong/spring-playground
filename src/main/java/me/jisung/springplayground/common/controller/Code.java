package me.jisung.springplayground.common.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Code {
    A001, B001, C001

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
