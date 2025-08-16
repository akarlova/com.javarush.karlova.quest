FROM tomcat:9.0-jdk21-temurin-jammy

RUN rm -rf /usr/local/tomcat/webapps/*


COPY target/quest-app.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
