package me.hypherionmc.hyperlighting.common.integration.top.overrides;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
/*public class TOPCampfireInfoProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {
        IProbeConfig config = TOPIntegration.getProbeConfig();

        if (probeMode != ProbeMode.DEBUG) {
            if (world.getBlockEntity(iProbeHitData.getPos()) instanceof AdvancedCampfireBlockEntity tileCampFire) {
                String modName = FabricLoader.getInstance().getModContainer(Constants.MOD_ID).get().getMetadata().getName();
                iProbeInfo
                        .horizontal()
                        .item(iProbeHitData.getPickBlock())
                        .vertical()
                        .text(CompoundText.create().name(blockState.getBlock().getName()))
                        .vertical()
                        .text(CompoundText.create().info(MODNAME + modName));

                for (int i = 0; i < tileCampFire.getItems().size(); i++) {
                    ItemStack stack = tileCampFire.getItems().get(i);
                    if (!stack.isEmpty()) {
                        iProbeInfo
                                .horizontal()
                                .item(stack)
                                .horizontal()
                                .progress(
                                        (int) ((float) tileCampFire.cookingProgress[i] / tileCampFire.cookingTime[i] * 100),
                                        100,
                                        iProbeInfo.defaultProgressStyle().suffix(" %").alignment(ElementAlignment.ALIGN_TOPLEFT));
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}*/
