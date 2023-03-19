package me.hypherionmc.hyperlighting.common.blocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.hypherionmc.craterlib.api.inventory.CraterCreativeModeTab;
import me.hypherionmc.craterlib.api.rendering.DyableBlock;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.craterlib.util.BlockStateUtils;
import me.hypherionmc.craterlib.util.RenderUtils;
import me.hypherionmc.hyperlighting.api.LightableBlock;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.FlameParticles;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import me.hypherionmc.hyperlighting.util.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class AdvancedTorchBlock extends HorizontalDirectionalBlock implements DyableBlock, LightableBlock {

    //region Properties
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    public static final EnumProperty<AttachFace> ATTACH_FACE = EnumProperty.create("face", AttachFace.class, AttachFace.FLOOR, AttachFace.WALL);
    //endregion

    //region Bounding Boxes
    private static final Map<Direction, VoxelShape> SHAPES = Maps.newEnumMap(
            ImmutableMap.of(
                    Direction.NORTH, Block.box(5.5D, 2.0D, 11.0D, 10.5D, 12.0D, 16.0D),
                    Direction.SOUTH, Block.box(5.5D, 2.0D, 0.0D, 10.5D, 12.0D, 5.0D),
                    Direction.WEST, Block.box(11.0D, 2.0D, 5.5D, 16.0D, 12.0D, 10.5D),
                    Direction.EAST, Block.box(0.0D, 2.0D, 5.5D, 5.0D, 12.0D, 10.5D),
                    Direction.UP, Block.box(6.0D, 0.0D, 6.0D, 10.0D, 10.0D, 10.0D)
            )
    );
    //endregion

    private DyeColor color;

    public AdvancedTorchBlock(String name, DyeColor color, CraterCreativeModeTab tab) {
        super(Properties.of(Material.WOOD).noCollission().instabreak().lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(15)));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, CommonRegistration.config.torchConfig.litByDefault).setValue(COLOR, color));
        this.color = color;

        CreativeTabRegistry.setCreativeTab(tab, HLItems.register(name, () -> new BlockItemDyable(this, new Item.Properties())));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (blockState.getValue(ATTACH_FACE)) {
            case FLOOR -> SHAPES.get(Direction.UP);
            case WALL -> SHAPES.get(blockState.getValue(FACING));
            case CEILING -> null;
        };
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, ATTACH_FACE, FACING, COLOR);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getClickedFace();
        BlockState state = this.defaultBlockState();
        if (direction == Direction.UP) {
            state = state.setValue(ATTACH_FACE, AttachFace.FLOOR);
        } else {
            state = state.setValue(ATTACH_FACE, AttachFace.WALL).setValue(FACING, direction);
        }
        return state.setValue(LIT, CommonRegistration.config.torchConfig.litByDefault);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState neighbourState, LevelAccessor levelIn, BlockPos currentPos, BlockPos newPos) {
        if (facing == Direction.DOWN && !this.isValidPosition(stateIn, levelIn, currentPos, facing)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(stateIn, facing, neighbourState, levelIn, currentPos, newPos);
    }

    public boolean isValidPosition(BlockState state, LevelAccessor levelAccessor, BlockPos pos, Direction direction) {
        return canSupportCenter(levelAccessor, pos, direction);
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
            } else if (!CommonRegistration.config.torchConfig.requiresTool) {
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
    public void appendHoverText(ItemStack stack, BlockGetter level, List<Component> tooltip, TooltipFlag options) {
        tooltip.add(Component.literal(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(Component.literal(ChatFormatting.GREEN + "Color: " + color.getName()));
        super.appendHoverText(stack, level, tooltip, options);
    }

    @Override
    public void animateTick(BlockState stateIn, Level levelIn, BlockPos pos, RandomSource random) {
        if (stateIn.getValue(LIT)) {
            DyeColor color = stateIn.getValue(COLOR);

            if (stateIn.getValue(ATTACH_FACE) == AttachFace.FLOOR) {
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + 0.7D;
                double d2 = (double) pos.getZ() + 0.5D;
                levelIn.addParticle(ParticleTypes.SMOKE, d0, d1, d2, 0.0D, 0.0D, 0.0D);
                levelIn.addParticle(FlameParticles.getParticleByColor(color).get(), d0, d1, d2, 0D, 0D, 0D);
            } else {
                Direction direction = stateIn.getValue(FACING);
                double d0 = (double) pos.getX() + 0.5D;
                double d1 = (double) pos.getY() + 0.7D;
                double d2 = (double) pos.getZ() + 0.5D;
                Direction direction1 = direction.getOpposite();
                levelIn.addParticle(ParticleTypes.SMOKE, d0 + 0.37D * (double) direction1.getStepX(), d1 + 0.15D, d2 + 0.37D * (double) direction1.getStepZ(), 0.0D, 0.0D, 0.0D);
                levelIn.addParticle(FlameParticles.getParticleByColor(color).get(), d0 + 0.37D * (double) direction1.getStepX(), d1 + 0.15D, d2 + 0.37D * (double) direction1.getStepZ(), 0D, 0D, 0D);
            }
        }
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state) {
        return StackUtil.getColorStack(this, state.getValue(COLOR));
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootContext.Builder lootBuilder) {
        return List.of(StackUtil.getColorStack(this, blockState.getValue(COLOR)));
    }
}
