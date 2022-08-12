package me.hypherionmc.hyperlighting.common.config;

import me.hypherionmc.craterlib.common.config.ModuleConfig;
import me.hypherionmc.craterlib.common.config.annotations.SubConfig;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.init.CommonRegistration;
import me.hypherionmc.nightconfig.core.conversion.Path;
import me.hypherionmc.nightconfig.core.conversion.SpecComment;

public class HyperLightingConfig extends ModuleConfig {

    @Path("torchConfig")
    @SpecComment("Torch Configuration")
    @SubConfig
    public TorchConfig torchConfig = new TorchConfig();

    @Path("lanternConfig")
    @SpecComment("Lantern Configuration")
    @SubConfig
    public LanternConfig lanternConfig = new LanternConfig();

    @Path("candleConfig")
    @SpecComment("Candle Configuration")
    @SubConfig
    public CandleConfig candleConfig = new CandleConfig();

    public HyperLightingConfig() {
        super(Constants.MOD_ID, "hyperlighting-common");
        registerAndSetup(this);
    }

    @Override
    public void configReloaded() {
        CommonRegistration.config = loadConfig(this);
    }

    public static class TorchConfig {
        @Path("litByDefault")
        @SpecComment("Should torches be lit by default when placed")
        public boolean litByDefault = false;

        @Path("requiresTool")
        @SpecComment("Is the Torch Lighter tool needed to light torches")
        public boolean requiresTool = true;
    }

    public static class LanternConfig {
        @Path("litByDefault")
        @SpecComment("Should Lanterns be lit by default when placed")
        public boolean litByDefault = false;

        @Path("requiresTool")
        @SpecComment("Is the Torch Lighter tool needed to light Lanterns")
        public boolean requiresTool = true;
    }

    public static class CandleConfig {
        @Path("litByDefault")
        @SpecComment("Should Candles be lit by default when placed")
        public boolean litByDefault = false;

        @Path("requiresTool")
        @SpecComment("Is the Torch Lighter tool needed to light Candles")
        public boolean requiresTool = true;
    }
}
