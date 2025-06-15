# ImpactorCobbleDollarsBridge
This is a fork of [Impactor](https://github.com/NickImpact/Impactor)!

It provides a bridge to the popular [CobbleDollars](https://modrinth.com/mod/cobbledollars) mod, such that other mods that use Impactor will use the CobbleDollars economy system, instead.
Notable examples:

- [Cobblemon GTS](https://modrinth.com/mod/cobblemon-gts)
- [Cobblemon Hunt](https://modrinth.com/mod/cobblemon-hunt)

It is not a side-mod, but a replacement for Impactor. 

Dependencies:
- Cobblemon
- CobbleDollars

# Development
## Prerequisites
- Install Java. I use openjdk 21.0.7
- Copy the CobbleDollars .jar found in `/libs` and paste it into `/launchers/fabric/run/mods` (make the folders if they don't already exist)
- Run the following to set up the api folder:
```
git submodule update --init --recursive
```

## Build for Fabric
```
./gradlew :launchers:fabric:remapProductionJar
```
It will be found under `/launchers/fabric/build/libs`

## Run with Fabric as Client
```
./gradlew launchers:fabric:runClient
```

## Run with Fabric as Server
```
./gradlew launchers:fabric:runServer
```

# Note for developers
This is the first minecraft mod or fork I've developed. The functionality it in all
likelihood quite buggy. Use at your own risk!

I have not developed or tested this fork with Forge/NeoForge in mind at all!

Yes, I have included CobbleDollars in `/libs`. I don't know how to include it from a remote server.

There is a very weird bug that happens when running runClient/runServer, which seems to be related to line 57 of `/launchers/fabric/build.gradle.kts`.
Commenting out/uncommenting the line sometimes fixes the bug.
Restarting vs code also seems to temporarily fix the problem.
I have concluded that it's some obscure CobbleDollars bug...
