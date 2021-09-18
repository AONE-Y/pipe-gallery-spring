FROM openjdk
ENV LANG C.UTF-8
EXPOSE 9998
EXPOSE 8999
ADD ./hainu-admin/target/*.jar /app.jar
ENTRYPOINT ["java","-jar","app.jar","--server.port=9998","--spring.profiles.active=docker"]

