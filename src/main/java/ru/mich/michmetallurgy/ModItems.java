package ru.mich.michmetallurgy;

import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber(modid = MichMetallurgy.MODID)
public class ModItems {
    // Создаем регистратор предметов для твоего MODID (michmetallurgy)
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(MichMetallurgy.MODID);

    // 1. Сам оловянный слиток
    public static final DeferredItem<Item> TIN_INGOT =
            ITEMS.registerSimpleItem("tin_ingot", new Item.Properties());

    // 2. Предмет оловянного блока (чтобы блок можно было взять в инвентарь и поставить)
    public static final DeferredItem<BlockItem> TIN_BLOCK_ITEM =
            ITEMS.register("tin_block", () -> new BlockItem(ModBlocks.TIN_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<Item> LEAD_INGOT = ITEMS.register("lead_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> LEAD_BLOCK_ITEM = ITEMS.register("lead_block",
            () -> new BlockItem(ModBlocks.LEAD_BLOCK.get(), new Item.Properties()));

    // Сырое олово
    public static final DeferredItem<Item> RAW_TIN = ITEMS.register("raw_tin",
            () -> new Item(new Item.Properties()));

    // Сырой свинец
    public static final DeferredItem<Item> RAW_LEAD = ITEMS.register("raw_lead",
            () -> new Item(new Item.Properties()));

    // Блок-предмет для оловянной руды
    public static final DeferredItem<Item> TIN_ORE_ITEM = ITEMS.register("tin_ore",
            () -> new BlockItem(ModBlocks.TIN_ORE.get(), new Item.Properties()));

    // Блок-предмет для свинцовой руды
    public static final DeferredItem<Item> LEAD_ORE_ITEM = ITEMS.register("lead_ore",
            () -> new BlockItem(ModBlocks.LEAD_ORE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> DEEPSLATE_LEAD_ORE_ITEM = ITEMS.register("deepslate_lead_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_LEAD_ORE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> DEEPSLATE_TIN_ORE_ITEM = ITEMS.register("deepslate_tin_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_TIN_ORE.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> RAW_LEAD_BLOCK_ITEM = ITEMS.register("raw_lead_block",
            () -> new BlockItem(ModBlocks.RAW_LEAD_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<BlockItem> RAW_TIN_BLOCK_ITEM = ITEMS.register("raw_tin_block",
            () -> new BlockItem(ModBlocks.RAW_TIN_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<Item> TIN_SWORD = ITEMS.register("tin_sword",
            () -> new SwordItem(ModTiers.TIN, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.TIN, 3, -2.4F))));

    public static final DeferredItem<Item> TIN_PICKAXE = ITEMS.register("tin_pickaxe",
            () -> new PickaxeItem(ModTiers.TIN, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModTiers.TIN, 1, -2.8F))));

    public static final DeferredItem<Item> TIN_AXE = ITEMS.register("tin_axe",
            () -> new AxeItem(ModTiers.TIN, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.TIN, 6.5F, -3.15F))));

    public static final DeferredItem<Item> TIN_SHOVEL = ITEMS.register("tin_shovel",
            () -> new ShovelItem(ModTiers.TIN, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModTiers.TIN, 1.5F, -3.0F))));

    public static final DeferredItem<Item> TIN_HOE = ITEMS.register("tin_hoe",
            () -> new HoeItem(ModTiers.TIN, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModTiers.TIN, 0, -1.5F))));

    public static final DeferredItem<Item> LEAD_SWORD = ITEMS.register("lead_sword",
            () -> new SwordItem(ModTiers.LEAD, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.LEAD, 3, -2.4F))));

    public static final DeferredItem<Item> LEAD_PICKAXE = ITEMS.register("lead_pickaxe",
            () -> new PickaxeItem(ModTiers.LEAD, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModTiers.LEAD, 1, -2.8F))));

    public static final DeferredItem<Item> LEAD_AXE = ITEMS.register("lead_axe",
            () -> new AxeItem(ModTiers.LEAD, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.LEAD, 6.5F, -3.15F))));

    public static final DeferredItem<Item> LEAD_SHOVEL = ITEMS.register("lead_shovel",
            () -> new ShovelItem(ModTiers.LEAD, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModTiers.LEAD, 1.5F, -3.0F))));

    public static final DeferredItem<Item> LEAD_HOE = ITEMS.register("lead_hoe",
            () -> new HoeItem(ModTiers.LEAD, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModTiers.LEAD, 0, -1.5F))));

    // === ОЛОВЯННАЯ БРОНЯ ===
    public static final DeferredItem<Item> TIN_HELMET = ITEMS.register("tin_helmet",
            () -> new ArmorItem(ModArmorMaterials.TIN_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(176))); // Шлем (между кольчугой [165] и железом [165+])

    public static final DeferredItem<Item> TIN_CHESTPLATE = ITEMS.register("tin_chestplate",
            () -> new ArmorItem(ModArmorMaterials.TIN_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(256))); // Нагрудник (между кольчугой [181] и железом [240])

    public static final DeferredItem<Item> TIN_LEGGINGS = ITEMS.register("tin_leggings",
            () -> new ArmorItem(ModArmorMaterials.TIN_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(240))); // Поножи (между кольчугой [171] и железом [225])

    public static final DeferredItem<Item> TIN_BOOTS = ITEMS.register("tin_boots",
            () -> new ArmorItem(ModArmorMaterials.TIN_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(208))); // Ботинки (между кольчугой [155] и железом [195])


    // === СВИНЦОВАЯ БРОНЯ ===
    public static final DeferredItem<Item> LEAD_HELMET = ITEMS.register("lead_helmet",
            () -> new ArmorItem(ModArmorMaterials.LEAD_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(154))); // Шлем (чуть ниже кольчуги [165])

    public static final DeferredItem<Item> LEAD_CHESTPLATE = ITEMS.register("lead_chestplate",
            () -> new ArmorItem(ModArmorMaterials.LEAD_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(168))); // Нагрудник (чуть ниже кольчуги [181])

    public static final DeferredItem<Item> LEAD_LEGGINGS = ITEMS.register("lead_leggings",
            () -> new ArmorItem(ModArmorMaterials.LEAD_ARMOR_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(158))); // Поножи (чуть ниже кольчуги [171])

    public static final DeferredItem<Item> LEAD_BOOTS = ITEMS.register("lead_boots",
            () -> new ArmorItem(ModArmorMaterials.LEAD_ARMOR_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(144))); // Ботинки (чуть ниже кольчуги [155])

    public static final DeferredItem<Item> ZINC_ORE_ITEM = ITEMS.register("zinc_ore",
            () -> new BlockItem(ModBlocks.ZINC_ORE.get(), new Item.Properties()));

    public static final DeferredItem<Item> DEEPSLATE_ZINC_ORE_ITEM = ITEMS.register("deepslate_zinc_ore",
            () -> new BlockItem(ModBlocks.DEEPSLATE_ZINC_ORE.get(), new Item.Properties()));

    public static final DeferredItem<Item> RAW_ZINC = ITEMS.register("raw_zinc",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ZINC_INGOT = ITEMS.register("zinc_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> ZINC_BLOCK = ITEMS.register("zinc_block",
            () -> new BlockItem(ModBlocks.ZINC_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<Item> RAW_ZINC_BLOCK = ITEMS.register("raw_zinc_block",
            () -> new BlockItem(ModBlocks.RAW_ZINC_BLOCK.get(), new Item.Properties()));

    public static final DeferredItem<Item> BIG_BLAST_FURNACE = ITEMS.register("big_blast_furnace",
            () -> new BlockItem(ModBlocks.BIG_BLAST_FURNACE.get(), new Item.Properties()));

    public static final DeferredItem<Item> BRONZE_INGOT = ITEMS.register("bronze_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> BRASS_INGOT = ITEMS.register("brass_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<BlockItem> BLAST_FURNACE_CASING = ITEMS.registerSimpleBlockItem(
            ModBlocks.BLAST_FURNACE_CASING
    );

    // Самородки (кусочки металлов)
    public static final DeferredItem<Item> COPPER_NUGGET = ITEMS.registerSimpleItem("copper_nugget");
    public static final DeferredItem<Item> TIN_NUGGET = ITEMS.registerSimpleItem("tin_nugget");
    public static final DeferredItem<Item> LEAD_NUGGET = ITEMS.registerSimpleItem("lead_nugget");
    public static final DeferredItem<Item> ZINC_NUGGET = ITEMS.registerSimpleItem("zinc_nugget");

    // === ЛАТУННЫЕ ИНСТРУМЕНТЫ ===
    public static final DeferredItem<Item> BRASS_SWORD = ITEMS.register("brass_sword",
            () -> new SwordItem(ModTiers.BRASS, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.BRASS, 3, -2.4F)))); // 3 - доп. урон меча, -2.4 - скорость атаки

    public static final DeferredItem<Item> BRASS_PICKAXE = ITEMS.register("brass_pickaxe",
            () -> new PickaxeItem(ModTiers.BRASS, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModTiers.BRASS, 1.0F, -2.8F))));

    public static final DeferredItem<Item> BRASS_AXE = ITEMS.register("brass_axe",
            () -> new AxeItem(ModTiers.BRASS, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.BRASS, 6.0F, -3.1F))));

    public static final DeferredItem<Item> BRASS_SHOVEL = ITEMS.register("brass_shovel",
            () -> new ShovelItem(ModTiers.BRASS, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModTiers.BRASS, 1.5F, -3.0F))));

    public static final DeferredItem<Item> BRASS_HOE = ITEMS.register("brass_hoe",
            () -> new HoeItem(ModTiers.BRASS, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModTiers.BRASS, -2.0F, -1.0F))));


    // === БРОНЗОВЫЕ ИНСТРУМЕНТЫ ===
    public static final DeferredItem<Item> BRONZE_SWORD = ITEMS.register("bronze_sword",
            () -> new SwordItem(ModTiers.BRONZE, new Item.Properties()
                    .attributes(SwordItem.createAttributes(ModTiers.BRONZE, 3, -2.4F))));

    public static final DeferredItem<Item> BRONZE_PICKAXE = ITEMS.register("bronze_pickaxe",
            () -> new PickaxeItem(ModTiers.BRONZE, new Item.Properties()
                    .attributes(PickaxeItem.createAttributes(ModTiers.BRONZE, 1.0F, -2.8F))));

    public static final DeferredItem<Item> BRONZE_AXE = ITEMS.register("bronze_axe",
            () -> new AxeItem(ModTiers.BRONZE, new Item.Properties()
                    .attributes(AxeItem.createAttributes(ModTiers.BRONZE, 6.0F, -3.1F))));

    public static final DeferredItem<Item> BRONZE_SHOVEL = ITEMS.register("bronze_shovel",
            () -> new ShovelItem(ModTiers.BRONZE, new Item.Properties()
                    .attributes(ShovelItem.createAttributes(ModTiers.BRONZE, 1.5F, -3.0F))));

    public static final DeferredItem<Item> BRONZE_HOE = ITEMS.register("bronze_hoe",
            () -> new HoeItem(ModTiers.BRONZE, new Item.Properties()
                    .attributes(HoeItem.createAttributes(ModTiers.BRONZE, -2.0F, -1.0F))));

    // === ЛАТУННАЯ БРОНЯ (+25% прочности -> множитель 19) ===
    public static final DeferredItem<Item> BRASS_HELMET = ITEMS.register("brass_helmet",
            () -> new ArmorItem(ModArmorMaterials.BRASS, ArmorItem.Type.HELMET, new Item.Properties().durability(11 * 19)));
    public static final DeferredItem<Item> BRASS_CHESTPLATE = ITEMS.register("brass_chestplate",
            () -> new ArmorItem(ModArmorMaterials.BRASS, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(16 * 19)));
    public static final DeferredItem<Item> BRASS_LEGGINGS = ITEMS.register("brass_leggings",
            () -> new ArmorItem(ModArmorMaterials.BRASS, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(15 * 19)));
    public static final DeferredItem<Item> BRASS_BOOTS = ITEMS.register("brass_boots",
            () -> new ArmorItem(ModArmorMaterials.BRASS, ArmorItem.Type.BOOTS, new Item.Properties().durability(13 * 19)));

    // === БРОНЗОВАЯ БРОНЯ (-25% прочности -> множитель 11) ===
    public static final DeferredItem<Item> BRONZE_HELMET = ITEMS.register("bronze_helmet",
            () -> new ArmorItem(ModArmorMaterials.BRONZE, ArmorItem.Type.HELMET, new Item.Properties().durability(11 * 11)));
    public static final DeferredItem<Item> BRONZE_CHESTPLATE = ITEMS.register("bronze_chestplate",
            () -> new ArmorItem(ModArmorMaterials.BRONZE, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(16 * 11)));
    public static final DeferredItem<Item> BRONZE_LEGGINGS = ITEMS.register("bronze_leggings",
            () -> new ArmorItem(ModArmorMaterials.BRONZE, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(15 * 11)));
    public static final DeferredItem<Item> BRONZE_BOOTS = ITEMS.register("bronze_boots",
            () -> new ArmorItem(ModArmorMaterials.BRONZE, ArmorItem.Type.BOOTS, new Item.Properties().durability(13 * 11)));

    public static final DeferredItem<Item> TIN_ROTOR = ITEMS.register("tin_rotor",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> AIR_DUCT = ITEMS.register("air_duct",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> COKE_COAL = ITEMS.registerSimpleItem("coke_coal");

    public static final DeferredItem<Item> COKE_OVEN_ITEM = ITEMS.register("coke_oven",
            () -> new BlockItem(ModBlocks.COKE_OVEN.get(), new Item.Properties()));

    public static final DeferredItem<Item> CAST_IRON_INGOT = ITEMS.register("cast_iron_ingot",
            () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> STEEL_INGOT = ITEMS.registerSimpleItem("steel_ingot");

    public static final DeferredItem<Item> PUDDLING_FURNACE_ITEM = ITEMS.register("puddling_furnace",
            () -> new BlockItem(ModBlocks.PUDDLING_FURNACE.get(), new Item.Properties()));

    // Метод для подключения к главной шине мода
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.COKE_COAL.get())) {
            event.setBurnTime(3200); // Обычный уголь = 1600, коксовый — вдвое дольше
        }
    }
}