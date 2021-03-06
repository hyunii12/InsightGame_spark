package preprocess

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object Ranking {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("ranking"))
    // load files on hdfs
    // meca: 1 ~ 50
    // inven: 1 ~ 10
    // ruri: 1 ~ 14
    val meca_rank = sc.textFile("/meca/rank_meca.txt")
    val inven_ranking = sc.textFile("/inven/rank_inven.txt")
    val ruri_ranking1 = sc.textFile("/ruri/Ruri_3ds_Ranking.txt")
    val ruri_ranking2 = sc.textFile("/ruri/Ruri_Mobile_Ranking.txt")
    val ruri_ranking3 = sc.textFile("/ruri/Ruri_PC_Ranking.txt")
    val ruri_ranking4 = sc.textFile("/ruri/Ruri_PS4_Ranking.txt")
    val ruri_ranking5 = sc.textFile("/ruri/Ruri_PSvita_Ranking.txt")
    val ruri_ranking6 = sc.textFile("/ruri/Ruri_Switch_Ranking.txt")
    val ruri_ranking7 = sc.textFile("/ruri/Ruri_Xbox_Ranking.txt")

	val meca_ranking = meca_rank.filter(data => data(1) < 16)
	
    val rankings = Seq(meca_ranking, inven_ranking, ruri_ranking1, ruri_ranking2, ruri_ranking3, ruri_ranking4, ruri_ranking5, ruri_ranking6, ruri_ranking7)
    val ranking_all = sc.union(rankings)
    val ranking_arr = ranking_all.map(data => data.split(","))
    
    var date = java.time.LocalDate.now.toString;      
    if(args(0) != null || args(0) != "")
      date = args(0);  
    
    val ranking_rdd = ranking_arr.filter(data => data(0) == date)
    val ranking_rdd2 = ranking_rdd.map(data => (data(2), data(1)))
    val ranking_rdd3 = ranking_rdd2.map{ case (k, v) => Array(k, v).mkString(", ")};
    ranking_rdd3.saveAsTextFile("/result_spark/ranking")
    
    
  }
}