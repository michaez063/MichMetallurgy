package ru.mich.michmetallurgy;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import ru.mich.michmetallurgy.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public static final int BIG_BLAST_FURNACE_COOKING_TIME = 200; // 10 секунд (200 тиков)
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        // наши рецепты


        // ================= ОЛОВЯННАЯ БРОНЯ =================

        // Шлем из олова (5 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TIN_HELMET.get())
                .define('X', ModItems.TIN_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput);

        // Нагрудник из олова (8 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TIN_CHESTPLATE.get())
                .define('X', ModItems.TIN_INGOT.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput);

        // Поножи из олова (7 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TIN_LEGGINGS.get())
                .define('X', ModItems.TIN_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput);

        // Ботинки из олова (4 слитка)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.TIN_BOOTS.get())
                .define('X', ModItems.TIN_INGOT.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput);


        // ================= СВИНЦОВАЯ БРОНЯ =================

        // Шлем из свинца (5 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEAD_HELMET.get())
                .define('X', ModItems.LEAD_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(ModItems.LEAD_INGOT.get()))
                .save(recipeOutput);

        // Нагрудник из свинца (8 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEAD_CHESTPLATE.get())
                .define('X', ModItems.LEAD_INGOT.get())
                .pattern("X X")
                .pattern("XXX")
                .pattern("XXX")
                .unlockedBy("has_lead_ingot", has(ModItems.LEAD_INGOT.get()))
                .save(recipeOutput);

        // Поножи из свинца (7 слитков)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEAD_LEGGINGS.get())
                .define('X', ModItems.LEAD_INGOT.get())
                .pattern("XXX")
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(ModItems.LEAD_INGOT.get()))
                .save(recipeOutput);

        // Ботинки из свинца (4 слитка)
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.LEAD_BOOTS.get())
                .define('X', ModItems.LEAD_INGOT.get())
                .pattern("X X")
                .pattern("X X")
                .unlockedBy("has_lead_ingot", has(ModItems.LEAD_INGOT.get()))
                .save(recipeOutput);

        // ================= ПЕРЕПЛАВКА ЦИНКА =================

// 1. Сырой цинк -> Цинковый слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_ZINC.get()),
                        RecipeCategory.MISC,
                        ModItems.ZINC_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_raw_zinc", has(ModItems.RAW_ZINC.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_ingot_from_smelting_raw_zinc"));

// 2. Сырой цинк -> Цинковый слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.RAW_ZINC.get()),
                        RecipeCategory.MISC,
                        ModItems.ZINC_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_raw_zinc", has(ModItems.RAW_ZINC.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_ingot_from_blasting_raw_zinc"));

// 3. Руда -> Цинковый слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.ZINC_ORE.get(), ModBlocks.DEEPSLATE_ZINC_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.ZINC_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_zinc_ore", has(ModBlocks.ZINC_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_ingot_from_smelting_zinc_ore"));

// 4. Руда -> Цинковый слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModBlocks.ZINC_ORE.get(), ModBlocks.DEEPSLATE_ZINC_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.ZINC_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_zinc_ore", has(ModBlocks.ZINC_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_ingot_from_blasting_zinc_ore"));

        // 9 Рудного цинка -> Блок необработанного цинка
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_ZINC_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.RAW_ZINC.get())
                .unlockedBy("has_raw_zinc", has(ModItems.RAW_ZINC.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "raw_zinc_block_from_raw_zinc"));

// Блок необработанного цинка -> 9 Рудного цинка
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_ZINC.get(), 9)
                .requires(ModBlocks.RAW_ZINC_BLOCK.get())
                .unlockedBy("has_raw_zinc_block", has(ModBlocks.RAW_ZINC_BLOCK.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "raw_zinc_from_raw_zinc_block"));

        // ================= ПЕРЕПЛАВКА ОЛОВА (РУДА) =================

