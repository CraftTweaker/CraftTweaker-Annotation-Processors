plugins {
    java
    idea
    `maven-publish`
}

group = "com.blamejared.crafttweaker"
version = "4.0.0" + (if (System.getenv().containsKey("BUILD_NUMBER")) ".${System.getenv("BUILD_NUMBER")}" else "")
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
    maven("https://libraries.minecraft.net") {
        name = "Mojang"
    }
}

configurations {
    extendConfiguration(implementation.get(), this.getByName("stubImplementation"))
    extendConfiguration(annotationProcessor.get(), this.getByName("stubAnnotationProcessor"))
}

dependencies {
    implementation("com.mojang:datafixerupper:4.1.27")

    implementation("org.jetbrains:annotations:24.0.1")
    implementation("org.openzen.zencode:JavaAnnotations:0.3.8")
    implementation("com.google.auto.service:auto-service-annotations:1.1.1")
    implementation("io.toolisticon.aptk:aptk-tools:0.26.0")
    implementation("io.toolisticon.aptk:aptk-annotationwrapper-api:0.26.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.apache.commons:commons-lang3:3.13.0")
    implementation("com.google.guava:guava:32.1.2-jre")
    implementation("it.unimi.dsi:fastutil:8.5.6")

    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.25.4")

    annotationProcessor("io.toolisticon.aptk:annotationwrapper-processor:0.17.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("io.toolisticon.cute:cute:0.11.1")
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