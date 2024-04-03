FROM amazoncorretto:17-alpine-jdk
# Setting work directory
WORKDIR /app
# Install Maven
RUN apk add --no-cache maven

# Copy your project files and build the project
COPY . .
RUN mvn clean install
ENTRYPOINT ["java","-jar","target/Chat-0.0.1.jar"]