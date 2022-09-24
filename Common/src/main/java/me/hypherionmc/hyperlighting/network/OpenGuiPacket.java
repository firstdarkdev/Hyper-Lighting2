package me.hypherionmc.hyperlighting.network;

import me.hypherionmc.craterlib.network.CraterPacket;
import me.hypherionmc.craterlib.platform.Platform;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.containers.BatteryNeonContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class OpenGuiPacket implements CraterPacket<OpenGuiPacket> {

    private BlockPos posToSet;
    private int guiid;

    public OpenGuiPacket() {}

    public OpenGuiPacket(int id, BlockPos pos) {
        this.posToSet = pos;
        this.guiid = id;
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(posToSet);
        friendlyByteBuf.writeInt(guiid);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        this.posToSet = friendlyByteBuf.readBlockPos();
        this.guiid = friendlyByteBuf.readInt();
    }

    @Override
    public PacketHandler createHandler() {
        return new PacketHandler() {
            @Override
            public void handle(CraterPacket packet, Player player, Object o) {
                BlockEntity be = player.level.getBlockEntity(posToSet);

                MenuProvider containerProvider = new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        if (be instanceof BatteryNeonBlockEntity) {
                            return Component.translatable("block.hyperlighting.battery_neon");
                        }
                        return null;
                    }

                    @Nullable
                    @Override
                    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                        if (be instanceof BatteryNeonBlockEntity) {
                            return new BatteryNeonContainer(i, player.level, posToSet, inventory);
                        }
                        return null;
                    }
                };

                if (containerProvider.getDisplayName() != null) {
                    Platform.COMMON_HELPER.openMenu((ServerPlayer) player, containerProvider, buf -> buf.writeBlockPos(posToSet));
                }
            }
        };
    }
}
