package com.smashingmods.chemlib.registry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smashingmods.chemlib.ChemLib;
import com.smashingmods.chemlib.api.ChemicalBlockType;
import com.smashingmods.chemlib.api.ChemicalItemType;
import com.smashingmods.chemlib.api.MatterState;
import com.smashingmods.chemlib.api.MetalType;
import com.smashingmods.chemlib.common.blocks.ChemicalBlock;
import com.smashingmods.chemlib.api.FluidAttributes;
import com.smashingmods.chemlib.common.items.*;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.*;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;

public class ItemRegistry {

    public static final List<ElementItem> ELEMENTS = new ArrayList<>();
    public static final List<CompoundItem> COMPOUNDS = new ArrayList<>();
    public static final List<ChemicalItem> COMPOUND_DUSTS = new ArrayList<>();
    public static final List<ChemicalItem> NUGGETS = new ArrayList<>();
    public static final List<ChemicalItem> INGOTS = new ArrayList<>();
    public static final List<ChemicalItem> PLATES = new ArrayList<>();
    public static final List<ChemicalItem> METAL_DUSTS = new ArrayList<>();
    public static final List<ChemicalBlockItem> CHEMICAL_BLOCK_ITEMS = new ArrayList<>();
    public static final PeriodicTableItem PERIODIC_TABLE_ITEM = new PeriodicTableItem();

    public static final ItemGroup ELEMENTS_TAB = FabricItemGroupBuilder
            .create(new Identifier(ChemLib.MOD_ID, "elements"))
            .icon(() -> getElementByName("hydrogen").map(ItemStack::new).orElseGet(() -> new ItemStack(Items.AIR)))
            .build();

    public static final ItemGroup COMPOUNDS_TAB = FabricItemGroupBuilder
            .create(new Identifier(ChemLib.MOD_ID, "compounds"))
            .icon(() -> getCompoundByName("cellulose").map(ItemStack::new).orElseGet(() -> new ItemStack(Items.AIR)))
            .appendItems(stacks -> {
                stacks.clear();
                List<ItemStack> compounds = getCompounds().stream().map(ItemStack::new).toList();
                List<ItemStack> compoundDusts = getChemicalItemsByType(ChemicalItemType.COMPOUND).stream().map(ItemStack::new).toList();
                stacks.addAll(compounds);
                stacks.addAll(compoundDusts);
            }).build();

    public static final ItemGroup METALS_TAB = FabricItemGroupBuilder
            .create(new Identifier(ChemLib.MOD_ID, "metals"))
            .icon(() -> getChemicalItemByNameAndType("barium", ChemicalItemType.INGOT).map(ItemStack::new).orElseGet(() -> new ItemStack(Items.AIR)))
            .appendItems(stacks -> {
                stacks.clear();
                List<ItemStack> dustStacks = getChemicalItemsByType(ChemicalItemType.DUST).stream().map(ItemStack::new).toList();
                List<ItemStack> nuggetStacks = getChemicalItemsByType(ChemicalItemType.NUGGET).stream().map(ItemStack::new).toList();
                List<ItemStack> ingotStacks = getChemicalItemsByType(ChemicalItemType.INGOT).stream().map(ItemStack::new).toList();
                List<ItemStack> plateStacks = getChemicalItemsByType(ChemicalItemType.PLATE).stream().map(ItemStack::new).toList();
                List<ItemStack> blockItemStacks = getChemicalBlockItems().stream().filter(item -> ((ChemicalBlock) item.getBlock()).getBlockType().asString().equals("metal")).map(ItemStack::new).toList();
                stacks.addAll(ingotStacks);
                stacks.addAll(blockItemStacks);
                stacks.addAll(nuggetStacks);
                stacks.addAll(dustStacks);
                stacks.addAll(plateStacks);
            }).build();

    public static final ItemGroup MISC_TAB = FabricItemGroupBuilder
            .create(new Identifier(ChemLib.MOD_ID, "misc"))
            .icon(() -> getChemicalBlockItemByName("radon_lamp_block").map(ItemStack::new).orElseGet(() -> new ItemStack(Items.AIR)))
            .appendItems(stacks -> {
                stacks.clear();
                List<ItemStack> lamps = BlockRegistry.getLampBlocks().stream().map(ItemStack::new).toList();
                List<ItemStack> buckets = FluidRegistry.getBuckets().stream().map(ItemStack::new).toList();
                stacks.add(new ItemStack(PERIODIC_TABLE_ITEM));
                stacks.addAll(lamps);
                stacks.addAll(buckets);
            })
            .build();

