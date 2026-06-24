package com.awesomesink.client;

import com.awesomesink.menu.AwesomeSinkMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AwesomeSinkScreen extends MachineScreen<AwesomeSinkMenu> {
    public AwesomeSinkScreen(AwesomeSinkMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(graphics, partialTick, mouseX, mouseY);
        int ax = leftPos + 78, ay = topPos + 40;
        graphics.blit(BG, ax, ay, 176, 0, 32, 16);
        int cost = menu.nextCost();
        int filled = cost > 0 ? Math.min(32, (int) ((long) 32 * menu.points() / cost)) : 0;
        if (filled > 0) {
            graphics.blit(BG, ax, ay, 176, 18, filled, 16);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawString(font, Component.translatable("screen.awesomesink.points", menu.points()),
                8, 16, 0x404040, false);
        graphics.drawString(font, Component.translatable("screen.awesomesink.rate", menu.rate()),
                8, 26, 0x404040, false);
        graphics.drawString(font, Component.translatable("screen.awesomesink.next_coupon", menu.nextCost()),
                62, 60, 0x404040, false);
    }
}
