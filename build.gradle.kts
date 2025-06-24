import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val baseGroup: String by project
val baseVersion: String by project

val fastutilVersion: String by project
val cliVersion: String by project

group = baseGroup
version = baseVersion

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

allprojects {
    group = baseGroup
    version = baseVersion

    apply(plugin = "kotlin")
    apply(plugin = "maven-publish")

    repositories {
        mavenCentral()
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }

    tasks.withType(KotlinCompile::class.java) {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_1_8)
    }

    publishing {
        val mavenAuth = System.getenv("MAVEN_USERNAME")?.let { username ->
            System.getenv("MAVEN_PASSWORD")?.let { password ->
                username to password
            }
        }

        repositories {
            mavenLocal()

            mavenAuth?.also { auth ->
                maven {
                    url = uri("https://nekocurit.asia/repository/release/")

                    credentials {
                        username = auth.first
                        password = auth.second
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(project(":Core"))
    implementation("it.unimi.dsi:fastutil:${fastutilVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-cli:$cliVersion")
}


tasks {
    shadowJar {
        archiveClassifier.set("")

        manifest {
            attributes("Main-Class" to "net.darkmeow.jar_optimizer.JarOptimizerLoader",)
        }

        minimize()
    }
    build {
        dependsOn(shadowJar)
    }
}