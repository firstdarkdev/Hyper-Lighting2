package me.hypherionmc.hyperlighting.mixin.access;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
@Mixin(MenuType.class)
public interface MenuTypeAccess {

    @Invoker("<init>")
    static <T extends AbstractContainerMenu> MenuType<T> crater_create(MenuType.MenuSupplier<T> menuSupplier) {
        throw new Error("Mixin did not apply!");
    }

}
