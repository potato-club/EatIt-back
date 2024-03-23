#FROM ubuntu:latest
#LABEL authors="jihye"
#ENTRYPOINT ["top", "-b"]

FROM openjdk:17-jdk AS build
WORKDIR /tmp
COPY . /tmp
#RUN chmod +x ./gradlew && ./gradlew clean bootJar
RUN chmod +x ./gradlew
RUN microdnf install -y findutils
RUN ./gradlew bootJar

# 생성한 jar 파일을 실행함.
FROM openjdk:17-jdk
WORKDIR /tmp
COPY --from=build /tmp/build/libs/ItEat.jar /tmp/ItEat.jar
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /tmp/ItEat.jar"]