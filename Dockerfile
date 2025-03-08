FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY gradle gradle
COPY build.gradle settings.gradle gradlew ./

RUN chmod +x gradlew
RUN ./gradlew dependencies

COPY . .

RUN ./gradlew bootJar

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]