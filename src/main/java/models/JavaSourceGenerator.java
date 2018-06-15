package models;

import io.soracom.inventory.agent.core.util.TypedAnnotatedObjectTemplateClassGenerator;

import java.io.File;

public class JavaSourceGenerator {

    public static void main(String[] args) {
        String javaPackage = "models";
        File sourceFileDir = new File("src/main/java");
        TypedAnnotatedObjectTemplateClassGenerator generator = new TypedAnnotatedObjectTemplateClassGenerator(
                javaPackage, sourceFileDir);
        File[] modelFiles = new File("src/main/resources").listFiles();
        for (File modelFile : modelFiles) {
            generator.generateTemplateClassFromObjectModel(modelFile);
        }
        System.out.println("Finished generating Java source.");
        System.exit(0);
    }
}
