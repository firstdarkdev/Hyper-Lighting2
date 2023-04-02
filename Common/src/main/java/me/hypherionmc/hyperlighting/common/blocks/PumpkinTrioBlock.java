package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.craterlib.util.BlockStateUtils;
import me.hypherionmc.hyperlighting.api.LightableBlock;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import me.hypherionmc.hyperlighting.common.init.HLSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class PumpkinTrioBlock extends Block implements LightableBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 0.256, 16);

    public PumpkinTrioBlock(String name) {
        super(Properties.of(Material.VEGETABLE).sound(SoundType.LILY_PAD).lightLevel(BlockStateUtils.createLightLevelFromLitBlockState(10)));
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, CommonRegistration.config.pumpkinTrioConfig.litByDefault).setValue(FACING, Direction.NORTH));

        CreativeTabRegistry.setCreativeTab(CommonRegistration.LIGHTS_TAB, HLItems.register(name, () -> new BlockItem(this, new Item.Properties())));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, LIT);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getPlayer().getDirection();
        return this.defaultBlockState().setValue(FACING, face).setValue(LIT, CommonRegistration.config.pumpkinTrioConfig.litByDefault);
    }
}
