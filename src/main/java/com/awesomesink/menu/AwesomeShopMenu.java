package com.awesomesink.menu;

import com.awesomesink.block.entity.AwesomeShopBlockEntity;
import com.awesomesink.registry.ModBlocks;
import com.awesomesink.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AwesomeShopMenu extends MachineMenu {

    public AwesomeShopMenu(int id, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(id, inv, resolve(inv, buf.readBlockPos()));
    }

    private AwesomeShopMenu(int id, Inventory inv, AwesomeShopBlockEntity be) {
        this(id, inv, be, be.data());
    }

    public AwesomeShopMenu(int id, Inventory inv, AwesomeShopBlockEntity be, ContainerData data) {
        super(ModMenus.AWESOME_SHOP.get(), id, ModBlocks.AWESOME_SHOP.get(), data,
                ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), 2, be.getBlockPos());
        IItemHandler handler = be.inventory();
        addSlot(new SlotItemHandler(handler, AwesomeShopBlockEntity.SLOT_COUPONS, 56, 40));
        addSlot(new SlotItemHandler(handler, AwesomeShopBlockEntity.SLOT_OUTPUT, 116, 40));
        addPlayerInventory(inv);
    }

    public int price() {
        return data.get(AwesomeShopBlockEntity.DATA_PRICE);
    }

    public int selection() {
        return data.get(AwesomeShopBlockEntity.DATA_SELECTION);
    }

    private static AwesomeShopBlockEntity resolve(Inventory inv, BlockPos pos) {
        if (inv.player.level().getBlockEntity(pos) instanceof AwesomeShopBlockEntity be) {
            return be;
        }
        throw new IllegalStateException("Missing AWESOME Shop at " + pos);
    }
}
