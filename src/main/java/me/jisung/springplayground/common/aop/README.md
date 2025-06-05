# SpringAOP 어노테이션

Spring AOP(AspectOrientedProgramming)에서 사용되는 주요 어노테이션들에 대한 설명입니다.

## @Aspect
- 해당 클래스가 AOP 클래스임을 나타냅니다.
- 보통 `@Component`와 함께 사용하여 스프링 빈으로 등록합니다.

```java
@Aspect
@Component
public class LoggingAspect {
    // Aspect 내용
}
```

## @Pointcut
- AOP가 적용될 메소드 패턴을 정의합니다.
- 정규 표현식 형태로 메소드를 지정할 수 있습니다.

```java
@Pointcut("execution(* me.jisung.springplayground..controller..*(..))")
public void controllers() {}
```

## @Around
- 메소드 실행 전후에 로직을 수행할 수 있는 가장 강력한 어드바이스입니다.
- `ProceedingJoinPoint`를 매개변수로 받아 대상 메소드 실행을 직접 제어합니다.
- 대상 메소드 실행 전후 처리, 반환값 변경, 예외 처리 등 모든 것을 제어할 수 있습니다.

```java
@Around("controllers()")
public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
    // 메소드 실행 전 처리
    Object result = joinPoint.proceed(); // 대상 메소드 실행
    // 메소드 실행 후 처리
    return result;
}
```

## @Before
- 대상 메소드 실행 전에 실행되는 어드바이스를 정의합니다.
- 메소드 실행을 막을 수는 없습니다.

```java
@Before("controllers()")
public void beforeMethod(JoinPoint joinPoint) {
    // 메소드 실행 전 처리
}
```

## @After
- 대상 메소드 실행 후(성공/예외 상관없이) 항상 실행됩니다.
- try-finally 블록의 finally와 유사합니다.

```java
@After("controllers()")
public void afterMethod(JoinPoint joinPoint) {
    // 메소드 실행 후 처리
}
```

## @AfterReturning
- 대상 메소드가 정상적으로 값을 반환한 후 실행됩니다.
- 반환값에 접근할 수 있지만 변경할 수는 없습니다.

```java
@AfterReturning(pointcut = "controllers()", returning = "result")
public void afterReturning(JoinPoint joinPoint, Object result) {
    // 메소드가 성공적으로 실행된 후 처리
}
```

## @AfterThrowing
- 대상 메소드에서 예외가 발생했을 때 실행됩니다.
- 발생한 예외에 접근할 수 있습니다.

```java
@AfterThrowing(pointcut = "controllers()", throwing = "ex")
public void afterThrowing(JoinPoint joinPoint, Exception ex) {
    // 메소드 실행 중 예외 발생 시 처리
}
```
