package me.jisung.springplayground.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] allowedMethods = {"GET", "POST", "PUT", "PATCH", "DELETE"};

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods(allowedMethods)
            .allowedHeaders("*")
            .allowCredentials(true);
    }

    /**
     * 사용자 정의 {@link HttpMessageConverter}를 등록합니다.
     *
     * <p>
     * 이 메서드는 전달받은 {@code converters} 리스트에 사용자 정의 {@link HttpMessageConverter}를 추가합니다.
     * 리스트의 인덱스 순서에 따라 {@code HttpMessageConverter}의 스캔 우선순위가 결정됩니다.
     * </p>
     *
     * <p>
     * Spring에서 기본적으로 등록하는 {@code HttpMessageConverter}의 순서는 다음과 같습니다:
     * </p>
     *
     * <ol>
     *     <li>ByteArrayHttpMessageConverter</li>
     *     <li>StringHttpMessageConverter</li>
     *     <li>StringHttpMessageConverter</li>
     *     <li>ResourceHttpMessageConverter</li>
     *     <li>ResourceRegionHttpMessageConverter</li>
     *     <li>AllEncompassingFormHttpMessageConverter</li>
     *     <li>MappingJackson2HttpMessageConverter</li>
     *     <li>MappingJackson2HttpMessageConverter</li>
     *     <li>Jaxb2RootElementHttpMessageConverter</li>
     * </ol>
     *
     * <p>
     * 이러한 기본 컨버터는 아래 클래스 및 메서드에서 확인할 수 있습니다:
     * </p>
     *
     * <ul>
     *     <li>{@link org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport#requestMappingHandlerAdapter}</li>
     *     <li>{@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter#setMessageConverters}</li>
     * </ul>
     *
     * @param converters Spring에 의해 기본적으로 등록된 {@code HttpMessageConverter} 목록
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 가장 앞에 추가
//        converters.add(0, new CustomMessageConverter());

        // 가장 뒤에 추가
//        converters.add(new CustomMessageConverter());
    }
}
