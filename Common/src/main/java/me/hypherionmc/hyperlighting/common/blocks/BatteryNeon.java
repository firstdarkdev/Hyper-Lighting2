package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.craterlib.api.rendering.DyableBlock;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.util.BlockStateUtils;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.network.OpenGuiPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class BatteryNeon extends BaseEntityBlock implements DyableBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape DOWN_BOUNDING_BOX = Block.box(0, 0, 7.008, 16, 3.008, 8.992);
    private static final VoxelShape UP_BOUNDING_BOX = Block.box(0, 12.8, 7.008, 16, 16, 8.992);
    private static final VoxelShape SOUTH_BOUNDING_BOX = Block.box(0, 7.008, 12.992, 16, 8.992, 16);
    private static final VoxelShape EAST_BOUNDING_BOX = Block.box(0, 7.008, 16, 12.8, 8.992, 16);
    private static final VoxelShape WEST_BOUNDING_BOX = Block.box(0, 7.008, 0, 3.2, 8.992, 16);
    private static final VoxelShape NORTH_BOUNDING_BOX = Block.box(0, 7.008, 0.336, 16, 8.992, 3.328);

    public BatteryNeon(String name) {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(14)));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));

        HLItems.ITEMS.register(name, () -> new BlockItemDyable(this, new Item.Properties().tab(CommonRegistration.LIGHTS_TAB)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch (state.getValue(FACING)) {
            case UP:
                return DOWN_BOUNDING_BOX;
            case DOWN:
            default:
                return UP_BOUNDING_BOX;
            case NORTH:
                return SOUTH_BOUNDING_BOX;
            case EAST:
                return WEST_BOUNDING_BOX;
            case WEST:
                return EAST_BOUNDING_BOX;
            case SOUTH:
                return NORTH_BOUNDING_BOX;
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.isClientSide) {
            if (Screen.hasControlDown()) {
                OpenGuiPacket openGUIPacket = new OpenGuiPacket(11, pos);
                CommonRegistration.networkHandler.sendToServer(openGUIPacket);
                return InteractionResult.SUCCESS;
            }
        } else {
            if (state.getValue(LIT)) {
                sendBlockUpdate(state, pos, worldIn, false);
                return InteractionResult.SUCCESS;
            } else {
                if (worldIn.getBlockEntity(pos) != null && worldIn.getBlockEntity(pos) instanceof BatteryNeonBlockEntity be && be.getEnergyStorage().getPowerLevel() > 0) {
                    sendBlockUpdate(state, pos, worldIn, true);
                    return InteractionResult.SUCCESS;
                } else {
                    sendBlockUpdate(state, pos, worldIn, false);
                    player.displayClientMessage(Component.literal("Out of power"), true);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, FACING);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(LIT, false);

    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.getValue(LIT)) {
                if (world != null && world.getBlockEntity(pos) instanceof BatteryNeonBlockEntity be) {
                    return (be.getInventory().getItemHandler().getItem(1).getItem() instanceof DyeItem dyeItem ? dyeItem.getDyeColor().getMaterialColor().col : DyeColor.WHITE.getTextColor());
                }
            } else {
                return DyeColor.BLACK.getMaterialColor().col;
            }
            return DyeColor.BLACK.getMaterialColor().col;
        };
    }

    @Override
    public DyeColor defaultDyeColor() {
        return DyeColor.WHITE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.literal(ChatFormatting.YELLOW + "Dyable"));
        tooltip.add(Component.literal(ChatFormatting.GREEN + "Color: " + defaultDyeColor().name()));
        tooltip.add(Component.literal(ChatFormatting.BLUE + "Colored Lighting Supported"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BatteryNeonBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (level1, blockPos, blockState, t) -> {
            if (!level.isClientSide()) {
                if (t instanceof BatteryNeonBlockEntity tile) {
                    tile.tick(level1, blockPos, blockState, tile);
                }
            }
        };
    }

    private void sendBlockUpdate(BlockState state, BlockPos pos, Level worldIn, boolean litState) {
        BlockState oldState = state;
        state = state.setValue(LIT, litState);
        worldIn.setBlock(pos, state, 2);
        worldIn.sendBlockUpdated(pos, oldState, state, 4);
    }
}
