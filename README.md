PCM
===
This project contains development artifacts used to perform research around product comparison matrices (PCM). This work is currently involving members of the diverse (DIVERsity-centric Software Engineering) research team (http://diverse.irisa.fr/).

This project is released under Apache v2 License.

### Development tools :
 * [Play](https://www.playframework.com/)
 * Maven
 * Continous integration with Jenkins (https://ci.inria.fr/) and Travis CI: [![Build Status](https://travis-ci.org/gbecan/PCM.svg?branch=master)](https://travis-ci.org/gbecan/PCM)
 * We use intellij with the KMF plugin for the development

### Framework(s) :
 
 *  Kevoree Modeling Framework aka KMF (https://github.com/dukeboard/kevoree-modeling-framework)

### Projects :
This github repository contains several projects :

* org.diverse.PCM.api.java: Java interface
* org.diverse.PCM.api.js: Javascript interface
* org.diverse.PCM.api.java.impl: Java implementation of the API
* org.diverse.PCM.api.js.impl: Js implementation of the API
* org.diverse.PCM.model: contain the data model, takes car of the code source generation for the API
* org.diverse.PCM.formalizer: interpret and formalize cells contained in PCMs
* org.diverse.PCM.naiveFrontEnd: a naive website to show how to manipulate model in a browser
* org.diverse.PCM.io.ShoppingWebSite: parses shopping.com and creates PCMs
* org.diverse.PCM.io.Wikipedia: parses wikipedia pages and creates PCMs
* org.diverse.PCM.io.BestBuy: parses bestbuy.com and creates PCMs
* org.diverse.PCM.play-app: contain OpenCompare website (web editor for PCMs)
* org.diverse.PCM.getting-started: minimal project that shows how to use the API

### Getting started :

    git clone https://github.com/gbecan/PCM.git
    mvn clean install

To compile the project you must define two environment variables:
* JAVA\_HOME must point to a JDK with a version >= 7
* PLAY2\_HOME must point to your install of Play! Framework. If you want to use the integrated install of the project, you can set the variable as follows: PLAY2\_HOME=/.../org.diverse.PCM/org.diverse.PCM.play-app/

The directory _org.diverse.PCM/org.diverse.PCM.getting-started_ contains a minimal maven project that shows how to use the PCM API. This project is independent from the other projects and can be easily copied and modified.

You can also use the following command to skip the tests: 

    mvn clean install -DskipTests

If you want to run the Play! application (basically the OpenCompare website), change directory to '''org.diverse.PCM.play-app''':

    ./activator test

will populate the database (before you need to launch MongoDB server through '''mongod''').

Then, or if you already have populated the database, you can launch the HTTP server:

    ./activator run

