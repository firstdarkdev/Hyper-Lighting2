package me.hypherionmc.hyperlighting.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @author HypherionSA
 * @date 04/08/2022
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        System.out.println("Running Datagen");
        DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeServer(), new RecipeGenerator(generator));
    }

}
