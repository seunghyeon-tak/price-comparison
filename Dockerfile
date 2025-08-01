FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY . /app

RUN ./gradlew clean build

COPY ./build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]