FROM java:8

COPY QuizduellFinalServer-1.0-jar-with-dependencies.jar /etc/QuizduellFinalServer-1.0-jar-with-dependencies.jar
#ADD target /usr/src/target
#ADD ./Shared /usr/src/Shared
#WORKDIR /usr/src
#RUN javac Server/ServerKnot.java
#RUN javac Server/ServerThread.java
#RUN javac Server/Player.java
#RUN javac Shared/Game.java
#RUN javac Shared/Question.java
#ENTRYPOINT java Server/ServerKnot



ENTRYPOINT ["java","-jar","/etc/QuizduellFinalServer-1.0-jar-with-dependencies.jar"]

EXPOSE 8081