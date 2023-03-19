package me.hypherionmc.hyperlighting.integration.shimmer;

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
        //LightManager.INSTANCE.registerItemLight(HLBlocks.ADVANCED_TORCH.asItem(), stack -> new ColorPointLight.Template(stack.getCount() / 10 + 6, RenderUtils.alphaColorFromDye(((BlockItemDyable)stack.getItem()).getColor(stack), 1f)));
        //LightManager.INSTANCE.registerItemLight(HLBlocks.ADVANCED_LANTERN.asItem(), stack -> new ColorPointLight.Template(stack.getCount() / 10 + 6, RenderUtils.alphaColorFromDye(((BlockItemDyable)stack.getItem()).getColor(stack), 1f)));

        //LightManager.INSTANCE.registerItemLight(HLItems.CANDLE_IN_A_JAR.get(), stack -> new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(DyeColor.WHITE, 1f)));
    }

    private static void registerBlocks() {
        /*LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_TORCH.get(), (state, blockPos) -> {
            if (state.getValue(AdvancedTorchBlock.LIT) && CommonRegistration.config.torchConfig.coloredLighting) {
                DyeColor color = state.getValue(AdvancedTorchBlock.COLOR);
                return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
            }
            return null;
        });

        LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_LANTERN.get(), (state, blockPos) -> {
            if (state.getValue(AdvancedLanternBlock.LIT) && CommonRegistration.config.lanternConfig.coloredLighting) {
                DyeColor color = state.getValue(AdvancedLanternBlock.COLOR);
                return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
            }
            return null;
        });

        LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_CAMPFIRE.get(), (state, blockPos) -> {
          if (state.getValue(AdvancedCampfire.LIT) && CommonRegistration.config.campfireConfig.coloredLighting) {
              DyeColor color = state.getValue(AdvancedCampfire.COLOR);
              return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
          }
            return null;
        });

        LightManager.INSTANCE.registerBlockLight(HLBlocks.ADVANCED_CANDLE.get(), (state, blockPos) -> {
            if (state.getValue(AdvancedCandleBlock.LIT) && CommonRegistration.config.candleConfig.coloredLighting) {
                DyeColor color = state.getValue(AdvancedCandleBlock.COLOR);
                return new ColorPointLight.Template(10, RenderUtils.alphaColorFromDye(color, 1f));
            }
            return null;
        });*/
    }

}
