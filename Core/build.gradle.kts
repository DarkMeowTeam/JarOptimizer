val fastutilVersion: String by project
val bcelVersion: String by project

dependencies {
    implementation("it.unimi.dsi:fastutil:$fastutilVersion")
    implementation("org.apache.bcel:bcel:$bcelVersion")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("Core") {
            artifactId = "jar-optimizer-core"
            groupId = project.group.toString()
            version = project.version.toString()

            from(components["java"])
        }
    }
}