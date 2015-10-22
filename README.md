# TwitterPopularTags
TwitterPopularTags: the famous example of Spark Streaming in a standalone project

## QUICK START

* install Apache Spark (http://spark.apache.org/):wq
* install sbt (http://www.scala-sbt.org/)
* git clone https://github.com/matteoredaelli/TwitterPopularTags
* cd TwitterPopularTags
* cp twitter4j.properties.sample twitter4j.properties
* edit twitter4j.properties.sample
* sbt package
* spark-submit --master local --packages "org.apache.spark:spark-streaming-twitter_2.10:1.5.1"  ./target/scala-2.10/twitterpopulartags_2.10-1.0.jar italy

Bye
Matteo.Redaelli@gmail.com
