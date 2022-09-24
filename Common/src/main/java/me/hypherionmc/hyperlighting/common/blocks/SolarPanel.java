package me.hypherionmc.hyperlighting.common.blocks;

import me.hypherionmc.hyperlighting.common.blockentities.SolarPanelBlockEntity;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
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
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 * @date 18/09/2022
 */
public class SolarPanel extends BaseEntityBlock {

    private final VoxelShape BOUNDS = Block.box(0, 0, 0, 16, 1.92, 16);

    public SolarPanel(String name) {
        super(Properties.of(Material.GLASS).sound(SoundType.GLASS).noCollission().noOcclusion());

        HLItems.register(name, () -> new BlockItem(this, new Item.Properties().tab(CommonRegistration.MACHINES_TAB)));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOUNDS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (level1, blockPos, blockState1, t) -> {
          if (!level1.isClientSide() && t instanceof SolarPanelBlockEntity be) {
            be.tick(level1, blockPos, blockState1, be);
          }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new SolarPanelBlockEntity(blockPos, blockState);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
