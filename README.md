# TranspilerSRL
Transpiler SRL to Java or Kotlin


IntelliJ will not recognize the auto-generated classes even if explicitly imported. Referencing these and using them in the project will
still work but they will be underlined in red. To fix this add the generated .jar file (<project-name>-1.0-SNAPSHOT-jar-with-dependencies.jar)
that is automatically generated under the target folder to the maven project modules. 
