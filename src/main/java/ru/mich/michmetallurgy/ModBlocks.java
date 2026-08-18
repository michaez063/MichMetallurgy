package ru.mich.michmetallurgy;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static net.minecraft.world.item.Items.registerBlock;

public class ModBlocks {

    // Создаем регистратор блоков для твоего мода
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(MichMetallurgy.MODID);

    public static final DeferredBlock<Block> LEAD_BLOCK = BLOCKS.register("lead_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(4.0f, 6.0f)
                    .sound(SoundType.METAL)));

    // Регистрируем сам физический блок олова ("tin_block")
    public static final DeferredBlock<Block> TIN_BLOCK = BLOCKS.register("tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL) // Цвет на карте
                    .requiresCorrectToolForDrops() // Чтобы дропался только при добыче киркой
                    .strength(4.0F, 6.0F)
                    .sound(SoundType.METAL)));
    // Регистрируем физический блок оловянной руды
    public static final DeferredBlock<Block> TIN_ORE = BLOCKS.register("tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)));

    // Регистрируем физический блок свинцовой руды
    public static final DeferredBlock<Block> LEAD_ORE = BLOCKS.register("lead_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)
                    .sound(SoundType.STONE)));

    public static final DeferredBlock<Block> DEEPSLATE_LEAD_ORE = BLOCKS.register("deepslate_lead_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(4.5F, 3.0F)
                    .sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> DEEPSLATE_TIN_ORE = BLOCKS.register("deepslate_tin_ore",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .requiresCorrectToolForDrops()
                    .strength(4.5F, 3.0F)
                    .sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> RAW_LEAD_BLOCK = BLOCKS.register("raw_lead_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.STONE)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()));

    public static final DeferredBlock<Block> RAW_TIN_BLOCK = BLOCKS.register("raw_tin_block",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .sound(SoundType.STONE)
                    .strength(5.0F, 6.0F)
                    .requiresCorrectToolForDrops()));

    // Цинковая руда (каменная основа)
    public static final DeferredBlock<Block> ZINC_ORE = BLOCKS.register("zinc_ore",
            () -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
                            .requiresCorrectToolForDrops()
                            .strength(3.0F, 3.0F)
                            .sound(SoundType.STONE)                // Звук камня
                            .mapColor(MapColor.STONE)              // Цвет на карте (серый каменный)
            ));

    // Глубинносланцевая цинковая руда
    public static final DeferredBlock<Block> DEEPSLATE_ZINC_ORE = BLOCKS.register("deepslate_zinc_ore",
            () -> new DropExperienceBlock(
                    ConstantInt.of(0),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
                            .requiresCorrectToolForDrops()
                            .strength(4.5F, 3.0F)
                            .sound(SoundType.DEEPSLATE)            // Звук глубинного сланца
                            .mapColor(MapColor.DEEPSLATE)          // Цвет на карте (тёмно-серый)
            ));
    // 3. Цинковый блок
    public static final DeferredBlock<Block> ZINC_BLOCK = BLOCKS.register("zinc_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .mapColor(MapColor.METAL)
            ));

    // 4. Блок необработанного цинка
    public static final DeferredBlock<Block> RAW_ZINC_BLOCK = BLOCKS.register("raw_zinc_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RAW_IRON_BLOCK)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.RAW_IRON)
            ));

    public static final DeferredBlock<Block> BIG_BLAST_FURNACE = BLOCKS.register("big_blast_furnace",
            () -> new BigBlastFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(4.0f, 7.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.COLOR_BROWN)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(BigBlastFurnaceBlock.LIT) ? 13 : 0)
            ));

    // Корпус плавильной печи
    public static final DeferredBlock<Block> BLAST_FURNACE_CASING = BLOCKS.registerSimpleBlock(
            "blast_furnace_casing",
            BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .strength(3.5F, 3.5F) // Прочность как у обычной печи
                    .sound(SoundType.STONE)
    );

    public static final DeferredBlock<Block> COKE_OVEN = BLOCKS.register("coke_oven",
            () -> new CokeOvenBlock(BlockBehaviour.Properties.of()
                    .strength(3.5f, 6.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.COLOR_BLACK)
                    .requiresCorrectToolForDrops()
            ));

    public static final DeferredBlock<Block> PUDDLING_FURNACE = BLOCKS.register("puddling_furnace",
            () -> new PuddlingFurnaceBlock(BlockBehaviour.Properties.of()
                    .strength(4.0f, 7.0f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.COLOR_GRAY)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(PuddlingFurnaceBlock.LIT) ? 13 : 0)
            ));

    // Метод для подключения регистратора к главной шине мода
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}