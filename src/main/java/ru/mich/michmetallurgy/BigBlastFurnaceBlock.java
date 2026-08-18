package ru.mich.michmetallurgy;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.RandomSource;

public class BigBlastFurnaceBlock extends BaseEntityBlock {

    // Свойства блока: направление (куда смотрит) и горит ли печь (для текстуры/частиц)
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    // В 1.21.1 BaseEntityBlock требует наличие кодека
    public static final MapCodec<BigBlastFurnaceBlock> CODEC = simpleCodec(BigBlastFurnaceBlock::new);

    // BigBlastFurnaceBlock.java (конструктор)
    public BigBlastFurnaceBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)); // По умолчанию печь не горит
    }

// Убедись, что в ModBlocks.java при регистрации ты прописал .lightLevel():
// Например: .lightLevel(state -> state.getValue(BigBlastFurnaceBlock.LIT) ? 13 : 0)

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // 1. Создаем нашу сущность при установке блока
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BigBlastFurnaceBlockEntity(pos, state);
    }

    // 2. Без этого метода блок будет невидимым (BaseEntityBlock по умолчанию прячет модель)
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // 3. Открываем наше меню при клике ПКМ
    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BigBlastFurnaceBlockEntity furnaceEntity) {
                player.openMenu(furnaceEntity, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    // 4. Подключаем метод tick() из нашего BlockEntity, чтобы печь работала
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null; // На клиенте тики нам не нужны
        }
        return createTickerHelper(type, ModBlockEntities.BIG_BLAST_FURNACE_BE.get(),
                (lvl, pos, st, entity) -> BigBlastFurnaceBlockEntity.tick(lvl, pos, st, entity));
    }

    // 5. Если блок сломали — выбрасываем все ресурсы из слотов
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof BigBlastFurnaceBlockEntity furnaceEntity) {
                furnaceEntity.drops();
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    // --- Настройка состояний (поворот и горение) ---
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        // Получаем направление взгляда игрока, разворачиваем блок лицом к нему (getOpposite)
        // и задаем базовое состояние "выключено" (LIT = false), если оно у тебя есть по умолчанию
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(LIT, false); // Удали эту строчку, если LIT ставится как-то иначе
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        // Проверяем, горит ли печь
        if (state.getValue(BlockStateProperties.LIT)) {
            // Центр блока
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY();
            double z = (double) pos.getZ() + 0.5D;

            // 1. Воспроизводим звук потрескивания (с шансом 10%)
            if (random.nextDouble() < 0.1D) {
                level.playLocalSound(x, y, z, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 2.5F, 1.0F, false);
            }

            // 2. Получаем направление, куда смотрит лицевая сторона печи
            Direction direction = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Direction.Axis axis = direction.getAxis();

            // Небольшой случайный разброс вправо/влево, чтобы частицы не шли ровным столбом
            double randomOffset = random.nextDouble() * 0.6D - 0.3D;

            // Вычисляем точные координаты, чтобы вынести частицы на переднюю грань блока (0.52D от центра)
            double xOffset = axis == Direction.Axis.X ? (double) direction.getStepX() * 0.52D : randomOffset;
            double yOffset = random.nextDouble() * 9.0D / 16.0D; // Высота топки от пола
            double zOffset = axis == Direction.Axis.Z ? (double) direction.getStepZ() * 0.52D : randomOffset;

            // 3. Спавним дым и пламя
            level.addParticle(ParticleTypes.SMOKE, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
            level.addParticle(ParticleTypes.SMALL_FLAME, x + xOffset, y + yOffset, z + zOffset, 0.0D, 0.0D, 0.0D);
        }
    }
}