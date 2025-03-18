#!/bin/bash

javac JogoDoMarciano.java
java -classpath ".:sqlite-jdbc-3.49.1.0.jar" JogoDoMarciano