// Руда -> Оловянный слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.TIN_ORE.get(), ModBlocks.DEEPSLATE_TIN_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.TIN_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_tin_ore", has(ModBlocks.TIN_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "tin_ingot_from_smelting_tin_ore"));

// Руда -> Оловянный слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModBlocks.TIN_ORE.get(), ModBlocks.DEEPSLATE_TIN_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.TIN_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_tin_ore", has(ModBlocks.TIN_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "tin_ingot_from_blasting_tin_ore"));

// Сырой олово -> Оловянный слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_TIN.get()),
                        RecipeCategory.MISC,
                        ModItems.TIN_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_raw_tin", has(ModItems.RAW_TIN.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "tin_ingot_from_smelting_raw_tin"));

// Сырой олово -> Оловянный слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.RAW_TIN.get()),
                        RecipeCategory.MISC,
                        ModItems.TIN_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_raw_tin", has(ModItems.RAW_TIN.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "tin_ingot_from_blasting_raw_tin"));


// ================= ПЕРЕПЛАВКА СВИНЦА (РУДА) =================

// Руда -> Свинцовый слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModBlocks.LEAD_ORE.get(), ModBlocks.DEEPSLATE_LEAD_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_lead_ore", has(ModBlocks.LEAD_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "lead_ingot_from_smelting_lead_ore"));

// Руда -> Свинцовый слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModBlocks.LEAD_ORE.get(), ModBlocks.DEEPSLATE_LEAD_ORE.get()),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_lead_ore", has(ModBlocks.LEAD_ORE.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "lead_ingot_from_blasting_lead_ore"));

// Сырой свинец -> Свинцовый слиток (Обычная печь)
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.RAW_LEAD.get()),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT.get(),
                        0.7f,
                        200
                ).unlockedBy("has_raw_lead", has(ModItems.RAW_LEAD.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "lead_ingot_from_smelting_raw_lead"));

// Сырой свинец -> Свинцовый слиток (Плавильня)
        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.RAW_LEAD.get()),
                        RecipeCategory.MISC,
                        ModItems.LEAD_INGOT.get(),
                        0.7f,
                        100
                ).unlockedBy("has_raw_lead", has(ModItems.RAW_LEAD.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "lead_ingot_from_blasting_raw_lead"));

        // ================= КРАФТ ЦИНКОВОГО БЛОКА =================

// 9 Цинковых слитков -> Цинковый блок
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ZINC_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ZINC_INGOT.get())
                .unlockedBy("has_zinc_ingot", has(ModItems.ZINC_INGOT.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_block_from_ingots"));

// Цинковый блок -> 9 Цинковых слитков
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ZINC_INGOT.get(), 9)
                .requires(ModBlocks.ZINC_BLOCK.get())
                .unlockedBy("has_zinc_block", has(ModBlocks.ZINC_BLOCK.get()))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "zinc_ingots_from_zinc_block"));

        // Рецепт Большой плавильни
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.BIG_BLAST_FURNACE.get())
                .pattern("FBF")
                .pattern("BCB")
                .pattern("BBB")
                .define('F', Items.BLAST_FURNACE) // Плавильни по верхним углам
                .define('C', ModBlocks.BLAST_FURNACE_CASING)      // Печка посередине (в центре)
                .define('B', Items.BRICKS)       // Кирпичные блоки во всех остальных ячейках
                .unlockedBy("has_blast_furnace", has(Items.BLAST_FURNACE))
                .save(recipeOutput, ResourceLocation.fromNamespaceAndPath(MichMetallurgy.MOD_ID, "big_blast_furnace"));

        // Бронза: 3 меди + 1 олово = 3 бронзы
        BigBlastFurnaceRecipeBuilder.recipe(
                Ingredient.of(Items.RAW_COPPER), 3,
                Ingredient.of(ModItems.RAW_TIN.get()), 1,
                ModItems.BRONZE_INGOT.get(), 3,
                200,  // Время в тиках
                1.0f  // Количество опыта
        ).save(recipeOutput, "bronze_alloy");

