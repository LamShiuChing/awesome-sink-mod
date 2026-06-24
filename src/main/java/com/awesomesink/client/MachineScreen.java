package com.awesomesink.client;

import com.awesomesink.block.RelativeSide;
import com.awesomesink.block.SideMode;
import com.awesomesink.block.entity.AbstractMachineBlockEntity;
import com.awesomesink.menu.MachineMenu;
import com.awesomesink.network.SetSideModePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Base screen: vanilla container background plus a Mekanism-style side-configuration overlay
 * (own implementation) toggled by an "I/O" button — a cube-net of six faces, click to cycle mode.
 */
public abstract class MachineScreen<T extends MachineMenu> extends AbstractContainerScreen<T> {
    private static final ResourceLocation BG =
            ResourceLocation.withDefaultNamespace("textures/gui/container/dispenser.png");
    private static final int BOX = 20;
    private static final int GAP = 22;

    private boolean showConfig;

    protected MachineScreen(T menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        addRenderableWidget(Button.builder(Component.literal("I/O"), b -> showConfig = !showConfig)
                .bounds(leftPos + imageWidth - 26, topPos + 4, 22, 16).build());
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BG, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        if (showConfig) {
            renderSideConfig(graphics);
        }
        renderTooltip(graphics, mouseX, mouseY);
    }

    private void renderSideConfig(GuiGraphics graphics) {
        for (RelativeSide side : RelativeSide.values()) {
            int[] r = boxRect(side);
            graphics.fill(r[0], r[1], r[0] + BOX, r[1] + BOX, sideMode(side).color);
            graphics.renderOutline(r[0], r[1], BOX, BOX, 0xFF000000);
            graphics.drawCenteredString(font, label(side), r[0] + BOX / 2, r[1] + 6, 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (showConfig && button == 0) {
            for (RelativeSide side : RelativeSide.values()) {
                int[] r = boxRect(side);
                if (mouseX >= r[0] && mouseX < r[0] + BOX && mouseY >= r[1] && mouseY < r[1] + BOX) {
                    PacketDistributor.sendToServer(new SetSideModePayload(menu.pos(), side));
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private int[] boxRect(RelativeSide side) {
        int originX = leftPos + imageWidth + 6;
        int originY = topPos + 16;
        int[] cell = switch (side) {
            case TOP -> new int[]{1, 0};
            case LEFT -> new int[]{0, 1};
            case FRONT -> new int[]{1, 1};
            case RIGHT -> new int[]{2, 1};
            case BACK -> new int[]{3, 1};
            case BOTTOM -> new int[]{1, 2};
        };
        return new int[]{originX + cell[0] * GAP, originY + cell[1] * GAP};
    }

    private static String label(RelativeSide side) {
        return switch (side) {
            case FRONT -> "F";
            case BACK -> "B";
            case LEFT -> "L";
            case RIGHT -> "R";
            case TOP -> "T";
            case BOTTOM -> "D";
        };
    }

    private SideMode sideMode(RelativeSide side) {
        return Minecraft.getInstance().level.getBlockEntity(menu.pos()) instanceof AbstractMachineBlockEntity be
                ? be.sideMode(side) : SideMode.DISABLED;
    }
}
