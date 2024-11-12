import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.darkmeow"
version = "1.0.1113"

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    `maven-publish`
}

gradlePlugin {
    plugins {
        create("jar-optimizer") {
            id = "net.darkmeow.jar-optimizer"
            displayName = "Jar Optimizer"
            description = "Simple jar file optimizing tool"
            implementationClass = "net.darkmeow.jaroptimizer.JarOptimizerPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

val library: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val pluginSourceSet = sourceSets.create("plugin").apply {
    java.srcDir("src/plugin/kotlin")
    resources.srcDir("src/plugin/resources")
    compileClasspath += sourceSets.main.get().compileClasspath + sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().runtimeClasspath + sourceSets.main.get().output
}

dependencies {
    library("it.unimi.dsi:fastutil:8.5.13")
    library("org.apache.bcel:bcel:6.8.1")
    library(kotlin("stdlib-jdk8"))
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withSourcesJar()
}

tasks {
    withType(KotlinCompile::class.java) {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }

    jar {
        from(pluginSourceSet.output)
    }

    named<Jar>("sourcesJar") {
        from(pluginSourceSet.allSource)
    }

    val standaloneJar by register<Jar>("standaloneJar") {
        group = "build"

        manifest {
            attributes(
                "Main-Class" to "net.darkmeow.jaroptimizer.JarOptimizer",
            )
        }

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        archiveClassifier.set("standalone")

        from(sourceSets.main.get().output)
        from(library.elements.map { set -> set.map { it.asFile }.map { if (it.isDirectory) it else zipTree(it) } })
    }

    artifacts {
        archives(standaloneJar)
    }
}


publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "net.darkmeow"
            artifactId = "jar-optimizer"
            version = "1.0.1113"
        }
    }
    repositories {
        mavenLocal()
    }
}