// Латунь: 2 меди + 1 цинк = 2 латуни
        BigBlastFurnaceRecipeBuilder.recipe(
                Ingredient.of(Items.RAW_COPPER), 2,
                Ingredient.of(ModItems.RAW_ZINC.get()), 1,
                ModItems.BRASS_INGOT.get(), 2,
                166,  // Время в тиках
                1.0f  // Количество опыта
        ).save(recipeOutput, "brass_alloy");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BLAST_FURNACE_CASING.get())
                .pattern("IBI")
                .pattern("BFB")
                .pattern("IBI")
                .define('I', Items.IRON_INGOT) // Железный слиток
                .define('B', Items.BRICKS) // Блок кирпичей
                .define('F', Items.FURNACE) // печка
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT)) // Условие для открытия рецепта в книге
                .save(recipeOutput);

// === МЕДЬ ===
// 1 слиток -> 9 кусочков (Бесформенный крафт)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 9)
                .requires(Items.COPPER_INGOT)
                .unlockedBy("has_copper_ingot", has(Items.COPPER_INGOT))
                .save(recipeOutput, "copper_nugget_from_ingot");

// 9 кусочков -> 1 слиток (Форменный крафт 3x3)
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT)
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.COPPER_NUGGET.get())
                .unlockedBy("has_copper_nugget", has(ModItems.COPPER_NUGGET.get()))
                .save(recipeOutput, "copper_ingot_from_nuggets");


// === ОЛОВО ===
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TIN_NUGGET.get(), 9)
                .requires(ModItems.TIN_INGOT.get())
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput, "tin_nugget_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIN_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.TIN_NUGGET.get())
                .unlockedBy("has_tin_nugget", has(ModItems.TIN_NUGGET.get()))
                .save(recipeOutput, "tin_ingot_from_nuggets");


// === СВИНЕЦ ===
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.LEAD_NUGGET.get(), 9)
                .requires(ModItems.LEAD_INGOT.get())
                .unlockedBy("has_lead_ingot", has(ModItems.LEAD_INGOT.get()))
                .save(recipeOutput, "lead_nugget_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.LEAD_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.LEAD_NUGGET.get())
                .unlockedBy("has_lead_nugget", has(ModItems.LEAD_NUGGET.get()))
                .save(recipeOutput, "lead_ingot_from_nuggets");


// === ЦИНК ===
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ZINC_NUGGET.get(), 9)
                .requires(ModItems.ZINC_INGOT.get())
                .unlockedBy("has_zinc_ingot", has(ModItems.ZINC_INGOT.get()))
                .save(recipeOutput, "zinc_nugget_from_ingot");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ZINC_INGOT.get())
                .pattern("NNN")
                .pattern("NNN")
                .pattern("NNN")
                .define('N', ModItems.ZINC_NUGGET.get())
                .unlockedBy("has_zinc_nugget", has(ModItems.ZINC_NUGGET.get()))
                .save(recipeOutput, "zinc_ingot_from_nuggets");

        // ==========================================
// ЛАТУННЫЕ ИНСТРУМЕНТЫ (BRASS)
// ==========================================
// Меч
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRASS_SWORD.get())
                .pattern("M")
                .pattern("M")
                .pattern("S")
                .define('M', ModItems.BRASS_INGOT.get()) // M - Металл (Латунь)
                .define('S', Items.STICK)                // S - Палка (Stick)
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Кирка
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRASS_PICKAXE.get())
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .define('M', ModItems.BRASS_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Топор
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRASS_AXE.get())
                .pattern("MM")
                .pattern("MS")
                .pattern(" S")
                .define('M', ModItems.BRASS_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Лопата
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRASS_SHOVEL.get())
                .pattern("M")
                .pattern("S")
                .pattern("S")
                .define('M', ModItems.BRASS_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Мотыга
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRASS_HOE.get())
                .pattern("MM")
                .pattern(" S")
                .pattern(" S")
                .define('M', ModItems.BRASS_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);


