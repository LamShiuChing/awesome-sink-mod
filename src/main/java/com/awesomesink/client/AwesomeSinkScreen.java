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
        couponProgressBar(graphics);
    }

    /** Bar showing the unspent points pool filling toward the next coupon. */
    private void couponProgressBar(GuiGraphics graphics) {
        int x = 8, y = 52, w = 160, h = 6;
        int cost = menu.nextCost();
        int filled = cost > 0 ? Math.min(w, (int) ((long) w * menu.points() / cost)) : 0;
        graphics.fill(x, y, x + w, y + h, 0xFF373737);
        graphics.fill(x, y, x + filled, y + h, 0xFFE9C46A);
    }
}
