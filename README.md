OpenCompare
===========

[![Join the chat at https://gitter.im/gbecan/OpenCompare](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/OpenCompare/OpenCompare?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/OpenCompare/OpenCompare.svg?branch=master)](https://travis-ci.org/OpenCompare/OpenCompare)

This project contains development artifacts used to perform research around product comparison matrices (PCM). This work is currently involving members of the [DiverSE](http://diverse.irisa.fr/) (DIVERsity-centric Software Engineering) research team.

This project is released under Apache v2 License.


## Development tools

 * [Maven](https://maven.apache.org)
 * [Continous integration](https://ci.inria.fr/) with Jenkins and Travis CI
 * [IntelliJ](https://www.jetbrains.com/idea/) with the KMF plugin for development

## Frameworks and dependencies
 
 * [Play Framewok](https://www.playframework.com)
 * [KMF](https://github.com/dukeboard/kevoree-modeling-framework) (Kevoree Modeling Framework)
 * [MongoDB](https://www.mongodb.com/) v2.6 or later

## Projects

This github repository contains several projects :

* api-java: Java interface to the PCM API
* api-java-impl: Java implementation of the API
* model: contains the PCM metamodel, takes care of code source generation for the API
* dataset-best-buy: builds datasets from BestBuy.com
* dataset-wikipedia: builds datasets from Wikipedia
* io-wikipedia: parses Wikipedia pages and creates PCMs
* io-best-buy: parses BestBuy.com and creates PCMs
* play-app: contains OpenCompare website (Play server)

## Getting started

### Install
First, make sure your JAVA_HOME environment variable is set to the location of a JDK version 8 or later. Then execute the following:

    git clone https://github.com/OpenCompare/OpenCompare.git
    cd OpenCompare/org.opencompare
    export PLAY2_HOME=play-app
    mvn clean install

You can also use this option to skip tests: 

    mvn clean install -DskipTests

You can also permanently set the PLAY2_HOME environment variable by adding the following to the .bashrc file in your /home directory:

	export PLAY2_HOME=/.../OpenCompare/org.opencompare/play-app/

If you wish to use your own installation of Play, export the relevent path in PLAY2_HOME.

### Start the server
To launch the Play application (basically the OpenCompare website), change directory to _org.opencompare/dataset-wikipedia_ and run:

    mvn test -PbuildDataset

Then, go to _org.opencompare/play-app_ and execute the following command to populate the database (be sure to have a MongoDB server running through _mongod_):

    ./activator test

Finally, or if you already have populated the database, you can launch the HTTP server:

    ./activator run

### Reset the database
To remove all the PCMs from the database, you have to connect to your MongoDB instance and remove all the elements of the _pcms_ collection in the _opencompare_ database, as in the following commands:

    mongo
    use opencompare
    db.pcms.remove({})