    public static void register() throws IOException {
        // Get element JSON data
        Gson gson = new Gson();
        InputStream elementsInputStream = ItemRegistry.class.getResourceAsStream("/data/chemlib/elements.json");
        JsonObject elements = gson.fromJson(new InputStreamReader(elementsInputStream, StandardCharsets.UTF_8), JsonObject.class);

        // Get compound JSON data
        InputStream compoundsInputStream = ItemRegistry.class.getResourceAsStream("/data/chemlib/compounds.json");
        JsonObject compounds = gson.fromJson(new InputStreamReader(compoundsInputStream, StandardCharsets.UTF_8), JsonObject.class);

        // Register items
        createElements(elements);
        createCompounds(compounds);
        Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, "periodic_table"), PERIODIC_TABLE_ITEM);
        ELEMENTS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, item.getChemicalName()), item));
        COMPOUNDS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, item.getChemicalName()), item));
        COMPOUND_DUSTS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_%s", item.getChemicalName(), item.getItemType().asString())), item));
        METAL_DUSTS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_%s", item.getChemicalName(), item.getItemType().asString())), item));
        NUGGETS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_%s", item.getChemicalName(), item.getItemType().asString())), item));
        INGOTS.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_%s", item.getChemicalName(), item.getItemType().asString())), item));
        PLATES.forEach(item -> Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format("%s_%s", item.getChemicalName(), item.getItemType().asString())), item));
        CHEMICAL_BLOCK_ITEMS.forEach(item -> {
            String path = (item.getType() == ChemicalBlockType.METAL) ? "%s_metal_block" : "%s_lamp_block";
            Registry.register(Registry.ITEM, new Identifier(ChemLib.MOD_ID, String.format(path, item.getChemicalName())), item);
        });
    }

    /*
       This section defines helper methods for getting specific objects out of the registry.
    */

    public static List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        items.addAll(getElements());
        items.addAll(getCompounds());
        items.addAll(getChemicalItems());
        items.addAll(getChemicalBlockItems());
        return items;
    }

    public static List<ElementItem> getElements() {
        return ELEMENTS;
    }

    public static List<CompoundItem> getCompounds() {
        return COMPOUNDS;
    }

    public static List<ChemicalItem> getChemicalItems() {
        List<ChemicalItem> items = new ArrayList<>();
        for (ChemicalItemType type : ChemicalItemType.values()) {
            items.addAll(getChemicalItemsByType(type));
        }
        return items;
    }

    public static List<ChemicalBlockItem> getChemicalBlockItems() {
        return CHEMICAL_BLOCK_ITEMS;
    }

    public static List<ChemicalItem> getChemicalItemsByType(ChemicalItemType chemicalItemType) {
        return switch (chemicalItemType) {
            case COMPOUND -> COMPOUND_DUSTS;
            case DUST -> METAL_DUSTS;
            case NUGGET -> NUGGETS;
            case INGOT -> INGOTS;
            case PLATE -> PLATES;
        };
    }

    public static Stream<ElementItem> getElementsByMatterState(MatterState matterState) {
        return getElements().stream().filter(element -> element.getMatterState().equals(matterState));
    }

    public static Stream<ElementItem> getElementsByMetalType(MetalType metalType) {
        return getElements().stream().filter(element -> element.getMetalType().equals(metalType));
    }

    public static Optional<ElementItem> getElementByName(String name) {
        return getElements().stream().filter(element -> element.getChemicalName().equals(name)).findFirst();
    }

    public static Optional<ElementItem> getElementByAtomicNumber(int atomicNumber) {
        return getElements().stream().filter(element -> element.getAtomicNumber() == atomicNumber).findFirst();
    }

    public static Optional<CompoundItem> getCompoundByName(String name) {
        return getCompounds().stream().filter(compound -> compound.getChemicalName().equals(name)).findFirst();
    }

    public static Optional<ChemicalItem> getChemicalItemByNameAndType(String name, ChemicalItemType chemicalItemType) {
        return getChemicalItemsByType(chemicalItemType)
                .stream()
                .filter(item -> item.getItemType().equals(chemicalItemType))
                .filter(item -> item.getChemical().getChemicalName().equals(name))
                .findFirst();
    }

    public static Optional<ChemicalBlockItem> getChemicalBlockItemByName(String name) {
        return CHEMICAL_BLOCK_ITEMS.stream().filter(item -> Objects.equals(Objects.requireNonNull(item.toString()), name)).findFirst();
    }

    /*
       Elements are built from the Elements json and then everything is registered based on that information.
    */

     public static void createItemByType(Item element, Identifier elementIdentifier, ChemicalItemType chemicalItemType, ItemGroup tab) {
         ChemicalItem chemicalItem = new ChemicalItem(element, chemicalItemType, new FabricItemSettings().group(tab));
         switch(chemicalItemType) {
             case COMPOUND -> COMPOUND_DUSTS.add(chemicalItem);
             case DUST -> METAL_DUSTS.add(chemicalItem);
             case NUGGET -> NUGGETS.add(chemicalItem);
             case INGOT -> INGOTS.add(chemicalItem);
             case PLATE -> PLATES.add(chemicalItem);
         }
     }

    private static void createElements(JsonObject data) {
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
            Identifier elementIdentifier = new Identifier(ChemLib.MOD_ID, elementName);
            ELEMENTS.add(element);

            if (!artificial) {
                switch (matterState) {
                    case SOLID -> {
                        boolean hasItem = object.has("has_item") && object.get("has_item").getAsBoolean();
                        if (metalType == MetalType.METAL) {
                            // Register metal items & blocks
                            createItemByType(element, elementIdentifier, ChemicalItemType.PLATE, METALS_TAB);
                            if (!hasItem) {
                                createItemByType(element, elementIdentifier, ChemicalItemType.NUGGET, METALS_TAB);
                                createItemByType(element, elementIdentifier, ChemicalItemType.INGOT, METALS_TAB);
                                createChemicalBlock(elementIdentifier, ChemicalBlockType.METAL);
                            } else if (elementName.equals("copper")) {
                                createItemByType(element, elementIdentifier, ChemicalItemType.NUGGET, METALS_TAB);
                            }
                        }
                        createItemByType(element, elementIdentifier, ChemicalItemType.DUST, METALS_TAB);
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

                            FluidAttributes attributes = new FluidAttributes(FluidRegistry.STILL, FluidRegistry.FLOWING)
                                    .density(density)
                                    .luminosity(luminosity)
                                    .viscosity(viscosity)
                                    .sound(SoundEvents.ITEM_BUCKET_FILL)
                                    .overlay(FluidRegistry.OVERLAY)
                                    .color((int) Long.parseLong(color, 16));

                            switch (matterState) {
                                case LIQUID -> {
                                    FluidRegistry.registerFluid(elementName, attributes, slopeFindDistance, decreasePerBlock);
                                }
                                case GAS -> {
                                    if (group == 18) {
                                        createChemicalBlock(elementIdentifier, ChemicalBlockType.LAMP);
                                    }
                                    attributes.gaseous();
                                    FluidRegistry.registerFluid(elementName, attributes, slopeFindDistance, decreasePerBlock);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void createCompounds(JsonObject data) {
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
            Identifier compoundIdentifier = new Identifier(ChemLib.MOD_ID, compoundName);
            COMPOUNDS.add(compoundItem);

            switch (matterState) {
                case SOLID -> {
                    boolean hasItem = object.get("has_item").getAsBoolean();
                    if (!hasItem) {
                        createItemByType(compoundItem, compoundIdentifier, ChemicalItemType.COMPOUND, COMPOUNDS_TAB);
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

                        FluidAttributes attributes = new FluidAttributes(FluidRegistry.STILL, FluidRegistry.FLOWING)
                                .density(density)
                                .luminosity(luminosity)
                                .viscosity(viscosity)
                                .sound(SoundEvents.ITEM_BUCKET_FILL)
                                .overlay(FluidRegistry.OVERLAY)
                                .color((int) Long.parseLong(color, 16));

                        switch (matterState) {
                            case LIQUID -> FluidRegistry.registerFluid(compoundName, attributes, slopeFindDistance, decreasePerBlock);
                            case GAS -> {
                                attributes.gaseous();
                                FluidRegistry.registerFluid(compoundName, attributes, slopeFindDistance, decreasePerBlock);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void createChemicalBlock(Identifier chemical, ChemicalBlockType type) {
        Identifier identifier;
        FabricItemSettings settings = new FabricItemSettings();
        if (type == ChemicalBlockType.METAL) {
            String path = "%s_metal_block";
            identifier = new Identifier(ChemLib.MOD_ID, String.format(path, chemical.getPath()));
            settings.group(METALS_TAB);
        } else {
            String path = "%s_lamp_block";
            identifier = new Identifier(ChemLib.MOD_ID, String.format(path, chemical.getPath()));
            settings.group(MISC_TAB);
        }
        ChemicalBlock chemicalBlock = BlockRegistry.registerBlock(chemical, identifier, type);
        CHEMICAL_BLOCK_ITEMS.add(new ChemicalBlockItem(chemicalBlock, settings));
    }
}
