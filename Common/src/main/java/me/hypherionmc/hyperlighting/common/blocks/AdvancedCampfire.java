package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.craterlib.api.inventory.CraterCreativeModeTab;
import me.hypherionmc.craterlib.api.rendering.CustomRenderType;
import me.hypherionmc.craterlib.api.rendering.DyableBlock;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.craterlib.util.BlockStateUtils;
import me.hypherionmc.craterlib.util.RenderUtils;
import me.hypherionmc.hyperlighting.api.LightableBlock;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.util.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class AdvancedCampfire extends BaseEntityBlock implements DyableBlock, LightableBlock, CustomRenderType {

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private final boolean spawnParticles;
    private final int fireDamage;

    private DyeColor color;

    public AdvancedCampfire(String name, DyeColor color, CraterCreativeModeTab tab) {
        super(Properties.of(
                Material.WOOD,
                MaterialColor.COLOR_BROWN)
                .strength(2.0f)
                .noOcclusion()
                .sound(SoundType.WOOD)
                .lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(15)));

        this.spawnParticles = true;
        this.fireDamage = 1;
        this.color = color;
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, CommonRegistration.config.campfireConfig.litByDefault).setValue(SIGNAL_FIRE, false).setValue(FACING, Direction.NORTH).setValue(COLOR, color));

        CreativeTabRegistry.setCreativeTab(tab, HLItems.register(name, () -> new BlockItemDyable(this, new Item.Properties())));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AdvancedCampfireBlockEntity campfireBlockEntity) {
                ItemStack itemStack = player.getItemInHand(interactionHand);
                Optional<CampfireCookingRecipe> optional = campfireBlockEntity.getCookableRecipe(itemStack);
                if (optional.isPresent()) {
                    if (campfireBlockEntity.placeFood(player, player.getAbilities().instabuild ? itemStack.copy() : itemStack, optional.get().getCookingTime())) {
                        player.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                        return InteractionResult.SUCCESS;
                    }

                    return InteractionResult.CONSUME;
                }
            }

            if (!player.getItemInHand(interactionHand).isEmpty() && player.getItemInHand(interactionHand).getItem() instanceof DyeItem dyeItem) {
                blockState = blockState.setValue(COLOR, dyeItem.getDyeColor());
                this.color = dyeItem.getDyeColor();
                level.setBlock(blockPos, blockState, 3);
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);

                if (!player.isCreative()) {
                    ItemStack stack = player.getItemInHand(interactionHand);
                    stack.shrink(1);
                    player.setItemInHand(interactionHand, stack);
                }
                return InteractionResult.CONSUME;
            } else if (!CommonRegistration.config.campfireConfig.requiresTool) {
                blockState = blockState.cycle(LIT);
                level.setBlock(blockPos, blockState, 3);
                level.sendBlockUpdated(blockPos, blockState, blockState, 3);
                if (!blockState.getValue(LIT)) {
                    level.playSound(null, blockPos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1.0f);
                } else {
                    level.playSound(null, blockPos, HLSounds.TORCH_IGNITE.get(), SoundSource.BLOCKS, 0.3f, 1.0f);
                }
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (blockState.getValue(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entity)) {
            entity.hurt(level.damageSources().inFire(), (float)this.fireDamage);
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof AdvancedCampfireBlockEntity campfireBlockEntity) {
                Containers.dropContents(level, blockPos, campfireBlockEntity.getItems());
            }

            super.onRemove(blockState, level, blockPos, blockState2, bl);
        }
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        LevelAccessor levelAccessor = blockPlaceContext.getLevel();
        BlockPos blockPos = blockPlaceContext.getClickedPos();
        boolean bl = levelAccessor.getFluidState(blockPos).getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(SIGNAL_FIRE, this.isSmokeSource(levelAccessor.getBlockState(blockPos.below()))).setValue(LIT, !bl && CommonRegistration.config.campfireConfig.litByDefault).setValue(FACING, blockPlaceContext.getHorizontalDirection());
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return direction == Direction.DOWN ? blockState.setValue(SIGNAL_FIRE, this.isSmokeSource(blockState2)) : super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);
    }

    private boolean isSmokeSource(BlockState blockState) {
        return blockState.is(Blocks.HAY_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        if (blockState.getValue(LIT)) {
            if (randomSource.nextInt(10) == 0) {
                level.playLocalSound((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + randomSource.nextFloat(), randomSource.nextFloat() * 0.7F + 0.6F, false);
            }

            if (this.spawnParticles && randomSource.nextInt(5) == 0) {
                for(int i = 0; i < randomSource.nextInt(1) + 1; ++i) {
                    level.addParticle(ParticleTypes.LAVA, (double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5, (double)(randomSource.nextFloat() / 2.0F), 5.0E-5, (double)(randomSource.nextFloat() / 2.0F));
                }
            }
        }
    }

    public static void dowse(@Nullable Entity entity, LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState) {
        if (levelAccessor.isClientSide()) {
            for(int i = 0; i < 20; ++i) {
                makeParticles((Level)levelAccessor, blockPos, blockState.getValue(SIGNAL_FIRE), true);
            }
        }

        BlockEntity blockEntity = levelAccessor.getBlockEntity(blockPos);
        if (blockEntity instanceof AdvancedCampfireBlockEntity campfireBlockEntity) {
            campfireBlockEntity.dowse();
        }
        levelAccessor.gameEvent(entity, GameEvent.BLOCK_CHANGE, blockPos);
    }

    public static void makeParticles(Level level, BlockPos blockPos, boolean bl, boolean bl2) {
        RandomSource randomSource = level.getRandom();
        SimpleParticleType simpleParticleType = bl ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        level.addAlwaysVisibleParticle(simpleParticleType, true, (double)blockPos.getX() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), (double)blockPos.getY() + randomSource.nextDouble() + randomSource.nextDouble(), (double)blockPos.getZ() + 0.5 + randomSource.nextDouble() / 3.0 * (double)(randomSource.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        if (bl2) {
            level.addParticle(ParticleTypes.SMOKE, (double)blockPos.getX() + 0.5 + randomSource.nextDouble() / 4.0 * (double)(randomSource.nextBoolean() ? 1 : -1), (double)blockPos.getY() + 0.4, (double)blockPos.getZ() + 0.5 + randomSource.nextDouble() / 4.0 * (double)(randomSource.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
        }
    }


    public static boolean isLitCampfire(BlockState blockState) {
        return blockState.hasProperty(LIT) && blockState.is(BlockTags.CAMPFIRES) && blockState.getValue(LIT);
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, SIGNAL_FIRE, FACING, COLOR);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new AdvancedCampfireBlockEntity(blockPos, blockState);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return (level1, blockPos, blockState1, t) -> {
            if (t instanceof AdvancedCampfireBlockEntity be) {
                if (level1.isClientSide() && blockState1.getValue(LIT)) {
                    be.clientTick(level1, blockPos, blockState1, t);
                } else if (!level1.isClientSide() && blockState1.getValue(LIT)) {
                    be.serverTick(level1, blockPos, blockState1, t);
                }
            }
        };
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public void toggleLight(Level worldIn, BlockState state, BlockPos pos) {
        state = state.setValue(LIT, !state.getValue(LIT));
        worldIn.setBlock(pos, state, 2);
        if (!state.getValue(LIT)) {
            worldIn.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1.0f);
        } else {
            worldIn.playSound(null, pos, HLSounds.TORCH_IGNITE.get(), SoundSource.BLOCKS, 0.3f, 1.0f);
        }
        worldIn.blockUpdated(pos, this);
    }

    @Override
    public BlockColor dyeHandler() {
        return ((blockState, blockAndTintGetter, blockPos, i) -> {
            if (blockState.getValue(LIT)) {
                return RenderUtils.renderColorFromDye(blockState.getValue(COLOR));
            } else {
                return RenderUtils.renderColorFromDye(DyeColor.BLACK);
            }
        });
    }

    @Override
    public DyeColor defaultDyeColor() {
        return this.defaultBlockState().getValue(COLOR);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Component.literal(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(Component.literal(ChatFormatting.GREEN + "Color: " + color.getName()));
        super.appendHoverText(stack, level, tooltip, options);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return StackUtil.getColorStack(this, state.getValue(COLOR));
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
        return List.of(StackUtil.getColorStack(this, blockState.getValue(COLOR)));
    }

    @Override
    public RenderType getCustomRenderType() {
        return RenderType.cutoutMipped();
    }
}
