package me.hypherionmc.hyperlighting.common.items;

import me.hypherionmc.craterlib.systems.internal.CreativeTabRegistry;
import me.hypherionmc.hyperlighting.api.LightableBlock;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.hyperlighting.common.init.HLItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;

public class LighterTool extends Item {

    public LighterTool() {
        super(new Properties().stacksTo(1)/*.tab(CommonRegistration.LIGHTS_TAB)*/.durability(20));
        CreativeTabRegistry.setCreativeTab(CommonRegistration.LIGHTS_TAB, HLItems.TORCH_TOOL);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (!context.getLevel().isClientSide()) {
            BlockState block = context.getLevel().getBlockState(context.getClickedPos());
            if (block.getBlock() instanceof LightableBlock lightableBlock) {
                lightableBlock.toggleLight(context.getLevel(), block, context.getClickedPos());
            }
        }
        return InteractionResult.CONSUME;
    }
}
