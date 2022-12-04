FROM adoptopenjdk/openjdk11:ubi
RUN mkdir /opt/app
COPY target/*.jar /opt/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /opt/app/app.jar