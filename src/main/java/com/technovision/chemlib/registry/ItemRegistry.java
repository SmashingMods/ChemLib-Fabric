package com.technovision.chemlib.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.technovision.chemlib.ChemLib;
import com.technovision.chemlib.api.MatterState;
import com.technovision.chemlib.api.MetalType;
import com.technovision.chemlib.common.items.CompoundItem;
import com.technovision.chemlib.common.items.ElementItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ItemRegistry {

    public static final Map<String, ElementItem> ELEMENTS = new HashMap<>();
    public static final Map<String, CompoundItem> COMPOUNDS = new HashMap<>();

    public static void register() throws IOException {
        // Get element JSON data
        Gson gson = new Gson();
        InputStream elementsInputStream = ItemRegistry.class.getResourceAsStream("/data/chemlib/elements.json");
        JsonObject elements = gson.fromJson(new InputStreamReader(elementsInputStream, StandardCharsets.UTF_8), JsonObject.class);

        // Get compound JSON data
        InputStream compoundsInputStream = ItemRegistry.class.getResourceAsStream("/data/chemlib/compounds.json");
        JsonObject compounds = gson.fromJson(new InputStreamReader(compoundsInputStream, StandardCharsets.UTF_8), JsonObject.class);

        // Register items
        registerElements(elements);
        registerCompounds(compounds);
    }

     /*
        This section defines helper methods for getting specific objects out of the registry.
     */

    //TODO: Add methods here

     /*
        Elements are built from the Elements json and then everything is registered based on that information.
     */

    private static void registerElements(JsonObject data) {
        for (JsonElement jsonElement : data.getAsJsonArray("elements")) {
            JsonObject object = jsonElement.getAsJsonObject();
            String elementName = object.get("name").getAsString();
            int atomicNumber = object.get("atomic_number").getAsInt();
            String abbreviation = object.get("abbreviation").getAsString();
            int group = object.get("group").getAsInt();
            int period = object.get("period").getAsInt();
            MatterState matterState = MatterState.valueOf(object.get("matter_state").getAsString().toUpperCase());
            MetalType metalType = MetalType.valueOf(object.get("metal_type").getAsString().toUpperCase());
            boolean artificial = object.has("artificial") && object.get("artificial").getAsBoolean();
            String color = object.get("color").getAsString();

            ElementItem element = new ElementItem(elementName, atomicNumber, abbreviation, group, period, matterState, metalType, artificial, color);
            Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, elementName), element);
            ELEMENTS.put(elementName, element);

            if (!artificial) {
                switch (matterState) {
                    case SOLID -> {
                        boolean hasItem = object.has("has_item") && object.get("has_item").getAsBoolean();
                        if (!hasItem) {
                            if (metalType == MetalType.METAL) {
                                //ItemRegistry.registerItemByType(registryObject, ChemicalItemType.NUGGET, ItemRegistry.METALS_TAB);
                                //ItemRegistry.registerItemByType(registryObject, ChemicalItemType.INGOT, ItemRegistry.METALS_TAB);
                                //ItemRegistry.registerItemByType(registryObject, ChemicalItemType.PLATE, ItemRegistry.METALS_TAB);

                                //ChemicalBlock chemicalBlock = new ChemicalBlock(new Identifier(ChemLib.MOD_ID, elementName), ChemicalBlockType.METAL, BlockRegistry.METAL_BLOCKS, BlockRegistry.METAL_PROPERTIES);
                                //Registry.register(Registry.BLOCK, new Identifier(ChemLib.MOD_ID, String.format("%s_metal_block", elementName)), chemicalBlock);

                                //ItemRegistry.fromChemicalBlock(BlockRegistry.getRegistryObjectByName(String.format("%s_metal_block", elementName)).get(), new Item.Properties().tab(ItemRegistry.METALS_TAB));
                            }
                            //ItemRegistry.registerItemByType(registryObject, ChemicalItemType.DUST, ItemRegistry.METALS_TAB);
                        }
                    }
                    case LIQUID, GAS -> {
                        boolean hasFluid = object.has("has_fluid") && object.get("has_fluid").getAsBoolean();
                        if (!hasFluid) {
                            JsonObject fluidAttributes = object.get("fluid_attributes").getAsJsonObject();
                            int density = fluidAttributes.get("density").getAsInt();
                            int luminosity = fluidAttributes.get("luminosity").getAsInt();
                            int viscosity = fluidAttributes.get("viscosity").getAsInt();
                            int slopeFindDistance = fluidAttributes.has("slope_find_distance") ? fluidAttributes.get("slope_find_distance").getAsInt() : 5;
                            int decreasePerBlock = fluidAttributes.has("decrease_per_block") ? fluidAttributes.get("decrease_per_block").getAsInt() : 2;

                            /**
                            FluidAttributes.Builder attributes = FluidAttributes.builder(FluidRegistry.STILL, FluidRegistry.FLOWING)
                                    .density(density)
                                    .luminosity(luminosity)
                                    .viscosity(viscosity)
                                    .sound(SoundEvents.BUCKET_FILL)
                                    .overlay(FluidRegistry.OVERLAY)
                                    .color((int) Long.parseLong(color, 16));

                            switch (matterState) {
                                case LIQUID -> {
                                    FluidRegistry.registerFluid(elementName, attributes, slopeFindDistance, decreasePerBlock);
                                }
                                case GAS -> {
                                    if (group == 18) {
                                        BlockRegistry.BLOCKS.register(String.format("%s_lamp_block", elementName), () -> new LampBlock(new ResourceLocation(ChemLib.MODID, elementName), ChemicalBlockType.LAMP, BlockRegistry.LAMP_BLOCKS, BlockRegistry.LAMP_PROPERTIES));
                                        ItemRegistry.fromChemicalBlock(BlockRegistry.getRegistryObjectByName(String.format("%s_lamp_block", elementName)).get(), new Item.Properties().tab(ItemRegistry.MISC_TAB));
                                    }
                                    attributes.gaseous();
                                    FluidRegistry.registerFluid(elementName, attributes, slopeFindDistance, decreasePerBlock);
                                }
                            }
                             */
                        }
                    }
                }
            }
        }
    }

    private static void registerCompounds(JsonObject data) {
        for (JsonElement jsonElement : data.getAsJsonArray("compounds")) {
            JsonObject object = jsonElement.getAsJsonObject();
            String compoundName = object.get("name").getAsString();
            MatterState matterState = MatterState.valueOf(object.get("matter_state").getAsString().toUpperCase());
            String description = object.has("description") ? object.get("description").getAsString() : "";
            String color = object.get("color").getAsString();

            JsonArray components = object.getAsJsonArray("components");
            HashMap<String, Integer> componentMap = new LinkedHashMap<>();
            for (JsonElement component : components) {
                JsonObject componentObject = component.getAsJsonObject();
                String componentName = componentObject.get("name").getAsString();
                int count = componentObject.has("count") ? componentObject.get("count").getAsInt() : 1;
                componentMap.put(componentName, count);
            }

            CompoundItem compoundItem = new CompoundItem(compoundName, matterState, componentMap, description, color);
            Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, compoundName), compoundItem);
            COMPOUNDS.put(compoundName, compoundItem);

            switch (matterState) {
                case SOLID -> {
                    boolean hasItem = object.get("has_item").getAsBoolean();
                    if (!hasItem) {
                        //ItemRegistry.registerItemByType(ItemRegistry.getRegistryObject(ItemRegistry.REGISTRY_COMPOUNDS, compoundName), ChemicalItemType.COMPOUND, ItemRegistry.COMPOUND_TAB);
                    }
                }
                case LIQUID, GAS -> {
                    boolean hasFluid = object.has("has_fluid") && object.get("has_fluid").getAsBoolean();
                    if (!hasFluid) {
                        JsonObject fluidAttributes = object.get("fluid_attributes").getAsJsonObject();
                        int density = fluidAttributes.get("density").getAsInt();
                        int luminosity = fluidAttributes.get("luminosity").getAsInt();
                        int viscosity = fluidAttributes.get("viscosity").getAsInt();
                        int slopeFindDistance = fluidAttributes.has("slope_find_distance") ? fluidAttributes.get("slope_find_distance").getAsInt() : 5;
                        int decreasePerBlock = fluidAttributes.has("decrease_per_block") ? fluidAttributes.get("decrease_per_block").getAsInt() : 2;

                        /**
                        FluidAttributes.Builder attributes = FluidAttributes.builder(FluidRegistry.STILL, FluidRegistry.FLOWING)
                                .density(density)
                                .luminosity(luminosity)
                                .viscosity(viscosity)
                                .sound(SoundEvents.BUCKET_FILL)
                                .overlay(FluidRegistry.OVERLAY)
                                .color((int) Long.parseLong(color, 16));

                        switch (matterState) {
                            case LIQUID -> FluidRegistry.registerFluid(compoundName, attributes, slopeFindDistance, decreasePerBlock);
                            case GAS -> {
                                attributes.gaseous();
                                FluidRegistry.registerFluid(compoundName, attributes, slopeFindDistance, decreasePerBlock);
                            }
                        }
                         */
                    }
                }
            }
        }
    }
}
