#!/bin/bash

# "--scan-class-path" pour tout tester
option="--scan-class-path"  # Utilise cette option si vous ne voulez tester qu'une classe spécifique

# Compilation des classes nécessaires
# Compile uniquement les classes spécifiques, ici les classes de test
javac -d bin -sourcepath src/tests -cp "bin:lib/junit-platform-console-standalone-1.11.3.jar" tests/tests_partie1/*.java tests/tests_partie2/*.java

# Exécution des tests
java -jar lib/junit-platform-console-standalone-1.11.3.jar execute --classpath bin $option