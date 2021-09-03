FROM daocloud.io/library/java:openjdk-8u40-jdk
EXPOSE 9998
ADD ./hainu-admin/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","app.jar","--server.port=9998","--spring.profiles.active=docker"]

