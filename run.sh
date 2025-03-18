#!/bin/bash

# Compile the Java program
javac JogoDoMarciano.java

# Check if the operating system is Windows or Linux/Mac
OS=$(uname -s)

if [[ "$OS" == "Linux" || "$OS" == "Darwin" ]]; then
  # On Linux or macOS, use colon ":" as classpath separator
  java -classpath ".:sqlite-jdbc-3.49.1.0.jar" JogoDoMarciano
elif [[ "$OS" == "CYGWIN"* || "$OS" == "MINGW"* || "$OS" == "MSYS"* ]]; then
  # On Windows (Cygwin, MinGW, MSYS), use semicolon ";" as classpath separator
  java -classpath ".;sqlite-jdbc-3.49.1.0.jar" JogoDoMarciano
else
  echo "Unsupported OS: $OS"
  exit 1
fi
