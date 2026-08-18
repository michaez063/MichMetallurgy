package ru.mich.michmetallurgy;

import net.minecraft.client.gui.screens.MenuScreens;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import static ru.mich.michmetallurgy.ModItems.LEAD_INGOT;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.gui.screens.MenuScreens;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MichMetallurgy.MODID)
public class MichMetallurgy {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "michmetallurgy";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "michmetallurgy" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "michmetallurgy" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "michmetallurgy" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a new Block with the id "michmetallurgy:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "michmetallurgy:example_block", combining the namespace and path
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);

    // Creates a new food item with the id "michmetallurgy:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(1).saturationModifier(2f).build()));

    // Creates a creative tab with the id "michmetallurgy:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.michmetallurgy")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());
    public static final String MOD_ID = "michmetallurgy";
    private final IEventBus modEventBus;

    private void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.BIG_BLAST_FURNACE_MENU.get(), BigBlastFurnaceScreen::new);
    }
    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MichMetallurgy(IEventBus modEventBus, ModContainer modContainer) {
        this.modEventBus = modEventBus;


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModArmorMaterials.ARMOR_MATERIALS.register(modEventBus);
        // В конструкторе главного класса MichMetallurgy(IEventBus modEventBus):
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENUS.register(modEventBus);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (MichMetallurgy) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("HELLO FROM COMMON SETUP");
        }

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(@NotNull BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.TIN_BLOCK.get());
            event.accept(ModItems.LEAD_BLOCK_ITEM.get());
            event.accept(ModBlocks.RAW_TIN_BLOCK.get());
            event.accept(ModItems.RAW_LEAD_BLOCK_ITEM.get());
            event.accept(ModBlocks.ZINC_BLOCK.get());
            event.accept(ModBlocks.RAW_ZINC_BLOCK.get());
            event.accept(ModBlocks.BLAST_FURNACE_CASING.get());
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.LEAD_INGOT.get());
            event.accept(ModItems.TIN_INGOT.get());
            event.accept(ModItems.RAW_LEAD.get());
            event.accept(ModItems.RAW_TIN.get());
            event.accept(ModItems.RAW_ZINC.get());
            event.accept(ModItems.ZINC_INGOT.get());
            event.accept(ModItems.LEAD_NUGGET.get());
            event.accept(ModItems.TIN_NUGGET.get());
            event.accept(ModItems.COPPER_NUGGET.get());
            event.accept(ModItems.ZINC_NUGGET.get());
            event.accept(ModItems.BRONZE_INGOT.get());
            event.accept(ModItems.BRASS_INGOT.get());
            event.accept(ModItems.AIR_DUCT.get());
            event.accept(ModItems.TIN_ROTOR.get());
            event.accept(ModItems.COKE_COAL.get());
            event.accept(ModItems.CAST_IRON_INGOT.get());
            event.accept(ModItems.STEEL_INGOT.get());
        }
        // Добавляем блоки во вкладку "Природные блоки"
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.TIN_ORE.get());
            event.accept(ModBlocks.LEAD_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_TIN_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_LEAD_ORE.get());
            event.accept(ModBlocks.ZINC_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_ZINC_ORE.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.TIN_SWORD.get());
            event.accept(ModItems.LEAD_SWORD.get());
            event.accept(ModItems.TIN_HELMET.get());
            event.accept(ModItems.TIN_BOOTS.get());
            event.accept(ModItems.TIN_CHESTPLATE.get());
            event.accept(ModItems.TIN_LEGGINGS.get());
            event.accept(ModItems.LEAD_CHESTPLATE.get());
            event.accept(ModItems.LEAD_BOOTS.get());
            event.accept(ModItems.LEAD_HELMET.get());
            event.accept(ModItems.LEAD_LEGGINGS.get());
            event.accept(ModItems.BRASS_SWORD.get());
            event.accept(ModItems.BRONZE_SWORD.get());
            event.accept(ModItems.BRASS_HELMET.get());
            event.accept(ModItems.BRASS_CHESTPLATE.get());
            event.accept(ModItems.BRASS_LEGGINGS.get());
            event.accept(ModItems.BRASS_BOOTS.get());
            event.accept(ModItems.BRONZE_HELMET.get());
            event.accept(ModItems.BRONZE_CHESTPLATE.get());
            event.accept(ModItems.BRONZE_LEGGINGS.get());
            event.accept(ModItems.BRONZE_BOOTS.get());
            event.accept(ModItems.BRASS_AXE.get());
            event.accept(ModItems.BRONZE_AXE.get());
            event.accept(ModItems.TIN_AXE.get());
            event.accept(ModItems.LEAD_AXE.get());
        }

        // Добавляем инструменты во вкладку «Инструменты и утилиты»
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.TIN_PICKAXE.get());
            event.accept(ModItems.TIN_AXE.get());
            event.accept(ModItems.TIN_SHOVEL.get());
            event.accept(ModItems.TIN_HOE.get());
            event.accept(ModItems.LEAD_PICKAXE.get());
            event.accept(ModItems.LEAD_AXE.get());
            event.accept(ModItems.LEAD_SHOVEL.get());
            event.accept(ModItems.LEAD_HOE.get());
            event.accept(ModItems.BRASS_PICKAXE.get());
            event.accept(ModItems.BRASS_AXE.get());
            event.accept(ModItems.BRASS_SHOVEL.get());
            event.accept(ModItems.BRASS_HOE.get());
            event.accept(ModItems.BRONZE_PICKAXE.get());
            event.accept(ModItems.BRONZE_AXE.get());
            event.accept(ModItems.BRONZE_SHOVEL.get());
            event.accept(ModItems.BRONZE_HOE.get());
        }

        // Вкладка "Функциональные блоки" (Functional Blocks) - здесь лежат печки, верстаки и т.д.
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.BIG_BLAST_FURNACE.get());
            event.accept(ModBlocks.COKE_OVEN.get());
            event.accept(ModBlocks.PUDDLING_FURNACE.get());
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
