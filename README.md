# HM API
HM API is a library that fully implements Hypixel's Mod API on Fabric 1.20.5+, HM API is built with modern Java 21 features in mind as well as providing a flexible, and easy to use interface for developers to work with.

### Supported Packets:
- [x] `hypixel:party_info` (v2)
- [x] `hypixel:player_info`
- [x] `hypixel:register`
- [x] `hyevent:location`

The `ping` packet is unsupported due to it serving as more of a test for implementations, and also because vanilla has its own `QueryPingC2SPacket` which achieves the same functionality.

### Mods using HM API
- <img src="https://raw.githubusercontent.com/AzureAaron/aaron-mod/1.20/src/main/resources/assets/aaron-mod/icon.png" width="32" height="32" align="center"> [Aaron's Mod](https://modrinth.com/mod/aaron-mod)
- <img src="https://raw.githubusercontent.com/SkyblockerMod/Skyblocker/master/src/main/resources/assets/skyblocker/icon.png" width="32" height="32" align="center"> [Skyblocker](https://modrinth.com/mod/skyblocker-liap)

## Requirements
- Minecraft 1.21+
- Java 21
- [Fabric API](https://modrinth.com/mod/fabric-api)

## Usage
You can import HM API into your project like this:

First, add the following Maven repository to your `build.gradle` file, ensure that it is always at the bottom of the repositories block.

```groovy
repositories {
	exclusiveContent {
		forRepository {
			maven { url "https://maven.azureaaron.net/releases" }
		}

		filter {
			includeGroup "net.azureaaron"
		}
	}
}
```

Second, add HM API as a mod dependency which will be JiJ'ed with your mod.
```groovy
dependencies {
	include modImplementation("net.azureaaron:hm-api:<latest version>")
}
```

Once you've imported the library, you can find the methods to send packet's to Hypixel's Mod API in the [`HypixelNetworking`](src/main/java/net/azureaaron/hmapi/network/HypixelNetworking.java) class. In order to listen to packets you'll need to register to the appropriate event inside of the [`PacketEvents`](src/main/java/net/azureaaron/hmapi/events/PacketEvents.java) class.

> [!IMPORTANT]
> There is an example available [here](src/test/java/net/azureaaron/hmapi/Example.java) with some code to get you started. It's also recommended to read the JavaDocs of these classes to get a sense of how the library works.

## Future Changes
The library has been designed with forwards compatibility in mind which is why if Hypixel releases a new version of a packet then support for it will be added in a separate version-specific package to avoid API breakages, and the old packet will be deprecated for removal at a later date. It's highly recommended to stay up-to-date with changes to Hypixel's Mod API and this library.<br><br>

I'll likely hold off on making breaking API changes to the library until a new Minecraft version releases or if Hypixel makes their own breaking changes to the Mod API. Note that APIs marked as internal or experimental in the library may be broken at any time without notice.

## Maintaining
HM API will only be maintained for the latest verions of Fabric API and Minecraft, it will also be kept up to date with any changes made to Hypixel's Mod API (e.g. new packets, deprecations).
