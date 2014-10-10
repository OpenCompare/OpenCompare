PCM
===
This project contains development artifacts used to perform research around product comparison matrices (PCM). This work is currently involving member of the diverse (DIVERsity-centric Software Engineering) research team (http://diverse.irisa.fr/).


### Development tools :
 * [Play](https://www.playframework.com/)
 * Maven
 * Continous integration with Jenkins (https://ci.inria.fr/)
 * We use intellij with the KMF plugin

### Framework(s) :
 
 *  Kevoree Modeling Framework aka KMF (https://github.com/dukeboard/kevoree-modeling-framework

### Projects :
This github repository contains several projects :

* org.diverse.PCM.api.java: Java interface
* org.diverse.PCM.api.js: Js interface
*  org.diverse.PCM.api.java.impl: Java implementation
* org.diverse.PCM.api.js.impl: Js implementation
* org.diverse.PCM.model: contain the data model, takes car of the code source generation
* org.diverse.PCM.naiveFrontEnd: a naive website to show how to manipulate model in a browser
* org.diverse.PCM.io.ShoppingWebSite: parses shopping.com and creates PCMs
* org.diverse.PCM.io.Wikipedia: parses wikipedia and creates PCMs
* org.diverse.PCM.play-app: will contain the web editor

### Getting started :
    https://github.com/gbecan/PCM.git
    mvm clean install