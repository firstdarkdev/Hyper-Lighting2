package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.craterlib.api.rendering.CustomRenderType;
import me.hypherionmc.craterlib.api.rendering.DyableBlock;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.util.BlockStateUtils;
import me.hypherionmc.craterlib.util.MathUtils;
import me.hypherionmc.craterlib.util.RenderUtils;
import me.hypherionmc.hyperlighting.api.LightableBlock;
import me.hypherionmc.hyperlighting.common.init.CandleFlameParticles;
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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author HypherionSA
 * @date 06/08/2022
 */
public class AdvancedLanternBlock extends FaceAttachedHorizontalDirectionalBlock implements DyableBlock, LightableBlock, CustomRenderType {

    //region Properties
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty HORIZONTAL_FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    //endregion

    //region Bounding Boxes
    private static final VoxelShape BB_TOP = Block.box(5, 0, 5, 11, 9, 11);
    private static final VoxelShape BB_NORTH = Block.box(5, 3, 3, 11, 12, 9);
    private static final VoxelShape BB_SOUTH = MathUtils.rotateShape(Direction.NORTH, Direction.WEST, BB_NORTH);
    private static final VoxelShape BB_EAST = MathUtils.rotateShape(Direction.NORTH, Direction.SOUTH, BB_NORTH);
    private static final VoxelShape BB_WEST = MathUtils.rotateShape(Direction.NORTH, Direction.EAST, BB_NORTH);
    private static final VoxelShape BB_CEILING = Block.box(5, 4, 5, 11, 13, 11);
    //endregion

    private DyeColor color;

    public AdvancedLanternBlock(String name, DyeColor color, CreativeModeTab tab) {
        super(Properties.of(Material.HEAVY_METAL)
                .instabreak()
                .sound(SoundType.LANTERN)
                .lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(15))
        );
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, CommonRegistration.config.lanternConfig.litByDefault).setValue(COLOR, color));
        this.color = color;
        HLItems.register(name, () -> new BlockItemDyable(this, new Item.Properties().tab(tab)));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (blockState.getValue(FACE)) {
            case FLOOR -> BB_TOP;
            case WALL -> switch (blockState.getValue(HORIZONTAL_FACING)) {
                case EAST -> BB_EAST;
                case WEST -> BB_WEST;
                case SOUTH -> BB_SOUTH;
                case DOWN, UP -> null;
                case NORTH -> BB_NORTH;
            };
            case CEILING -> BB_CEILING;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, COLOR, HORIZONTAL_FACING, FACE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        for (Direction direction : Direction.values()) {
            if (!isValidPosition(stateIn, worldIn, currentPos, direction)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    public boolean isValidPosition(BlockState state, LevelReader worldIn, BlockPos pos, Direction direction) {
        return canSupportCenter(worldIn, pos.below(), direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state =  super.getStateForPlacement(context);
        return state != null ? state.setValue(LIT, false) : null;
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

    /** Check if player clicked the block with DYE and apply Color Tint **/
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide()) {
            if (!player.getItemInHand(handIn).isEmpty() && player.getItemInHand(handIn).getItem() instanceof DyeItem dyeItem) {
                state = state.setValue(COLOR, dyeItem.getDyeColor());
                this.color = dyeItem.getDyeColor();
                level.setBlock(pos, state, 3);
                level.sendBlockUpdated(pos, state, state, 3);

                if (!player.isCreative()) {
                    ItemStack stack = player.getItemInHand(handIn);
                    stack.shrink(1);
                    player.setItemInHand(handIn, stack);
                }
                return InteractionResult.CONSUME;
            } else if (!CommonRegistration.config.lanternConfig.requiresTool) {
                state = state.cycle(LIT);
                level.setBlock(pos, state, 3);
                level.sendBlockUpdated(pos, state, state, 3);
                if (!state.getValue(LIT)) {
                    level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.3f, 1.0f);
                } else {
                    level.playSound(null, pos, HLSounds.TORCH_IGNITE.get(), SoundSource.BLOCKS, 0.3f, 1.0f);
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void animateTick(BlockState state, Level worldIn, BlockPos pos, RandomSource rand) {
        if (worldIn.isClientSide && state.getValue(LIT)) {
            DyeColor color = state.getValue(COLOR);
            Direction direction = state.getValue(HORIZONTAL_FACING);

            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + 0.7D;
            double d2 = (double) pos.getZ() + 0.5D;

            if (state.getValue(FACE) == AttachFace.WALL) {
                Direction direction1 = direction.getOpposite();
                worldIn.addParticle(ParticleTypes.SMOKE, d0 + -0.13D * (double) direction1.getStepX(), d1 + -0.13D, d2 + -0.13D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(CandleFlameParticles.getParticleByColor(color).get(), d0 + -0.13D * (double) direction1.getStepX(), d1 - 0.18D, d2 + -0.13D * (double) direction1.getStepZ(), 0D, 0D, 0D);
            } else if (state.getValue(FACE) == AttachFace.FLOOR) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.3D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(CandleFlameParticles.getParticleByColor(color).get(), d0, d1 - 0.35D, d2, 0D, 0D, 0D);
            } else if (state.getValue(FACE) == AttachFace.CEILING) {
                worldIn.addParticle(ParticleTypes.SMOKE, d0, d1 - 0.1D, d2, 0.0D, 0.0D, 0.0D);
                worldIn.addParticle(CandleFlameParticles.getParticleByColor(color).get(), d0, d1 - 0.1D, d2, 0D, 0D, 0D);
            }
        }
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
