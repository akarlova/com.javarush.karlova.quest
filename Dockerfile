FROM tomcat:9-jdk17

# Удалим дефолтные приложения
RUN rm -rf /usr/local/tomcat/webapps/*

# Копируем WAR в webapps
COPY target/quest-app.war /usr/local/tomcat/webapps/ROOT.war

VOLUME ["/data"]

# Порт, на котором будет работать Tomcat
EXPOSE 8080

# Tomcat сам стартует
