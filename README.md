# spring-playground

---

# Profile Setting

---

application-${Active profiles}.properties
Run -> Edit Configurations -> Active profiles setting

application.properties
```properties
server.port=8182
server.shutdown=graceful

spring.application.name=spring-playground

# docker mysql
spring.jpa.hibernate.ddl-auto=validate
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=override
spring.datasource.username=override
spring.datasource.password=override
```

application-local.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_playground
spring.datasource.username=playground
spring.datasource.password=playground
```

# Build
```bash
./gradlew clean build ${options}
```
- -Dorg.gradle.java.home=${java_home}: 특정 JDK 버전으로 빌드
- -Dspring.profiles.active=${profile}: 특정 프로파일로 빌드

# Run
```bash
# foreground
java -jar playground.jar --spring.profiles.active=${profile} --jasypt.encryptor.password=${password}

# background
nohup java -jar playground.jar --spring.profiles.active=${profile} --jasypt.encryptor.password=${password} > /dev/null 2>&1 &
```

# Spring Actuator

## 설정

application.properties에 Actuator 설정 추가:
```properties
# Actuator 기본 경로 설정
management.endpoints.web.base-path=/actuator
management.endpoint.health.show-details=when-authorized

# 노출할 엔드포인트 설정
management.endpoints.web.exposure.include=health,info,metrics,env,beans
# 또는 모든 엔드포인트 노출 (운영 환경에서는 권장하지 않음)
# management.endpoints.web.exposure.include=*

# Health 체크 상세 정보 표시
management.endpoint.health.show-components=always
```

## 주요 엔드포인트

### Health Check
```bash
curl http://localhost:9000/actuator/health
```

### Application Info
```bash
# 애플리케이션 정보
curl http://localhost:9000/actuator/info
```

### Metrics
```bash
# 모든 메트릭 조회
curl http://localhost:9000/actuator/metrics

# 특정 메트릭 조회 (예: jvm.memory.used)
curl http://localhost:9000/actuator/metrics/jvm.memory.used
```

### Environment
```bash
# 환경 변수 및 설정 정보
curl http://localhost:9000/actuator/env
```

### Beans
```bash
# Spring Bean 목록
curl http://localhost:9000/actuator/beans
```

### 사용 가능한 모든 엔드포인트 목록
```bash
# 모든 엔드포인트 목록 조회
curl http://localhost:9000/actuator
```

## 보안 설정

**참고**: `/actuator/health` 엔드포인트는 Spring Boot의 기본 동작으로 Spring Security가 활성화되어 있어도 **별도 설정 없이 접근 가능**합니다.

다른 Actuator 엔드포인트들(`info`, `metrics`, `env`, `beans` 등)에 접근하려면 Spring Security 설정이 필요합니다.

현재 프로젝트에서는 `SecurityConfig`의 `publicEndPoints()`에 `/actuator/**` 경로가 추가되어 모든 Actuator 엔드포인트에 접근 가능합니다:

```69:78:src/main/java/me/jisung/springplayground/common/config/SecurityConfig.java
    private RequestMatcher publicEndPoints() {
        return new OrRequestMatcher(
            new AntPathRequestMatcher("/api/**"),

            /* websocket */
            new AntPathRequestMatcher("/ws/chat"),

            /* actuator */
            new AntPathRequestMatcher("/actuator/**")
        );
    }
```

운영 환경에서는 보안을 위해 health만 public으로 두고 나머지 엔드포인트는 인증을 추가하는 것을 권장합니다:

```java
// 예시: health는 기본적으로 public이므로 별도 설정 불필요
// 나머지 엔드포인트만 필요시 추가
private RequestMatcher publicEndPoints() {
    return new OrRequestMatcher(
        new AntPathRequestMatcher("/api/**"),
        new AntPathRequestMatcher("/ws/chat")
        // /actuator/health는 자동으로 허용됨
    );
}
```

## 주요 사용 사례

- **헬스 체크**: Kubernetes, Docker 등의 오케스트레이션 도구에서 애플리케이션 상태 확인
- **모니터링**: 메트릭 수집 및 모니터링 시스템 연동 (Prometheus, Grafana 등)
- **디버깅**: 환경 변수, Bean 정보 등 런타임 정보 확인