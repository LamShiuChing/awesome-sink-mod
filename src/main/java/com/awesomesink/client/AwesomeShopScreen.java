package com.awesomesink.client;

import java.util.List;

import com.awesomesink.data.ShopCatalog;
import com.awesomesink.menu.AwesomeShopMenu;
import com.awesomesink.network.BuyShopItemPayload;
import com.awesomesink.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class AwesomeShopScreen extends MachineScreen<AwesomeShopMenu> {
    private static final int COLS = 8;
    private static final int CELL = 18;
    private static final int ROWS_VISIBLE = 4;

    private boolean showShop;
    private int scroll;

    public AwesomeShopScreen(AwesomeShopMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.literal("Shop"), b -> showShop = !showShop)
                .bounds(leftPos + imageWidth - 26, topPos + 22, 22, 16).build());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);
        graphics.drawString(font, Component.translatable("screen.awesomesink.price", menu.price()),
                8, 18, 0x404040, false);
        graphics.drawString(font, Component.translatable("screen.awesomesink.shop_hint"),
                8, 28, 0x707070, false);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if (showShop) {
            renderBrowser(graphics, mouseX, mouseY);
        }
    }

    private void renderBrowser(GuiGraphics graphics, int mouseX, int mouseY) {
        List<ShopCatalog.Entry> entries = ClientShopCatalog.entries();
        int totalRows = Math.max(1, (entries.size() + COLS - 1) / COLS);
        int rows = Math.min(totalRows, ROWS_VISIBLE);
        int x0 = leftPos + 7;
        int y0 = topPos + 16;
        graphics.fill(x0, y0, x0 + COLS * CELL + 2, y0 + rows * CELL + 12, 0xF01A1A1A);
        graphics.drawString(font, Component.translatable("screen.awesomesink.balance", couponBalance()),
                x0 + 3, y0 + 2, 0xFFE9C46A, false);

        for (int i = 0; i < entries.size(); i++) {
            if (!onScreen(i)) {
                continue;
            }
            int[] c = cell(i);
            ItemStack stack = new ItemStack(entries.get(i).item(), entries.get(i).count());
            graphics.renderItem(stack, c[0], c[1]);
            graphics.renderItemDecorations(font, stack, c[0], c[1]);
        }
        int hovered = cellAt(mouseX, mouseY, entries.size());
        if (hovered >= 0) {
            ShopCatalog.Entry e = entries.get(hovered);
            graphics.renderComponentTooltip(font, List.of(
                    new ItemStack(e.item()).getHoverName(),
                    Component.translatable("screen.awesomesink.price", e.price()),
                    Component.literal("x" + e.count())), mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (showShop && button == 0) {
            int i = cellAt((int) mouseX, (int) mouseY, ClientShopCatalog.entries().size());
            if (i >= 0) {
                PacketDistributor.sendToServer(new BuyShopItemPayload(menu.pos(), i));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dx, double dy) {
        if (showShop) {
            int max = Math.max(0, (ClientShopCatalog.entries().size() + COLS - 1) / COLS - ROWS_VISIBLE);
            scroll = Math.max(0, Math.min(max, scroll - (int) Math.signum(dy)));
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, dx, dy);
    }

    private boolean onScreen(int index) {
        int row = index / COLS - scroll;
        return row >= 0 && row < ROWS_VISIBLE;
    }

    private int[] cell(int index) {
        return new int[]{leftPos + 8 + (index % COLS) * CELL, topPos + 27 + (index / COLS - scroll) * CELL};
    }

    private int cellAt(int mouseX, int mouseY, int size) {
        for (int i = 0; i < size; i++) {
            if (!onScreen(i)) {
                continue;
            }
            int[] c = cell(i);
            if (mouseX >= c[0] && mouseX < c[0] + 16 && mouseY >= c[1] && mouseY < c[1] + 16) {
                return i;
            }
        }
        return -1;
    }

    private int couponBalance() {
        return minecraft.player.getInventory().items.stream()
                .filter(s -> s.is(ModItems.COUPON.get())).mapToInt(ItemStack::getCount).sum();
    }
}
