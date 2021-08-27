FROM alpine:latest
WORKDIR /app
RUN apk --no-cache add openjdk11
RUN adduser -D java_user && chown -R java_user /app
COPY ./target/*.jar /app/frontend.jar
USER java_user
ENTRYPOINT ["java", "-jar", "/app/frontend.jar"]