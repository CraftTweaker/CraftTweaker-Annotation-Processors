plugins {
    java
    idea
    `maven-publish`
}

group = "com.blamejared.crafttweaker"
version = "3.0.0" + (if (System.getenv().containsKey("BUILD_NUMBER")) ".${System.getenv("BUILD_NUMBER")}" else "")
val baseArchiveName = "Crafttweaker_Annotation_Processors"
base {
    archivesName.set(baseArchiveName)
}
extensions.configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withJavadocJar()
    withSourcesJar()
}

sourceSets {
    val stub = create("stub")

    main {
        java {
            srcDirs("src/generated/java")
        }
        compileClasspath += project.files(stub.output)
        runtimeClasspath += project.files(stub.output)
    }

    test {
        compileClasspath += stub.output + main.get().output
        runtimeClasspath += stub.output + main.get().output
    }
}

repositories {
    mavenCentral()
    maven("https://maven.blamejared.com") {
        name = "BlameJared Maven"
    }
}

configurations {
    extendConfiguration(implementation.get(), this.getByName("stubImplementation"))
    extendConfiguration(annotationProcessor.get(), this.getByName("stubAnnotationProcessor"))
}

dependencies {
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("org.openzen.zencode:JavaAnnotations:0.3.8")
    implementation("com.google.auto.service:auto-service-annotations:1.0.1")
    implementation("io.toolisticon.aptk:aptk-tools:0.19.2")
    implementation("io.toolisticon.aptk:annotationwrapper-api:0.17.1")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    annotationProcessor("io.toolisticon.aptk:annotationwrapper-processor:0.17.1")
    annotationProcessor("com.google.auto.service:auto-service:1.0.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.toolisticon.cute:cute:0.11.1")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.google.truth.extensions:truth-java8-extension:1.1.3")
}

tasks {
    getByName<Test>("test") {
        useJUnitPlatform()
        maxParallelForks = Runtime.getRuntime().availableProcessors().div(4)

        systemProperty("junit.jupiter.execution.parallel.enabled", "true")
        systemProperty("junit.jupiter.execution.parallel.mode.default", "concurrent")
        systemProperty("junit.jupiter.execution.parallel.mode.classes.default", "concurrent")
    }

    jar {
        from(sourceSets["stub"].output.classesDirs)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    withType<Javadoc> {
        source(sourceSets["stub"].allJava)
    }

    named<Jar>("sourcesJar") {
        from(sourceSets["stub"].allSource)
    }

    withType<JavaCompile> {
        val generatedFiles = file("$projectDir/src/generated/java")
        options.compilerArgs.add("-s")
        options.compilerArgs.add(generatedFiles.path)

        doFirst {

            generatedFiles.mkdirs()
        }
    }
}

fun extendConfiguration(base: Configuration, vararg others: Configuration) {
    others.forEach { it.extendsFrom(base) }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = baseArchiveName
            from(components["java"])
        }
    }

    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}