# Sistemas Inteligentes - Práctica 1
## Configure project on Eclipse IDE
1. First clone the repository from `Git Repositories` option menu
2. On `Package Explorer`, right click on _Import_ > _Projects from Git_ > _Existing local repository_ > and select the **puzzle-lab-c1-1** repository
3. If the project has been imported like a general project, you need to convert the project to a Maven/Java project. Right click on your project > _Configure_ > _Configure as a **Maven** project_
4. If you have issues with JAR libraries:
   - Right click on your project > _Build Path_ > _Configure Build Path_
   - On tab _Libraries_, in _Classpath_ add a new external JAR and select the .jar (gson) on _[lib](https://github.com/SI-ESI-2021-22/puzzle-lab-c1-1/tree/main/lib)_ directory