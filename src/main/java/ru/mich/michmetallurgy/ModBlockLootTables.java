package ru.mich.michmetallurgy;

import ru.mich.michmetallurgy.ModItems; // Проверь путь к твоим предметам
import ru.mich.michmetallurgy.ModBlocks; // Проверь путь к твоим блокам

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {

    public ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        // 1. Руды (при ломании выпадает сырой металл):
        this.add(ModBlocks.TIN_ORE.get(),
                block -> createOreDrop(block, ModItems.RAW_TIN.get()));

        this.add(ModBlocks.LEAD_ORE.get(),
                block -> createOreDrop(block, ModItems.RAW_LEAD.get()));

        add(ModBlocks.DEEPSLATE_LEAD_ORE.get(),
                block -> createOreDrop(block, ModItems.RAW_LEAD.get()));

        add(ModBlocks.DEEPSLATE_TIN_ORE.get(),
                block -> createOreDrop(block, ModItems.RAW_TIN.get()));

        this.add(ModBlocks.ZINC_ORE.get(),
                block -> createOreDrop(ModBlocks.ZINC_ORE.get(), ModItems.RAW_ZINC.get()));
        this.add(ModBlocks.DEEPSLATE_ZINC_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_ZINC_ORE.get(), ModItems.RAW_ZINC.get()));

        // 2. Обычные металлические блоки (выпадают сами из себя):
        this.dropSelf(ModBlocks.TIN_BLOCK.get());
        this.dropSelf(ModBlocks.LEAD_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_TIN_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_LEAD_BLOCK.get());
        this.dropSelf(ModBlocks.ZINC_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_ZINC_BLOCK.get());
        // Большая плавильня выпадает сама при разрушении
        this.dropSelf(ModBlocks.BIG_BLAST_FURNACE.get());
        this.dropSelf(ModBlocks.BLAST_FURNACE_CASING.get());
        this.dropSelf(ModBlocks.COKE_OVEN.get());
        this.dropSelf(ModBlocks.PUDDLING_FURNACE.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(holder -> (Block) holder.value())::iterator;
    }
}