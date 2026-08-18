package ru.mich.michmetallurgy;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "michmetallurgy", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Простые плоские предметы (слитки и сырая руда)
        // Ищет текстуру по пути: assets/michmetallurgy/textures/item/название.png
        basicItem(ModItems.LEAD_INGOT.get());
        basicItem(ModItems.TIN_INGOT.get());

        basicItem(ModItems.RAW_LEAD.get());
        basicItem(ModItems.RAW_TIN.get());

        // Оловянные инструменты
        basicItem(ModItems.TIN_SWORD.get());
        basicItem(ModItems.TIN_PICKAXE.get());
        basicItem(ModItems.TIN_AXE.get());
        basicItem(ModItems.TIN_SHOVEL.get());
        basicItem(ModItems.TIN_HOE.get());

        // Свинцовые инструменты
        basicItem(ModItems.LEAD_SWORD.get());
        basicItem(ModItems.LEAD_PICKAXE.get());
        basicItem(ModItems.LEAD_AXE.get());
        basicItem(ModItems.LEAD_SHOVEL.get());
        basicItem(ModItems.LEAD_HOE.get());

        // Оловянная броня
        basicItem(ModItems.TIN_HELMET.get());
        basicItem(ModItems.TIN_CHESTPLATE.get());
        basicItem(ModItems.TIN_LEGGINGS.get());
        basicItem(ModItems.TIN_BOOTS.get());

        // Свинцовая броня
        basicItem(ModItems.LEAD_HELMET.get());
        basicItem(ModItems.LEAD_CHESTPLATE.get());
        basicItem(ModItems.LEAD_LEGGINGS.get());
        basicItem(ModItems.LEAD_BOOTS.get());

        basicItem(ModItems.ZINC_INGOT.get());
        basicItem(ModItems.RAW_ZINC.get());
        basicItem(ModItems.BRASS_INGOT.get());
        basicItem(ModItems.BRONZE_INGOT.get());

        basicItem(ModItems.COPPER_NUGGET.get());
        basicItem(ModItems.TIN_NUGGET.get());
        basicItem(ModItems.LEAD_NUGGET.get());
        basicItem(ModItems.ZINC_NUGGET.get());

        // Латунные инструменты
        basicItem(ModItems.BRASS_SWORD.get());
        basicItem(ModItems.BRASS_PICKAXE.get());
        basicItem(ModItems.BRASS_AXE.get());
        basicItem(ModItems.BRASS_SHOVEL.get());
        basicItem(ModItems.BRASS_HOE.get());

        // Бронзовые инструменты
        basicItem(ModItems.BRONZE_SWORD.get());
        basicItem(ModItems.BRONZE_PICKAXE.get());
        basicItem(ModItems.BRONZE_AXE.get());
        basicItem(ModItems.BRONZE_SHOVEL.get());
        basicItem(ModItems.BRONZE_HOE.get());

        // === ЛАТУННАЯ БРОНЯ ===
        basicItem(ModItems.BRASS_HELMET.get());
        basicItem(ModItems.BRASS_CHESTPLATE.get());
        basicItem(ModItems.BRASS_LEGGINGS.get());
        basicItem(ModItems.BRASS_BOOTS.get());

// === БРОНЗОВАЯ БРОНЯ ===
        basicItem(ModItems.BRONZE_HELMET.get());
        basicItem(ModItems.BRONZE_CHESTPLATE.get());
        basicItem(ModItems.BRONZE_LEGGINGS.get());
        basicItem(ModItems.BRONZE_BOOTS.get());

        basicItem(ModItems.TIN_ROTOR.get());
        basicItem(ModItems.AIR_DUCT.get());

        basicItem(ModItems.COKE_COAL.get());
        basicItem(ModItems.CAST_IRON_INGOT.get());
        basicItem(ModItems.STEEL_INGOT.get());
    }
}