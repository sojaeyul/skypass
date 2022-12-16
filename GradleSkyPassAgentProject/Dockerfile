#FROM openjdk:11
FROM amazoncorretto:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# timezone 적용
ENV TZ Asia/Seoul
ENTRYPOINT ["java","-jar","-Dskypass.instance=dev",,"-Dapp.server.num=1""app.jar"]