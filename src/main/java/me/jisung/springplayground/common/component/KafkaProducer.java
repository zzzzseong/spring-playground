package me.jisung.springplayground.common.component;

public interface KafkaProducer {

    /**
     * 지정된 토픽에 메시지를 동기적으로 전송.
     * @param topic       메시지를 전송할 토픽 이름
     * @param partitionNo 메시지를 전송할 파티션 번호 (nullable, 자동 할당)
     * @param key         메시지의 해시 키 (nullable, 키 없이 전송)
     * @param message     전송할 메시지 내용
     */
    void produce(String topic, Integer partitionNo, String key, String message);

    /**
     * 지정된 토픽에 메시지를 비동기적으로 전송.
     * @param topic       메시지를 전송할 토픽 이름
     * @param partitionNo 메시지를 전송할 파티션 번호 (nullable, 자동 할당)
     * @param key         메시지의 해시 키 (nullable, 키 없이 전송)
     * @param message     전송할 메시지 내용
     */
    void produceAsync(String topic, Integer partitionNo, String key, String message);

}
