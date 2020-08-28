FROM openjdk:11
EXPOSE 8081
ADD  target/roomrecommendation-docker.jar roomrecommendation-docker.jar
ADD RoomsData.json RoomsData.json
ENTRYPOINT ["java","-jar","/roomrecommendation-docker.jar"]