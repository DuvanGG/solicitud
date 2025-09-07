FROM amazoncorretto:21
WORKDIR /app
COPY applications/app-service/build/libs/solicitud.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]