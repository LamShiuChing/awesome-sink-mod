package com.awesomesink.block.entity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Exposes a machine inventory to one face for automation: an INPUT face may only insert into input
 * slots, an OUTPUT face may only extract from output slots. The GUI uses the raw inventory directly,
 * so the player is unaffected by these restrictions.
 */
public record DirectionalItemHandler(MachineInventory delegate, boolean allowInsert, boolean allowExtract)
        implements IItemHandler {

    @Override
    public int getSlots() {
        return delegate.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return delegate.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return allowInsert && delegate.isInput(slot) ? delegate.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return allowExtract && !delegate.isInput(slot) ? delegate.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return delegate.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return allowInsert && delegate.isInput(slot) && delegate.isItemValid(slot, stack);
    }
}
