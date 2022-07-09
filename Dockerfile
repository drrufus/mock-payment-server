FROM openjdk:11
COPY . /app/
WORKDIR /app/
RUN ./gradlew --no-daemon shadowJar

FROM openjdk:11
COPY --from=0 /app/build/libs/mock-payment-server-0.1-all.jar /app/app.jar
WORKDIR /app
CMD ["java", "-jar", "app.jar"]