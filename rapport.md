# Rapport du projet Spark / Scala – Analyse e-commerce

##  Contexte

Ce projet a été réalisé dans le cadre de la MSPR "Big Data et Analyse de Données". L'objectif était de manipuler des données issues d'une plateforme e-commerce à l'aide de Spark et Scala, en suivant un processus structuré : chargement, nettoyage, analyse statistique, puis visualisation.

##  Étape 1 – Chargement et nettoyage des données

- Le jeu de données `ecommerce_data_enriched.csv` a été chargé dans un RDD Spark.
- Les lignes invalides (valeurs manquantes ou "null", mauvais format) ont été filtrées.
- Une `case class` Scala (`EcommerceSession`) a été utilisée pour structurer les données.

**Avant nettoyage** : 10000 lignes (exemple).  
**Après nettoyage** : ~8452 lignes conservées.

<p align="center">
  <img src="cptr/net.png" width="600" alt="Affichage des lignes après nettoyage">
</p>

##  Étape 2 – Agrégation et analyse statistique

Plusieurs agrégations clés ont été réalisées :

- **Score moyen par catégorie de produit** :

```scala
val avgScoreByCategory = parsedRDD
  .map(s => (s.productCategory, (s.reviewScore, 1)))
  .reduceByKey((a, b) => (a._1 + b._1, a._2 + b._2))
  .mapValues { case (total, count) => total / count }
```

<p align="center">
  <img src="cptr/agreg1.png" width="600" alt="Console - Score moyen par catégorie">
</p>

- **Nombre de sessions par pays** :

```scala
val sessionsByCountry = parsedRDD
  .map(s => (s.country, 1))
  .reduceByKey(_ + _)
```

<p align="center">
  <img src="cptr/agreg2.png" width="600" alt="Console - Sessions par pays">
</p>

- **Durée moyenne de session par catégorie**, **achats moyens**, **pages vues par ville**, etc.
<p align="center">
  <img src="cptr/agreg0.png" width="600" alt="Console - autres">
</p>
<p align="center">
  <img src="cptr/agreg3.png" width="600" alt="Console - autres">
</p>
<p align="center">
  <img src="cptr/agreg4.png" width="600" alt="Console - autres">
</p>
<p align="center">
  <img src="cptr/agreg5.png" width="600" alt="Console - autres">
</p>
<p align="center">
  <img src="cptr/agreg6.png" width="600" alt="Console - autres">
</p>
##  Étape 3 – Visualisation

Nous avons généré des graphiques HTML interactifs à l’aide de **Plotly.js** via du code Scala générant les fichiers HTML.

- **Bar chart** – Score moyen par catégorie :

<p align="center">
  <img src="cptr/Capture%20d'écran%202025-05-26%20005741.png" width="600" alt="Bar Chart - Score par catégorie">
</p>

- **Pie chart** – Répartition des sessions par pays :

<p align="center">
  <img src="cptr/Capture%20d'écran%202025-05-26%20005746.png" width="600" alt="Pie Chart - Sessions par pays">
</p>

- **Line chart** – Durée moyenne des sessions :

<p align="center">
  <img src="cptr/Capture%20d'écran%202025-05-26%20005751.png" width="600" alt="Line Chart - Durée moyenne des sessions">
</p>

- **Bar chart** – Nombre de session par pays :

<p align="center">
  <img src="cptr/Capture%20d'écran%202025-05-26%20005735.png" width="600" alt="Line Chart - Durée moyenne des sessions">
</p>

##  Difficultés rencontrées

- Incompatibilités avec les bibliothèques de visualisation Scala (Vegas, Smile).
- Solutions alternatives mises en place : génération manuelle de fichiers HTML + Plotly.js.
- Nettoyage du jeu de données : plusieurs formats mal définis, valeurs nulles, etc.

##  Conclusion

Ce projet nous a permis de :
- Manipuler Spark RDD avec Scala.
- Appliquer des transformations, filtrages et agrégations complexes.
- Générer des visualisations web interactives en HTML.

##  Pistes d'amélioration
- Utiliser Spark DataFrame au lieu de RDD pour optimiser les traitements.
- Ajouter des modèles prédictifs simples (régression linéaire, clustering).
- Explorer des outils de visualisation Scala plus modernes si disponibles.

---

**Fichiers HTML générés** disponibles dans le dossier `/charts`.  
**Captures d’écran** stockées dans `/images`.
