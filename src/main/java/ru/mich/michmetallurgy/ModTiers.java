package ru.mich.michmetallurgy;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class ModTiers {
    public static final Tier TIN = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL, // Уровень шахтёрской добычи (как у камня)
            170,                                // Прочность
            5.5F,                               // Скорость копания
            1.5F,                               // Базовый бонус к урону
            12,                                  // Зачаровываемость
            () -> Ingredient.of(ModItems.TIN_INGOT.get()) // Ремонтный материал — слиток олова
    );
    public static final Tier LEAD = new SimpleTier(
            BlockTags.INCORRECT_FOR_STONE_TOOL,
            220,          // Прочность (между камнем 131 и железом 250)
            4.5F,         // Скорость добычи
            1.8F,         // Бонус к урону
            12,           // Зачаровываемость
            () -> Ingredient.of(ModItems.LEAD_INGOT.get()) // Убедись, что свинцовый слиток называется LEAD_INGOT
    );
    public static final Tier BRASS = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL, // Уровень блоков, которые НЕЛЬЗЯ сломать
            428,                               // Прочность
            5.7f,                              // Скорость
            1.85f,                             // Базовый урон
            12,                                // Зачаровываемость (взял среднюю)
            () -> Ingredient.of(ModItems.BRASS_INGOT.get()) // Предмет для починки на наковальне
    );

    public static final Tier BRONZE = new SimpleTier(
            BlockTags.INCORRECT_FOR_IRON_TOOL,
            232,                               // Прочность
            6.5f,                              // Скорость
            2.25f,                             // Базовый урон
            12,                                // Зачаровываемость
            () -> Ingredient.of(ModItems.BRONZE_INGOT.get()) // Предмет для починки
    );
}