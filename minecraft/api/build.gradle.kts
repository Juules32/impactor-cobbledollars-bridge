import extensions.writeVersion

plugins {
    id("impactor.base-conventions")
    id("impactor.publishing-conventions")
    id("org.spongepowered.gradle.vanilla")
}

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

minecraft {
    version("${rootProject.property("minecraft")}")
}

dependencies {
    api(project(":api:items"))
    api(project(":api:ui"))
    
    implementation(files("../../libs/CobbleDollars-fabric-${rootProject.property("cobbledollars")}.jar"))
}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])

            groupId = "net.impactdev.impactor.minecraft"
            artifactId = "api"
            version = writeVersion(true)
        }
    }
}