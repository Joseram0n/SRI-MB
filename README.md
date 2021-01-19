# SISTEMA DE RECUPERACIÓN DE INFORMACIÓN
___
Este es un proyecto para la asignatura "Motores de Busqueda" de la [ :mortar_board: Universidad
de Huelva :mortar_board: ](http://www.uhu.es/index.php "UHU").
___

## Informacion sobre el proyecto :information_source:
El proyecto consiste en indexar un corpus (Conjunto de documentos) a [Apache Solr](https://lucene.apache.org/solr/ "Solr") mendiante java, para luego relizar una consulta y evaluarla mediante la herramienta [Trec_eval](https://github.com/usnistgov/trec_eval "Trec_eval").   
Se realizaran **2 versiones**, una sencilla y otra usando la herramienta [GATE](https://gate.ac.uk/ "GATE").   
Adicionalmente la parte de java se realizara mediante una interfaz de usuario (GUI).

- :computer: SO: [~~Ubuntu 20.04~~ -> Windows 10](https://ubuntu.com/ "Ubuntu")
- :writing_hand: IDE: [Apache Netbeans 12.2](https://processing.org/ "Netbeans")
- :abc: JAVA: [Java 11](https://openjdk.java.net/ "OpenJDK")
- :computer_mouse: JAVAFX: [JavaFX 13](https://openjfx.io/ "JavaFX")

El proyecto ya no usa ANT, ahora usa Maven por lo que no deberia ser dificil recrealo y montalo en otro lugar.

### Entregas :calendar:

- [x] v0.1 (8/11/2020) App que lea el fichero del corpus Lisa0.001 y sea capaz de indexarlo desde Java usando Solrj.

- [x] v0.2 (23/11/2020) La app lee el fichero de consultas LISA.QUE, toma las primeras 5 palabras y lanza consulta a Apache Solr. Recorremos la respuesta de Solr y la mostramos.

- [x] v0.3 (1/12/2020) Esta versión de la app construye un fichero en formato 'trec_top_file', para poder ejecutar la evaluación con trec_eval.

- [x] v1.0 (8/12/2020) Entrega de SRI1.

- [x] v2.0 (12/1/2020) Entrega de SRI2.