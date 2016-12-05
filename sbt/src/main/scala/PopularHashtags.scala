package com.daya.com

import org.apache.spark._
import org.apache.spark.SparkContext._
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter._
import org.apache.spark.streaming.StreamingContext._
import Utilities._

/** Listens to a stream of Tweets and keeps track of the most popular
 *  hashtags over a 5 minute window.
 */
object PopularHashtags {
  
  /** main function */
  def main(args: Array[String]) {

    // Configure Twitter credentials using twitter.txt
    setupTwitter()
    
    // Set up a Spark streaming context 
    val ssc = new StreamingContext("local[*]", "PopularHashtags", Seconds(1))
    
    
    setupLogging()

    // Create a DStream 
    val tweets = TwitterUtils.createStream(ssc, None)
    
    // Now extract the text of each status 
    val statuses = tweets.map(status => status.getText())
    
    // Blow out each word into a new DStream
    val tweetwords = statuses.flatMap(tweetText => tweetText.split(" "))
    
    // Now eliminate anything that's not a hashtag
    val hashtag = tweetwords.filter(word => word.startsWith("#"))
    
    //Filter the English Tweets only
    
    val hashtags = hashtag.flatMap(x => x.split("\\W+").filter(x => x.matches("[A-Za-z]+") && x.length > 2))

    
  
    val hashtagKeyValues = hashtags.map(hashtag => (hashtag, 1))
    
    // count them up over a 5 minute window sliding every one second
    val hashtagCounts = hashtagKeyValues.reduceByKeyAndWindow( (x,y) => x + y, (x,y) => x - y, Seconds(300), Seconds(1))
    
    // Sort the results by the count values
    val sortedResults = hashtagCounts.transform(rdd => rdd.sortBy(x => x._2, false))
    
    // Print the top 10
    sortedResults.print
    
    // Set a checkpoint directory
    
    ssc.checkpoint("/tmp/checkpoint/")
    ssc.start()
    ssc.awaitTermination()
  }  
}

