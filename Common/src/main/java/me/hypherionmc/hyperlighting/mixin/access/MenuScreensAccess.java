package me.hypherionmc.hyperlighting.mixin.access;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
@Mixin(MenuScreens.class)
public interface MenuScreensAccess {

    @Invoker("register")
    static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void crater_register(MenuType<? extends M> p_216911_0_, MenuScreens.ScreenConstructor<M, U> p_216911_1_) {
        throw new Error("Mixin did not apply!");
    }

}
