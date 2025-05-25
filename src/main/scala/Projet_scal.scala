// === [Imports nécessaires] ===
import org.apache.spark.sql.SparkSession
import java.io.{File, PrintWriter}
import org.apache.commons.lang3.StringEscapeUtils

// === [Structure des données] ===
// Définition d'une classe pour représenter une session e-commerce
case class EcommerceSession(
                             userId: String,
                             sessionDuration: Int,
                             pagesViewed: Int,
                             productCategory: String,
                             purchaseAmount: Double,
                             reviewScore: Double,
                             reviewText: String,
                             timestamp: String,
                             deviceType: String,
                             country: String,
                             city: String
                           )

object Projet_scal {
  def main(args: Array[String]): Unit = {

    // === [1. Initialisation Spark] ===
    val spark = SparkSession.builder
      .appName("Ecommerce Data Preprocessing")
      .master("local[*]")
      .config("spark.ui.showConsoleProgress", "false")
      .config("spark.log.level", "OFF")
      .getOrCreate()

    val sc = spark.sparkContext

    // === [2. Chargement et Nettoyage des données] ===
    val rawRDD = sc.textFile("data/ecommerce_data_enriched.csv")
    val header = rawRDD.first()
    val dataRDD = rawRDD.filter(line => line != header)

    // Suppression des lignes incomplètes ou contenant "null"
    val cleanedRDD = dataRDD.filter { line =>
      val parts = line.split(",", -1)
      parts.length == 11 && !parts.contains("") && !parts.contains("null")
    }

    // Conversion en objets EcommerceSession
    val parsedRDD = cleanedRDD.map { line =>
      val parts = line.split(",", -1)
      EcommerceSession(
        parts(0), parts(1).toInt, parts(2).toInt, parts(3),
        parts(4).toDouble, parts(5).toDouble, parts(6),
        parts(7), parts(8), parts(9), parts(10)
      )
    }

    // Affichage du volume avant/après nettoyage
    val totalAvant = dataRDD.count()
    val totalApres = cleanedRDD.count()
    println(s"Total avant nettoyage : $totalAvant")
    println(s"Total après nettoyage : $totalApres")
    println(s"Nombre de lignes après nettoyage : ${parsedRDD.count()}")

    // === [3. Analyses statistiques et visualisations] ===

    // --- (3.1) Score moyen par catégorie (bar chart) ---
    val avgScoreByCategory = parsedRDD
      .map(s => (s.productCategory, (s.reviewScore, 1)))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      .mapValues { case (total, count) => total / count }
      .sortBy({ case (_, score) => -score })
      .collect()
    println("=== Score moyen par catégorie ===")
    avgScoreByCategory.foreach(println)
    generateChartHTML(avgScoreByCategory, "Score moyen par catégorie", "charts/score_bar.html", "bar")

    // --- (3.2) Nombre de sessions par pays (pie chart) ---
    val sessionsByCountry = parsedRDD
      .map(s => (s.country, 1))
      .reduceByKey(_ + _)
      .sortBy({ case (_, count) => -count })
      .collect()
    println("=== Nombre de sessions par pays ===")
    sessionsByCountry.foreach(println)
    generateChartHTML(sessionsByCountry.map { case (k, v) => (k, v.toDouble) }, "Sessions par pays", "charts/sessions_pie.html", "pie")

    // --- (3.3) Durée moyenne de session par catégorie (line chart) ---
    val avgSessionByCategory = parsedRDD
      .map(s => (s.productCategory, (s.sessionDuration.toDouble, 1)))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      .mapValues { case (total, count) => total / count }
      .sortBy({ case (_, duration) => -duration })
      .collect()
    println("=== Durée moyenne de session par catégorie ===")
    avgSessionByCategory.foreach(println)
    generateChartHTML(avgSessionByCategory, "Durée moyenne de session par catégorie", "charts/session_line.html", "line")

    // --- (3.4) Montant moyen des achats par pays ---
    val avgPurchaseByCountry = parsedRDD
      .map(s => (s.country, (s.purchaseAmount, 1)))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      .mapValues { case (total, count) => total / count }
      .collect()
    println("=== Montant moyen des achats par pays ===")
    avgPurchaseByCountry.foreach(println)

    // --- (3.5) Nombre de sessions par catégorie ---
    val sessionsByCategory = parsedRDD
      .map(s => (s.productCategory, 1))
      .reduceByKey(_ + _)
      .collect()
    println("=== Nombre de sessions par catégorie ===")
    sessionsByCategory.foreach(println)

    // --- (3.6) Répartition par type d'appareil ---
    val deviceDistribution = parsedRDD
      .map(s => (s.deviceType, 1))
      .reduceByKey(_ + _)
      .collect()
    println("=== Répartition par type d'appareil ===")
    deviceDistribution.foreach(println)

    // --- (3.7) Moyenne des pages vues par ville (Top 10) ---
    val avgPagesByCity = parsedRDD
      .map(s => (s.city, (s.pagesViewed, 1)))
      .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
      .mapValues { case (total, count) => total.toDouble / count }
      .take(10)
    println("=== Moyenne des pages vues par ville (Top 10) ===")
    avgPagesByCity.sortBy({ case (_, avgPages) => -avgPages }).foreach(println)

    // === [Fin du programme Spark] ===
    spark.stop()
  }

  // === [4. Fonction de génération de graphiques HTML] ===
  def generateChartHTML(data: Array[(String, Double)], title: String, outputFile: String, chartType: String): Unit = {
    val labels = data.map { case (k, _) => "\"" + StringEscapeUtils.escapeJson(k) + "\"" }.mkString(", ")
    val values = data.map { case (_, v) => v.toString }.mkString(", ")

    val plotType = chartType match {
      case "pie" =>
        s"""
           |var data = [{
           |  labels: [$labels],
           |  values: [$values],
           |  type: 'pie'
           |}];
         """.stripMargin
      case "line" =>
        s"""
           |var data = [{
           |  x: [$labels],
           |  y: [$values],
           |  type: 'scatter',
           |  mode: 'lines+markers'
           |}];
         """.stripMargin
      case _ =>
        s"""
           |var data = [{
           |  x: [$labels],
           |  y: [$values],
           |  type: 'bar'
           |}];
         """.stripMargin
    }

    val html =
      s"""
         |<!DOCTYPE html>
         |<html>
         |<head>
         |  <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>
         |</head>
         |<body>
         |  <div id="chart" style="width:1000px;height:600px;"></div>
         |  <script>
         |    $plotType
         |    var layout = { title: '${StringEscapeUtils.escapeEcmaScript(title)}' };
         |    Plotly.newPlot('chart', data, layout);
         |  </script>
         |</body>
         |</html>
         |""".stripMargin

    val chartsDir = new File("charts")
    if (!chartsDir.exists()) chartsDir.mkdir()

    val writer = new PrintWriter(new File(outputFile))
    writer.write(html)
    writer.close()
  }
}
