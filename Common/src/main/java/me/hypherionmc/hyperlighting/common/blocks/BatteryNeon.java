package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.craterlib.api.rendering.DyableBlock;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.craterlib.util.BlockStateUtils;
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
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
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

    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    private static final VoxelShape DOWN_BOUNDING_BOX = Block.box(0, 0.005, 7, 16, 3.005, 9);
    private static final VoxelShape UP_BOUNDING_BOX = Block.box(0, 12.995, 7, 16, 15.995, 9);
    private static final VoxelShape SOUTH_BOUNDING_BOX = Block.box(0, 7, 12.995, 16, 9, 15.995);
    private static final VoxelShape EAST_BOUNDING_BOX = Block.box(12.995, 7, 0, 15.995, 9, 16);
    private static final VoxelShape WEST_BOUNDING_BOX = Block.box(0.005, 7, 0, 3.005, 9, 16);
    private static final VoxelShape NORTH_BOUNDING_BOX = Block.box(0, 7, 0.005, 16, 9, 3.005);

    public BatteryNeon(String name) {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(14)));
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(COLOR, DyeColor.WHITE));

        CreativeTabRegistry.setCreativeTab(CommonRegistration.LIGHTS_TAB, HLItems.register(name, () -> new BlockItemDyable(this, new Item.Properties())));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case UP -> DOWN_BOUNDING_BOX;
            case DOWN -> UP_BOUNDING_BOX;
            case NORTH -> SOUTH_BOUNDING_BOX;
            case EAST -> WEST_BOUNDING_BOX;
            case WEST -> EAST_BOUNDING_BOX;
            case SOUTH -> NORTH_BOUNDING_BOX;
        };
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
        builder.add(LIT, FACING, COLOR);
        super.createBlockStateDefinition(builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(LIT, false).setValue(COLOR, DyeColor.WHITE);

    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockColor dyeHandler() {
        return (state, world, pos, tintIndex) -> {
            if (state.getValue(LIT)) {
                return state.getValue(COLOR).getMaterialColor().col;
            } else {
                return DyeColor.BLACK.getMaterialColor().col;
            }
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
