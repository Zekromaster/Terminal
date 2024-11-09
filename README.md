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

The preferred way to download this library is from Glass Maven

```kotlin
repositories {
    maven {
        name = "Glass Releases"
        url = uri("https://maven.glass-launcher.net/releases")
    }
}
```

```kotlin
dependencies {
    modImplementation("net.zekromaster.minecraft:terminal:0.2.0")
}
```