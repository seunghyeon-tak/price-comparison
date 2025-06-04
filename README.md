# 실시간 상품 가격 비교 서비스

이 프로젝트는 여러 쇼핑몰에서 상품 가격을 크롤링하여 사용자가 최저가를 쉽게 찾을 수 있도록 돕는 서비스입니다.

## 기술 스택

- **Backend:** Spring Boot 3.3.9, Java 17, MySQL 8.0, Redis, JPA
- **Deployment:** AWS EC2, RDS, S3, Docker
- **Etc:** Swagger (API 문서화)

---

## 프로젝트 구조

```bash
├── src
│   ├── main
│   │   ├── java/com/github/seunghyeon_tak/price_comparison
│   │   ├── api
│   │   │   ├── business         # API 흐름을 조율하는 비지니스 로직 조합 계층
│   │   │   ├── controller       # 클라이언트 요청을 처리하는 REST 컨트롤러
│   │   │   ├── converter        # DTO <-> Entity 간 변환 책임을 담당 
│   │   │   ├── service          # 도메인 중심 로직 및 트랜잭션 처리 계층
│   │   ├── common
│   │   │   ├── annotation       # 커스텀 애노테이션 정의 (@Business, @LogException 등)
│   │   │   ├── aop              # 공통 AOP 로직 정의 (로깅, 예외처리 등)
│   │   │   ├── dto
│   │   │   │   ├── api
│   │   │   │   │   ├── request    # 공통 API 요청 DTO 정의
│   │   │   │   │   ├── response   # 공통 API 응답 DTO 정의
│   │   │   ├── exception
│   │   │   │   ├── response       # 예외 응답 형식 정의 클래스
│   │   │   │   │   ├── enums
│   │   │   │   │   │   ├── auth          # 인증 관련 에러 코드 Enum  (프로젝트 내 에러 코드 2000~)
│   │   │   │   │   │   ├── base          # 공통 에러 코드 Enum
│   │   │   │   │   │   ├── price_alert   # 가격 알림 관련 에러 코드 Enum  (프로젝트 내 에러 코드 6000~)
│   │   │   │   │   │   ├── product       # 상품 관련 에러 코드 Enum (프로젝트 내 에러 코드 3000~)
│   │   │   │   │   │   ├── store         # 스토어 관련 에러 코드 Enum (프로젝트 내 에러 코드 4000~)
│   │   │   │   │   │   ├── user          # 사용자 관련 에러 코드 Enum (프로젝트 내 에러 코드 1000~, 5000~)
│   │   │   ├── logging
│   │   │   │   ├── filter         # 서블릿 필터 기반 로깅 처리
│   │   │   │   ├── interceptor    # 스프링 인터셉터 기반 로깅 처리
│   │   │   ├── security
│   │   │   │   ├── jwt
│   │   │   │   │   ├── filter     # JWT 인증 필터
│   │   │   │   │   ├── handler    # JWT 인증 실패/성공 핸들러
│   │   │   ├── util               # 공통 유틸리티 클래스
│   │   ├── config                 # 전역 설정 클래스
│   │   ├── core
│   │   │   ├── alert              # 알림 전송 로직 구성 요소
│   │   │   ├── notification       # 알림 전송 (이메일, 푸시 등)
│   │   │   ├── redis              # Redis 관련 구성 및 유틸
│   │   │   ├── scheduler          # 스케줄링 작업 정의
│   │   ├── db
│   │   │   ├── domain             # JPA 엔티티 클래스
│   │   │   ├── enums              # 공통/도메인별 Enum 클래스
│   │   │   ├── repository         # JPA Repository 인터페이스
│   │   ├── external
│   │   │   ├── kakao
│   │   │   │   ├── dto            # 카카오 API 연동용 DTO
│   │   ├── resources
│   │   │   ├── db
│   │   │   │   ├── migration     # flyway migration SQL 파일
│   │   │   ├── application.yml   # 환경 설정 파일 (git에 포함되지 않음)
│   ├── test                      # 테스트 코드
├── .gitignore
├── README.md                     # 프로젝트 설명 문서
├── build.gradle                  # gradle 의존성 및 빌드 설정
```

## 프로젝트 환경 변수 설정 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/[DBNAME]?serverTimezone=UTC&characterEncoding=UTF-8
    username: [DB_USERNAME]
    password: [DB_PASSWORD]
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    hibernate:
      ddl-auto: update
    show-sql: true

  data:
    redis:
      host: localhost
      port: 6379
      cache:
        product-price-ttl-minutes: 10


  devtools:
    restart:
      enabled: false

  flyway:
    enabled: true
    baseline-on-migrate: false
    locations: classpath:db/migration
    validate-on-migrate: false
    clean-disabled: false

management:
  endpoints:
    web:
      exposure:
        include: "health"
  endpoint:
    health:
      show-details: always

logging:
  level:
    org.hibernate.sql: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.flywaydb.core.internal.command: DEBUG
    org.flywaydb.core.internal.database: DEBUG
    org.flywaydb.core.internal.sqlscript: DEBUG

jwt:
  secret: "..."
  access-token-validity-in-ms: 3600000  # 1시간
  refresh-token-validity-in-ms: 604800000  # 7일

kakao:
  client-id: [KAKAO_CLIENT_ID]
  redirect-uri: http://localhost:8080/auth/kakao/callback
  token-uri: https://kauth.kakao.com/oauth/token
  user-info-uri: https://kapi.kakao.com/v2/user/me

```

## 프로젝트 실행 방법

```bash
# git clone
git clone https://github.com/seunghyeon-tak/price-comparison.git
cd project_name

# gradle 빌드 및 실행
./gradlew bootRun
```