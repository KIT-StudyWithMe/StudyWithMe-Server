FROM openjdk:11
ARG VERSION 
#ENV VERSION=$VERSION
ADD server-$VERSION.jar /opt/server.jar

CMD ["sh", "-c", "java -jar /opt/server.jar"]

