FROM gradle:6-jdk11
COPY ./ ./
RUN gradle build -x test
ARG SAMPLE_FOLDER
ARG CHANNEL_TOKEN
ARG CHANNEL_SECRET
CMD java -Dline.bot.channelToken=${CHANNEL_TOKEN} -Dline.bot.channelSecret=${CHANNEL_SECRET} -jar ./${SAMPLE_FOLDER}/build/libs/${SAMPLE_FOLDER}-*-SNAPSHOT.jar
