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
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawString(font, Component.translatable("screen.awesomesink.points", menu.points()),
                8, 18, 0x404040, false);
        graphics.drawString(font, Component.translatable("screen.awesomesink.next_coupon", menu.nextCost()),
                8, 28, 0x404040, false);
    }
}
