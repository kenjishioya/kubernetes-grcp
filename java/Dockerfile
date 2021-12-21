FROM gradle:7.3-jdk17 as builder

COPY . /src
WORKDIR /src
RUN ./gradlew installDist

FROM openjdk:17-slim

RUN mkdir /app

COPY --from=builder /src/app/build/install/app /app
ENTRYPOINT ["/app/bin/app"]