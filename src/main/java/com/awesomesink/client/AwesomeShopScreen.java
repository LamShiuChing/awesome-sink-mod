package com.awesomesink.client;

import com.awesomesink.menu.AwesomeShopMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class AwesomeShopScreen extends MachineScreen<AwesomeShopMenu> {
    public AwesomeShopScreen(AwesomeShopMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawString(font, Component.translatable("screen.awesomesink.price", menu.price()),
                8, 18, 0x404040, false);
        graphics.drawString(font, Component.translatable("screen.awesomesink.shop_hint"),
                8, 28, 0x707070, false);
    }
}
