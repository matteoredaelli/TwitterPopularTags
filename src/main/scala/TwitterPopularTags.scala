// scalastyle:off println
//package apache.spark.examples.streaming

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkContext._
import org.apache.spark.streaming.twitter._
import org.apache.spark.SparkConf

/**
 * Calculates popular hashtags (topics) over sliding 10 and 60 second windows from a Twitter
 * stream. The stream is instantiated with credentials and optionally filters supplied by the
 * command line arguments.
 *
 * Run this on your local machine as
 *
 */
object TwitterPopularTags  {
  def main(args: Array[String]) {
    val filters = args

    // Set the system properties so that Twitter4j library used by twitter stream
    // can use them to generat OAuth credentials
    // otherwise put them in the file twitter4j.properties
    // System.setProperty("twitter4j.oauth.consumerKey", consumerKey)
    // System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret)
    // System.setProperty("twitter4j.oauth.accessToken", accessToken)
    // System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret)
    // System.setProperty("http.proxyHost", "proxy.redaelli.org");
    // System.setProperty("http.proxyPort", 80);
    // System.setProperty("https.proxyHost", "proxy.redaelli.org");
    // System.setProperty("https.proxyPort", 80);

    val sparkConf = new SparkConf().setAppName("TwitterPopularTags")
    val ssc = new StreamingContext(sparkConf, Seconds(30))
    val stream = TwitterUtils.createStream(ssc, None, filters)

    val hashTags = stream.flatMap(status => status.getText.split(" ").filter(_.startsWith("#")))

    val topCounts60 = hashTags.map((_, 1)).reduceByKeyAndWindow(_ + _, Seconds(60))
                     .map{case (topic, count) => (count, topic)}
                     .transform(_.sortByKey(false))

    // Print popular hashtags
    topCounts60.foreachRDD(rdd => {
      val topList = rdd.take(10)
      println("\nPopular topics in last 60 seconds (%s total):".format(rdd.count()))
      topList.foreach{case (count, tag) => println("%s (%s tweets)".format(tag, count))}
    })

    ssc.start()
    ssc.awaitTermination()
  }
}
