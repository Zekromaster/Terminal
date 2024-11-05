import java.nio.charset.Charset

plugins {
    id("fabric-loom") version "1.8.8"
    id("babric-loom-extension") version "1.8.5"
    id("maven-publish")
    id("com.modrinth.minotaur") version "2.+"
}

val maven_group: String by project
val minecraft_version: String by project
val yarn_mappings: String by project
val loader_version: String by project
val stapi_version: String by project
val ami_version: String by project
val bhcreative_version: String by project
val modmenu_version: String by project
val archives_base_name: String by project
val next_version: String by project
val artifact_id: String by project
val api_version: String by project

val use_github_packages = (project.findProperty("gpr.use") as String? ?: System.getenv("GITHUB_USE_PACKAGE_REGISTRY") ?: "false").toBoolean()
val gh_username = project.findProperty("gpr.username") as String? ?: System.getenv("GITHUB_ACTOR")
val gh_token = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
val gh_repo = project.findProperty("gpr.repo") as String

val use_modrinth = (project.findProperty("modrinth.use") as String? ?: System.getenv("MODRINTH_USE") ?: "false").toBoolean()
val modrinth_id = project.findProperty("modrinth.id") as String
val modrinth_token = project.findProperty("modrinth.token") as String? ?: System.getenv("MODRINTH_TOKEN")

val releasing = project.hasProperty("releasing")

java {
    withSourcesJar()
}

loom {
    accessWidenerPath = file("src/main/resources/terminal.accesswidener")
}

group = maven_group
version = next_version

if (!releasing) {
    version = "${version}-SNAPSHOT"
}

tasks.jar {
    archiveBaseName.value(archives_base_name)
}

repositories {
    maven {
        name = "Babric"
        url = uri("https://maven.glass-launcher.net/babric")
    }
    // Used for mappings.
    maven {
        name = "Glass Releases"
        url = uri("https://maven.glass-launcher.net/releases")
    }

    if (use_github_packages) {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Zekromaster/*") // Github Package
            credentials {
                username = gh_username
                password = gh_token
            }
        }
    }

    maven(uri("https://jitpack.io"))

    maven {
        name = "Froge"
        url = uri("https://maven.minecraftforge.net/")
    }

    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }

    mavenCentral()
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraft_version}")
    mappings("net.glasslauncher:biny:${yarn_mappings}:v2")
    modImplementation("babric:fabric-loader:${loader_version}")

    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.17.2")
    implementation("blue.endless:jankson:1.2.1")

    modImplementation("net.modificationstation:StationAPI:${stapi_version}")

    // Optional, but convenient mods for mod creators and users alike.
    modImplementation("net.glasslauncher.mods:ModMenu:${modmenu_version}") {
        isTransitive = false
    }
    modImplementation("net.glasslauncher.mods:glass-networking:1.0.2") {
        isTransitive = false
    }
    implementation("me.carleslc:Simple-Yaml:1.8.4")
    modImplementation("net.glasslauncher.mods:GlassConfigAPI:3.0.0") {
        isTransitive = false
    }
    modImplementation("net.glasslauncher.mods:AlwaysMoreItems:${ami_version}") {
        isTransitive = false
    }

    modImplementation("com.github.matthewperiut:retrocommands:0.5.2") {
        isTransitive = false
    }

    modImplementation ("com.github.paulevsGitch:BHCreative:${bhcreative_version}") {
        isTransitive = false
    }

    modImplementation("maven.modrinth:accessory-api:0.7.0")
}

tasks {
    withType<ProcessResources> {
        inputs.property("version", project.version)

        filesMatching("**/fabric.mod.json") {
            expand("version" to project.version)
        }
    }
}

tasks.withType<JavaCompile>().configureEach { options.release = 17 }

tasks.jar {
    from("LICENSE") {
        rename { "LICENSE_${archives_base_name}"}
    }
}

if (use_github_packages) {
    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                artifactId = artifact_id
                from(components["java"])
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/${gh_repo}") // Github Package
                credentials {
                    username = gh_username
                    password = gh_token
                }
            }
        }
    }
}

if (use_modrinth) {
    modrinth {
        token.set(modrinth_token)
        projectId.set(modrinth_id)
        versionNumber.set(project.version.toString())
        versionType.set("release")
        uploadFile.set(tasks.remapJar)
        gameVersions.addAll("b1.7.3")
        loaders.add("fabric")
        dependencies {
            required.project("stationapi")
        }
        syncBodyFrom = project.file("README.md").readText(Charset.forName("UTF-8"))
    }

    tasks.modrinth {
        dependsOn(tasks.modrinthSyncBody)
    }
}

task("upload") {
    dependsOn(tasks.publish)
    if (use_modrinth && releasing) {
        dependsOn(tasks.modrinth)
    }
}
