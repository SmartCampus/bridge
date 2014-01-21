Bridge
======

Présentation de la plate-forme
---------

Le bridge (ou Data Collector) à pour rôle de récupèrer les données transmises par un ou plusieurs réseau(x) de capteurs,
de les stocker pour les traiter, puis de les transmettre à un serveur web, pour consultation ultérieure. 
Ce bridge doit être compatible avec le plus de plate-formes possibles (Arduino, Phidget ...), pour assurer la réutilisabilité
du système et son extension. 
Le bridge est développé en Java. 

Contenu du répertoire Git
---------

Le dossier Git est divisé en 2 parties : une partie test et une partie source (src). Ici, nous allons détailler la partie
source (src) du programme. 
Cette partie contient les classes Java nécessaire à la bonne exécution du programme, ainsi que les dépendances Maven, 
nécessaire pour l'utilisation de librairies extérieurs. 

La partie source (src) est divisée en 8 packages : 
* __fr.unice.smart_campus :__
    *
