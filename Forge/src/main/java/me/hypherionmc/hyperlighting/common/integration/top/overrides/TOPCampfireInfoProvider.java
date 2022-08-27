package me.hypherionmc.hyperlighting.common.integration.top.overrides;

import mcjty.theoneprobe.api.*;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.AdvancedCampfireBlockEntity;
import me.hypherionmc.hyperlighting.common.integration.top.TOPIntegration;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;
import static mcjty.theoneprobe.api.TextStyleClass.MODNAME;

/**
 * @author HypherionSA
 * @date 27/08/2022
 */
public class TOPCampfireInfoProvider implements IBlockDisplayOverride {

    @Override
    public boolean overrideStandardInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, Player playerEntity, Level world, BlockState blockState, IProbeHitData iProbeHitData) {
        IProbeConfig config = TOPIntegration.getProbeConfig();

        if (probeMode != ProbeMode.DEBUG) {
            if (world.getBlockEntity(iProbeHitData.getPos()) instanceof AdvancedCampfireBlockEntity tileCampFire) {
                String modName = ModList.get().getModContainerById(Constants.MOD_ID).get().getModInfo().getDisplayName();
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
                                        (int) ((float) tileCampFire.cookingTime[i] / tileCampFire.cookingProgress[i] * 100),
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
}
