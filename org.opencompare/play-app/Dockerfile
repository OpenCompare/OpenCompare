FROM java:latest


COPY ./target/universal/opencompare-*.zip .
RUN unzip *.zip && rm *.zip && mv opencompare-* server
WORKDIR server

ENTRYPOINT ["./bin/opencompare"]

CMD ["-Dhttp.port=80", "-Dconfig.file=/server/config.conf"]

# sudo docker run --name opencompare --rm -v /PATH/TO/CONFIG.PROD.CONF:/server/config.conf:z IMAGE/NAME