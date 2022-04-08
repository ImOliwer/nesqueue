plugins {
    java
}

group = "com.github.imoliwer"
version = "0.0.1"

subprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(project(":shared"))
    }
}