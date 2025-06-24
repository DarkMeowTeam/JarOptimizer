val fastutilVersion: String by project
val bcelVersion: String by project

plugins {
    `java-gradle-plugin`
}

dependencies {
    implementation(project(":Core"))
    implementation("it.unimi.dsi:fastutil:$fastutilVersion")
    implementation("org.apache.bcel:bcel:$bcelVersion")
}

gradlePlugin {
    plugins {
        create("jar-optimizer") {
            id = "net.darkmeow.jar-optimizer"
            displayName = "Jar Optimizer"
            description = "Simple jar file optimizing tool"
            implementationClass = "net.darkmeow.jar_optimizer.JarOptimizerPlugin"
        }
    }
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("Plugin") {
            artifactId = "jar-optimizer"
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }
}