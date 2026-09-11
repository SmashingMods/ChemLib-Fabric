![lol](https://i.imgur.com/nRaok6G.png)

[![](https://cf.way2muchnoise.eu/full_chemlib-fabric_downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/chemlib-fabric) [![](https://cf.way2muchnoise.eu/packs/chemlib-fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/chemlib-fabric) [![](https://cf.way2muchnoise.eu/versions/chemlib-fabric.svg)](https://www.curseforge.com/minecraft/mc-mods/chemlib-fabric) [![](https://jitpack.io/v/SmashingMods/ChemLib-Fabric.svg)](https://jitpack.io/#SmashingMods/ChemLib-Fabric)

# ChemLib - Fabric Library
_This is a Fabric port of the original ChemLib Forge library by Dark_Arcana. It adds over 950+ items to Minecraft, including every known chemical element, compound, metal, liquid, and gas that exists in the real world. The purpose of this library is to provide a shared source of items, blocks, and fluids for chemistry and tech mods. It was originally developed for the Alchemistry mod but can be used by any mod!_

# Minecraft 26.2

This branch targets **Minecraft Java 26.2**, **Java 25**, **Fabric Loader 0.19.5 or newer**, and **Fabric API 0.160.0+26.2**.

The port preserves the chemical definitions, registry identifiers, textures, recipe quantities, fluid flow settings, redstone lamp timing, four creative tabs, element labels, and periodic table screen and painting. It contains 800 registered items (including 118 elements and 175 compounds), 105 blocks, 37 fluid pairs, and 387 recipes.

Build with `./gradlew build` (`gradlew.bat build` on Windows). The installable mod is `build/libs/ChemLib-1.0.1+mc26.2.jar`; put it alongside Fabric API in the Minecraft 26.2 instance's `mods` folder.

Run `./gradlew runGameTest` for server-side content, recipes, creative tabs, block drops, mining requirements, and lamp timing checks. Run `./gradlew runClientGameTest` on a machine with graphics support for model loading and screenshots of the item gallery and periodic table. These tests use temporary worlds under `build/run/` and are excluded from the published mod.

The update follows the [Fabric 26.2 migration notes](https://fabricmc.net/2026/06/15/262.html). Recipes, loot tables, advancements, and tag folders use the current data-pack format. Painting variants are now data-driven, and item colors and element rendering use the current model pipeline. A legacy `forge:glass` alias maps to `c:glass_blocks` so the existing lamp recipes load on Fabric.

# Downloads

### Recommended Releases

The latest stable releases can always be found on either [CurseForge](https://www.curseforge.com/minecraft/mc-mods/chemlib-fabric) or [Modrinth](https://modrinth.com/mod/chemlib-fabric).<br/>
The top file in the list is the latest recommended release!

### Development Builds

If you are looking for the latest bleeding edge build, you can find unstable releases [here](https://github.com/SmashingMods/ChemLib-Fabric/releases).</br>
Use these builds with caution, and please do not put these in modpacks. They may contain major bugs!

# Maven
In Fabric, add the following to your `build.gradle` and replace `[VERSION]` with the version you want.<br/>
You can view a list of all available versions by visiting [JitPack](https://jitpack.io/#SmashingMods/ChemLib-Fabric)
```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation "com.github.SmashingMods:ChemLib-Fabric:[VERSION]"
}
```

# Screenshots
<img src="https://i.imgur.com/eEwQjtp.png" width="70%" height="70%" />

# Credits

* TechnoVision - Developer of the Fabric version
* DarkArcana - Developer of the Forge version
* Timbroglio - Model and Texture Artist

# License

ChemLib is All Rights Reserved unless explicitly stated.
