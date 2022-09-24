package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.network.PacketDirection;
import me.hypherionmc.hyperlighting.network.OpenGuiPacket;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class HLPackets {

    public static void registerServer() {
        CommonRegistration.networkHandler.registerPacket(OpenGuiPacket.class, OpenGuiPacket::new, PacketDirection.TO_SERVER);
    }

    public static void registerClient() {

    }

}
