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
    *__Configuration.java :__ initialise la configuration de tout le programme en lisant un fichier de configuration au format JSON. Ce fichier contient la liste de tous les micro contrôleurs à instancier, ainsi que leurs paramètres.
    *__Main.java :__ configure et exécute le programme.
*__fr.unice.smart_campus.cnx :__ 
    *__SerialConnection.java :__ ouvre la communication série entre le micro contrôleur et le client Java. Cette classe permet également d'envoyer des commandes au micro contrôleur et d'imprimer les informations reçues de ce dernier.
*__fr.unice.smart_campus.controller :__
    *__MicroController.java :__ reprèsente un micro contrôleur et toutes les commandes qu'il peut exécuter. Il s'agit de la classe mère des classes suivantes.
    *__MicroControllerArduino.java :__ cette classe fournie toutes les méthodes et les commandes disponibles sur le micro contrôleur Arduino. Elle hérite de la classe __MicroController.java__.
    *__MicroControllerPhidget.java :__ cette classe fournie toutes les méthodes et les commandes disponibles sur le micro contrôleur Phidget. Elle hérite de la classe __MicroController.java__.
    *__MicroControllerConfig.java :__ permet de sauvegarder les capteurs actuellement branchés sur le micro contrôleur, pour permettre de recharger cette configuration en cas de redémarrage du micro contrôleur.
*__fr.unice.smart_campus.data :___
   *__CurrentSensorRepository.java :__ stocks toutes les valeurs de tous les capteurs branchés aux micro contrôleurs. Cette classe permet de fournir la valeur actuelle de chaque capteur.
   *__SensorData.java :__ modélise des informations concernant les données envoyées par les capteurs, comme la valeur de cette donnée et le moment où elle à été reçue. 
   *__SensorDescriptor.java :__ donne des informations "physique" sur le capteur, comme son nom, le pin où il se situe et sa période de rafraichissement. 
   *__SensorHistory.java :__ stock toutes les données concernant les capteurs, depuis leurs création. Ce stockage doit être persistent et est réalisé (pour le moment) dans un fichier au nom du capteur ayant envoyé les données.
   *__SensorTimerRepository.java :__ cette ne sert pour le moment qu'aux Phidgets. Elle permet de stocker dans un dictionnaire chaque catpeur attaché au Phidget et de l'affecté à une tâche. Cette tâche est effectuée toutes les X secondes, X étant la période de rafraichissement des données du capteur. 
*__fr.unice.smart_campus.transformer :__ 
   *__JsonTransformer.java :__ transforme un message du micro contrôleur en un objet JSON, qui sera enegistré dans un fichier ou envoyer à une adresse http. Produit également des données de capteurs depuis un objet JSON. Cette classe se charge du management des flux de données. 

Chacune de ses classes à été testée. De même, plusieurs "stress test" ont été réalisés, pour assurer la stabilité de la plateform en cas d'une utilisation particulièrement prolongée.
