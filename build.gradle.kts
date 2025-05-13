plugins {
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.14"
}
repositories {
    mavenCentral()
}
dependencies {
}
javafx {
    version = "20"
    modules = listOf("javafx.controls")
}
application {
    mainClass.set("ca.qc.bdeb.sim203.projetjavafx.Main")
    applicationDefaultJvmArgs = listOf("-Dprism.order=sw")
}