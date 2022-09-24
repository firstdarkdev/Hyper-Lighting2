package me.hypherionmc.hyperlighting.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import me.hypherionmc.hyperlighting.Constants;
import me.hypherionmc.hyperlighting.common.blockentities.BatteryNeonBlockEntity;
import me.hypherionmc.hyperlighting.common.containers.BatteryNeonContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HypherionSA
 * @date 24/09/2022
 */
public class BatteryNeonScreen extends AbstractContainerScreen<BatteryNeonContainer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/gui/neon_light.png");
    private final Inventory player;
    private final BatteryNeonBlockEntity te;

    public BatteryNeonScreen(BatteryNeonContainer container, Inventory inventory, Component title) {
        super(container, inventory, title);
        this.te = container.getBlockEntity();
        this.player = inventory;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float v, int i, int i1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        blit(poseStack, this.leftPos + 47, this.topPos + 20, 0, 198, (int) (((float) this.te.getEnergyStorage().getPowerLevel() / this.te.getEnergyStorage().getPowerCapacity()) * 110), 16);

        if (this.te.isCharging()) {
            blit(poseStack, this.leftPos + 26, this.topPos + 38, 185, 17, 4, 4);
        }
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
        super.renderLabels(matrixStack, mouseX, mouseY);

        this.drawPowerToolTip(matrixStack, mouseX, mouseY, leftPos + this.menu.slots.get(0).x, topPos + this.menu.slots.get(0).y, 16, 16, ChatFormatting.YELLOW + "Power Slot", "Place a wireless battery", "module in this slot", "linked to a solar panel", "to charge the light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, leftPos + this.menu.slots.get(1).x, topPos + this.menu.slots.get(1).y, 16, 16, ChatFormatting.YELLOW + "Dye Slot", "Place dye here to", "change the color of the", "light");
        this.drawPowerToolTip(matrixStack, mouseX, mouseY, this.leftPos + 47, this.topPos + 20, 110, 16, ChatFormatting.YELLOW + "Power Level", ChatFormatting.BLUE + "" + (int) (((float) this.te.getEnergyStorage().getPowerLevel() / this.te.getEnergyStorage().getPowerCapacity()) * 100) + "%", (te.isCharging() ? ChatFormatting.GREEN + "Charging" : ChatFormatting.RED + "Not Charging"));
    }

    private void drawPowerToolTip(PoseStack stack, int mouseX, int mouseY, int startX, int startY, int sizeX, int sizeY, String title, String... description) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        if (mouseX > startX && mouseX < startX + sizeX) {
            if (mouseY > startY && mouseY < startY + sizeY) {
                List<Component> list = new ArrayList<>();
                list.add(Component.translatable(title));
                for (String desc : description) {
                    list.add(Component.translatable(desc));
                }
                renderComponentTooltip(stack, list, mouseX - k, mouseY - l);
            }
        }
    }
}
