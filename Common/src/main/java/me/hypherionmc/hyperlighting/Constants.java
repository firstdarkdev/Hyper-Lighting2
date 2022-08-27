package me.hypherionmc.hyperlighting;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {

	public static final String MOD_ID = "hyperlighting";
	public static final String MOD_NAME = "Hyper Lighting 2";
	public static final Logger LOG = LogManager.getLogger(MOD_NAME);
	public static ResourceLocation rl(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static final String THE_ONE_PROBE = "theoneprobe";
}
