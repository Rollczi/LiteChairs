project(":nms:nms-api").dependencies {
    shadow("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
}

project(":nms:v1_19R1").dependencies {
    api(project(":nms:nms-api"))

    shadow("org.spigotmc:spigot:1.19-R0.1-SNAPSHOT")
    shadow("org.spigotmc:spigot-api:1.19-R0.1-SNAPSHOT")
    shadow("io.netty:netty-all:4.1.74.Final")
}