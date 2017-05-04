FROM java:8


ADD target /usr/src/target
#ADD ./Shared /usr/src/Shared
#WORKDIR /usr/src
#RUN javac Server/ServerKnot.java
#RUN javac Server/ServerThread.java
#RUN javac Server/Player.java
#RUN javac Shared/Game.java
#RUN javac Shared/Question.java
#ENTRYPOINT java Server/ServerKnot



ENTRYPOINT ["java","-jar","/usr/src/target/quizduell-1.0-jar-with-dependencies.jar"]

EXPOSE 8081