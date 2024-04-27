FROM amazoncorretto:17-alpine-jdk
WORKDIR /app
RUN apk add --no-cache maven

COPY . .
RUN mvn clean install
EXPOSE 8080
ENTRYPOINT ["java","-jar","target/Chat-0.0.1.jar"]
