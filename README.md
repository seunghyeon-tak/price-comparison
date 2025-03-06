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
│   │   ├── resources
│   │   │   ├── application.yml (git에 포함되지 않음)
│   ├── test (테스트 코드)
├── .gitignore
├── README.md
├── build.gradle (gradle 의존성 관리)
```

## 프로젝트 환경 변수 설정 (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```

## 프로젝트 실행 방법

```bash
# git clone
git clone https://github.com/seunghyeon-tak/price-comparison.git
cd project_name

# gradle 빌드 및 실행
./gradlew bootRun
```