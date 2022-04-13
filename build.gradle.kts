plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.github.imoliwer"
version = "0.0.1"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(project(":server"))
}

tasks.jar {
    manifest {
        attributes(Pair("Main-Class", "com.github.imoliwer.nesqueue.test.CommunicationTest"))
    }
}