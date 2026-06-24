package com.awesomesink.menu;

import com.awesomesink.block.entity.AwesomeSinkBlockEntity;
import com.awesomesink.registry.ModBlocks;
import com.awesomesink.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class AwesomeSinkMenu extends MachineMenu {

    public AwesomeSinkMenu(int id, Inventory inv, RegistryFriendlyByteBuf buf) {
        this(id, inv, resolve(inv, buf.readBlockPos()));
    }

    private AwesomeSinkMenu(int id, Inventory inv, AwesomeSinkBlockEntity be) {
        this(id, inv, be, be.data());
    }

    public AwesomeSinkMenu(int id, Inventory inv, AwesomeSinkBlockEntity be, ContainerData data) {
        super(ModMenus.AWESOME_SINK.get(), id, ModBlocks.AWESOME_SINK.get(), data,
                ContainerLevelAccess.create(be.getLevel(), be.getBlockPos()), 2, be.getBlockPos());
        IItemHandler handler = be.inventory();
        addSlot(new SlotItemHandler(handler, AwesomeSinkBlockEntity.SLOT_INPUT, 56, 35));
        addSlot(new SlotItemHandler(handler, AwesomeSinkBlockEntity.SLOT_OUTPUT, 116, 35));
        addPlayerInventory(inv);
    }

    public int points() {
        return data.get(AwesomeSinkBlockEntity.DATA_POINTS);
    }

    public int nextCost() {
        return data.get(AwesomeSinkBlockEntity.DATA_NEXT_COST);
    }

    private static AwesomeSinkBlockEntity resolve(Inventory inv, BlockPos pos) {
        if (inv.player.level().getBlockEntity(pos) instanceof AwesomeSinkBlockEntity be) {
            return be;
        }
        throw new IllegalStateException("Missing AWESOME Sink at " + pos);
    }
}
