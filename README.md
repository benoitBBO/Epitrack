# Epitrack
Pour pouvoir lancer le projet il faut :
- faire un ckeckout du projet Epitrack
- générer le jar : lancer un mvn clean install dans le BackEnd
- générer le /dist : lancer un ng build dans le FrontEnd
- installer Docker Desktop : https://www.docker.com/products/docker-desktop/
- lancer le projet avec Docker : commande docker-compose up --build
- charger la base dans le volume : avec l'extension Volumes Backup & Share de Docker Desktop, importer le fichier epitrack_data.tar.zst
- le site est disponible sur l'url localhost:4200
- Enjoy
