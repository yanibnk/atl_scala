# Projet Spark Scala – Analyse de données e-commerce

##  Description du projet

Ce projet a pour objectif d’analyser un ensemble de données e-commerce enrichies (sessions utilisateur, avis, durée, panier, etc.) à l’aide de **Apache Spark** en **Scala**. Les données sont nettoyées, agrégées puis visualisées sous forme de graphiques HTML générés dynamiquement.

##  Membres du projet

- **Nom** : Yani Benkhelifa
- Ali ismail
- Lydia Hamza
- Ikram Amroun
- 
- **Établissement** : EPSI Paris
- **Classe** : I1 / Big Data

##  Structure du projet

```
.
├── src/
│   └── main/
│       └── scala/
│           └── Projet_scal.scala    # Fichier principal du projet (tout en un)
├── data/
│   └── ecommerce_data_enriched.csv # Jeu de données à analyser
├── charts/
│   ├── session_line.html           
│   ├── score_moyen_par_categorie.html              
│   ├── sessions_pie.html           
│   └── session_line.html 
├── README.md                       # Ce fichier
└── build.sbt                       # Fichier SBT avec dépendances
```

##  Lancer le projet

### 1. Prérequis
- JDK 8 ou 11
- SBT (Scala Build Tool)
- IntelliJ ou un terminal

### 2. Instructions

1. Cloner le dépôt :
```bash
git clone https://github.com/Yanibnk/scala.git
cd scala
```

2. Placer le fichier CSV dans le dossier `data/` (si ce n’est pas déjà fait).

3. Lancer le projet avec :
```bash
sbt run
```

4. Les graphiques seront générés dans le dossier `charts/`.

##  Résultats et visualisations

| Nom du graphique                    | Type      | Description |
|------------------------------------|-----------|-------------|
| `score_bar.html`                   | Bar chart | Score moyen par catégorie de produit |
| `sessions_pie.html`                | Pie chart | Nombre de sessions par pays |
| `session_line.html`                | Line chart| Durée moyenne de session par catégorie |

Les fichiers HTML s’ouvrent dans n’importe quel navigateur.

##  Statistiques clés observées

- Le score moyen par catégorie varie fortement selon les produits.
- Les pays avec le plus d’activité sont France, Maroc, Belgique.
- Les catégories Mode et Maison ont les sessions les plus longues.


---

 Projet réalisé dans le cadre du module Atl Spark Scala à l’EPSI Paris.