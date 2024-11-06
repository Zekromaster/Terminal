# Terminal

Library mod for b1.7.3, built on top of [StationAPI](https://github.com/ModificationStation/StationAPI).

## Features
* Capability API
* Attachments API
* Item Storage API (Think `IItemHandler`)
* Network and NBT serialization helper classes

## Credits
* Capabilities and Attachments inspired by [Neoforge](https://neoforged.net/)

## Documentation
Stuff is currently documented in Javadoc, and a [wiki](https://github.com/Zekromaster/Terminal/wiki) is under
construction.

## Importing in Your Dev Environment

### Jitpack
```kotlin
repositories {
    maven("https://jitpack.io")
}
```

To get the latest development version:
```kotlin
modImplementation("com.github.zekromaster:Terminal:trunk-SNAPSHOT")
```


To get a specific version (i.e. 0.1.0):
```kotlin
modImplementation(include("com.github.zekromaster:Terminal:0.1.0"))
```

### GitHub Package Registry
Assuming you have a GitHub account, you can authenticate with the GitHub Package Registry with your username and a [personal access token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/managing-your-personal-access-tokens).

Assuming the username and token are stored in the `gpr.user` and `gpr.key` properties, respectively, or in the `GH_USERNAME` and `GH_TOKEN` environment variables, you can add the following to your `build.gradle.kts` file:

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/Zekromaster/Terminal")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GH_USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("GH_TOKEN")
        }
    }
}
```

To get a development version (i.e. 0.2.0 snapshots):
```kotlin
modImplementation("net.zekromaster.minecraft:terminal:0.2.0-SNAPSHOT")
```

To get a specific version (i.e. 0.1.0)
```kotlin
modImplementation("net.zekromaster.minecraft:terminal:0.1.0")
```

### Modrinth Maven

Coming SOON™

### Glass Maven

Coming SOON™