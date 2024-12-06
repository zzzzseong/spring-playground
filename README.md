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
spring.datasource.url=should_override
spring.datasource.username=should_override
spring.datasource.password=should_override
```

application-local.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/spring_playground
spring.datasource.username=root
spring.datasource.password=1234
```

# Build
```bash
./gradlew clean build ${options}
```
- -Dorg.gradle.java.home=${java_home}:  특정 JDK 버전으로 빌드
- -Dspring.profiles.active=${profile}: 특정 프로파일로 빌드
