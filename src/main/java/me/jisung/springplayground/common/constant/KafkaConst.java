package me.jisung.springplayground.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaConst {

    /* 카프카 토픽 관련 */
    public static final String TOPIC_NAME_CHAT = "CHAT-MESSAGE";

}
