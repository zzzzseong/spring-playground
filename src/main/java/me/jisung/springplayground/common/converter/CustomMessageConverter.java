package me.jisung.springplayground.common.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.jisung.springplayground.common.util.JsonUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@Slf4j(topic = "CustomMessageConverter")
public class CustomMessageConverter extends AbstractHttpMessageConverter<Object> {

    public CustomMessageConverter() {
        super(MediaType.ALL);
    }

    // 여기서 Class, MediaType을 체크하여 해당 컨버터를 사용할지 결정합니다.
    @Override
    protected boolean supports(Class<?> clazz) {
        log.info("[1] CustomMessageConverter.supports() called");

        // 아래와 같이 별도의 조건 없이 true를 반환한다면, 모든 요청에 대해 이 컨버터가 사용될 것입니다. [테스트 용도]
        return true;
    }

    @Override
    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        log.info("[2] CustomMessageConverter.readInternal() called");

        ObjectMapper objectMapper = new ObjectMapper();

        InputStream is = inputMessage.getBody();

        byte[] bytes = is.readAllBytes();
        log.info("[2.1] Serialized InputMessage is {}", Arrays.toString(bytes));

        Object body = objectMapper.readValue(bytes, clazz);
        log.info("[2.2] Deserialized InputMessage is {}", JsonUtil.toJson(body));

        return body;
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        log.info("[3] CustomMessageConverter.writeInternal() called");

        log.info("[3.1] Deserialized OutputMessage is {}", JsonUtil.toJson(object));

        ObjectMapper objectMapper = new ObjectMapper();

        OutputStream os = outputMessage.getBody();

        objectMapper.writeValue(os, object);
        log.info("[3.2] Serialized OutputMessage is {}", Arrays.toString(os.toString().getBytes()));
    }
}
