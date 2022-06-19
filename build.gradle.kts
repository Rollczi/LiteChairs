plugins {
    id("java-library")
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

subprojects {
    repositories {
        mavenCentral()

        maven { url = uri("https://repo.panda-lang.org/releases") }
        maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    }

    group = "dev.rollczi"
    version = "1.0.0-SNAPSHOT"

    apply(plugin = "java-library")
    apply(plugin = "com.github.johnrengelman.shadow")

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    sourceSets.main.get().java.setSrcDirs(listOf("src/"))

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

project(":lite-chairs") {
    apply(plugin = "net.minecrell.plugin-yml.bukkit")
    apply(plugin = "xyz.jpenilla.run-paper")

    bukkit {
        main = "dev.rollczi.litechairs.LiteChairs"
        apiVersion = "1.13"
        prefix = "LiteChairs"
        author = "Rollczi"
        name = "LiteChairs"
        version = "${project.version}"
    }

    dependencies {
        implementation(project(":nms:nms-api"))
        implementation(project(":nms:v1_19R1"))
        compileOnly("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
    }

    tasks.withType <com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        archiveFileName.set("LiteChairs v${project.version}.jar")

        exclude("org/intellij/lang/annotations/**")
        exclude("org/jetbrains/annotations/**")
        exclude("META-INF/**")
        exclude("javax/**")

        mergeServiceFiles()
        minimize()
    }

    tasks {
        runServer {
            minecraftVersion("1.19")
        }
    }
}