// ==========================================
// БРОНЗОВЫЕ ИНСТРУМЕНТЫ (BRONZE)
// ==========================================
// Меч
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRONZE_SWORD.get())
                .pattern("M")
                .pattern("M")
                .pattern("S")
                .define('M', ModItems.BRONZE_INGOT.get()) // M - Металл (Бронза)
                .define('S', Items.STICK)
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Кирка
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRONZE_PICKAXE.get())
                .pattern("MMM")
                .pattern(" S ")
                .pattern(" S ")
                .define('M', ModItems.BRONZE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Топор
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRONZE_AXE.get())
                .pattern("MM")
                .pattern("MS")
                .pattern(" S")
                .define('M', ModItems.BRONZE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Лопата
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRONZE_SHOVEL.get())
                .pattern("M")
                .pattern("S")
                .pattern("S")
                .define('M', ModItems.BRONZE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Мотыга
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.BRONZE_HOE.get())
                .pattern("MM")
                .pattern(" S")
                .pattern(" S")
                .define('M', ModItems.BRONZE_INGOT.get())
                .define('S', Items.STICK)
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

        // Шлем
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRASS_HELMET.get())
                .pattern("MMM")
                .pattern("M M")
                .define('M', ModItems.BRASS_INGOT.get())
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Нагрудник
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRASS_CHESTPLATE.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.BRASS_INGOT.get())
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Поножи
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRASS_LEGGINGS.get())
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.BRASS_INGOT.get())
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

// Ботинки
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRASS_BOOTS.get())
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.BRASS_INGOT.get())
                .unlockedBy("has_brass_ingot", has(ModItems.BRASS_INGOT.get()))
                .save(recipeOutput);

        // Шлем
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRONZE_HELMET.get())
                .pattern("MMM")
                .pattern("M M")
                .define('M', ModItems.BRONZE_INGOT.get())
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Нагрудник
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRONZE_CHESTPLATE.get())
                .pattern("M M")
                .pattern("MMM")
                .pattern("MMM")
                .define('M', ModItems.BRONZE_INGOT.get())
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Поножи
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRONZE_LEGGINGS.get())
                .pattern("MMM")
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.BRONZE_INGOT.get())
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

// Ботинки
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BRONZE_BOOTS.get())
                .pattern("M M")
                .pattern("M M")
                .define('M', ModItems.BRONZE_INGOT.get())
                .unlockedBy("has_bronze_ingot", has(ModItems.BRONZE_INGOT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.TIN_ROTOR.get())
                .pattern(" I ")
                .pattern("INI")
                .pattern(" I ")
                .define('I', ModItems.TIN_INGOT.get()) // Оловянный слиток
                .define('N', ModItems.TIN_NUGGET.get()) // Оловянный кусочек
                .unlockedBy("has_tin_ingot", has(ModItems.TIN_INGOT.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.AIR_DUCT.get())
                .pattern("BRB")
                .pattern("B B")
                .pattern("BRB")
                .define('R', ModItems.TIN_ROTOR.get()) // Твой новый ротор
                .define('B', Items.BRICK) // Ванильный предмет кирпича (не блок)
                .unlockedBy("has_tin_rotor", has(ModItems.TIN_ROTOR.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.COKE_OVEN.get())
                .pattern("BBB")
                .pattern("BFB")
                .pattern("BBB")
                .define('B', Items.BRICKS)
                .define('F', Items.FURNACE)
                .unlockedBy("has_furnace", has(Items.FURNACE))
                .save(recipeOutput);

        BigBlastFurnaceRecipeBuilder.recipe(
                Ingredient.of(Items.RAW_IRON), 1,
                Ingredient.of(ModItems.COKE_COAL.get()), 1,
                ModItems.CAST_IRON_INGOT.get(), 1,
                266,
                1.0f
        ).save(recipeOutput, "cast_iron_alloy");
    }
}