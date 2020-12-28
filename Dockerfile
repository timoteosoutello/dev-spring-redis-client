FROM adoptopenjdk/openjdk11-openj9:jre-11.0.9_11_openj9-0.23.0

ENV APP_HOME=/usr/app/

RUN mkdir $APP_HOME

WORKDIR $APP_HOME

COPY target/dev-spring-redis.jar application.jar

EXPOSE 8585

CMD ["java","-jar","application.jar"]