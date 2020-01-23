FROM hseeberger/scala-sbt:8u222_1.3.2_2.13.1
WORKDIR /orderandchaos
ADD . /orderandchaos
CMD sbt run

