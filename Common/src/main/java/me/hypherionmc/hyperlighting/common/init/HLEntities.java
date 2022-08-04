package me.hypherionmc.hyperlighting.common.init;

import me.hypherionmc.craterlib.systems.reg.RegistrationProvider;
import me.hypherionmc.craterlib.systems.reg.RegistryObject;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.entities.NeonFlyEntity;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
public class HLEntities {

    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registry.ENTITY_TYPE, Constants.MOD_ID);

    public static final RegistryObject<EntityType<NeonFlyEntity>> FIREFLY = ENTITIES.register("firefly", () -> EntityType.Builder.of(NeonFlyEntity::new, MobCategory.AMBIENT)
            .sized(1f, 1f)
            .clientTrackingRange(8)
            .build("firefly"));

    public static void loadAll() {}
}
