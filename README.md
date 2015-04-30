OpenCompare
===========
This project contains development artifacts used to perform research around product comparison matrices (PCM). This work is currently involving members of the diverse (DIVERsity-centric Software Engineering) research team (http://diverse.irisa.fr/).

This project is released under Apache v2 License.

### Development tools :
 * [Play](https://www.playframework.com/)
 * Maven
 * Continous integration with Jenkins (https://ci.inria.fr/) and Travis CI: [![Build Status](https://travis-ci.org/gbecan/OpenCompare.svg?branch=master)](https://travis-ci.org/gbecan/PCM)
 * We use intellij with the KMF plugin for the development

### Framework(s) :
 
 *  Kevoree Modeling Framework aka KMF (https://github.com/dukeboard/kevoree-modeling-framework)

### Projects :
This github repository contains several projects :

* api-java: Java interface
* api-js: Javascript interface
* api-java-impl: Java implementation of the API
* api-js-impl: Js implementation of the API
* model: contain the data model, takes car of the code source generation for the API
* formalizer: interpret and formalize cells contained in PCMs
* naiveFrontEnd: a naive website to show how to manipulate model in a browser
* io-shopping-website: parses shopping.com and creates PCMs
* io-wikipedia: parses wikipedia pages and creates PCMs
* io.BestBuy: parses bestbuy.com and creates PCMs
* play-app: contain OpenCompare website (web editor for PCMs)
* getting-started: minimal project that shows how to use the API

### Getting started :

    git clone https://github.com/gbecan/PCM.git
    mvn clean install

To compile the project you must define two environment variables:
* JAVA\_HOME must point to a JDK with a version >= 7
* PLAY2\_HOME must point to your install of Play! Framework. If you want to use the integrated install of the project, you can set the variable as follows: PLAY2\_HOME=/.../org.opencompare/play-app/

The directory _org.diverse.PCM/org.diverse.PCM.getting-started_ contains a minimal maven project that shows how to use the PCM API. This project is independent from the other projects and can be easily copied and modified.

You can also use the following command to skip the tests: 

    mvn clean install -DskipTests

If you want to run the Play! application (basically the OpenCompare website), change directory to '''org.diverse.PCM.play-app''':

    ./activator test

will populate the database (before you need to launch MongoDB server through '''mongod''').

Then, or if you already have populated the database, you can launch the HTTP server:

    ./activator run

