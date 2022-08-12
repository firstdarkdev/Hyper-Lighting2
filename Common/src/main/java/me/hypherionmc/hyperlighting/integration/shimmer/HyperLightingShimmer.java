package me.hypherionmc.hyperlighting.integration.shimmer;

import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import me.hypherionmc.craterlib.common.item.BlockItemDyable;
import me.hypherionmc.craterlib.util.RenderUtils;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedLanternBlock;
import me.hypherionmc.hyperlighting.common.blocks.AdvancedTorchBlock;
import me.hypherionmc.hyperlighting.common.init.HLBlocks;
import net.minecraft.world.item.DyeColor;

/**
 * @author HypherionSA
 * @date 07/08/2022
 */
public class HyperLightingShimmer {

    public static void registerAll() {
        registerBlocks();
        registerItems();
    }

    private static void registerItems() {
        LightManager.INSTANCE.registerItemLight(HLBlocks.ADVANCED_TORCH.asItem(), stack -> new ColorPointLight.Template(stack.getCount() / 10 + 6, RenderUtils.alphaColorFromDye(((BlockItemDyable)stack.getItem()).getColor(stack), 1f)));
        LightManager.INSTANCE.registerItemLight(HLBlocks.ADVANCED_LANTERN.asItem(), stack -> new ColorPointLight.Template(stack.getCount() / 10 + 6, RenderUtils.alphaColorFromDye(((BlockItemDyable)stack.getItem()).getColor(stack), 1f)));
    }

    private static void registerBlocks() {
        LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_TORCH.get(), (state, blockPos) -> {
            if (state.getValue(AdvancedTorchBlock.LIT)) {
                DyeColor color = state.getValue(AdvancedTorchBlock.COLOR);
                return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
            }
            return null;
        });

        LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_LANTERN.get(), (state, blockPos) -> {
            if (state.getValue(AdvancedLanternBlock.LIT)) {
                DyeColor color = state.getValue(AdvancedLanternBlock.COLOR);
                return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
            }
            return null;
        });
    }

}
