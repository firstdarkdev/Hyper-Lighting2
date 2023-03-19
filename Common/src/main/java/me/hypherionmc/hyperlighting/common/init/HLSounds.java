package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

/**
 * @author HypherionSA
 * @date 01/07/2022
 */
public class HLSounds {

    public static final RegistrationProvider<SoundEvent> SOUNDS = RegistrationProvider.get(BuiltInRegistries.SOUND_EVENT, Constants.MOD_ID);

    public static RegistryObject<SoundEvent> TORCH_IGNITE = createSound("block.torch_ignite");

    public static RegistryObject<SoundEvent> createSound(String location) {
        final var soundLocation = Constants.rl(location);
        return SOUNDS.register(location, () -> SoundEvent.createVariableRangeEvent(soundLocation));
    }

    public static void loadAll() {}
}
