package com.awesomesink.block.entity;

import java.util.function.BiPredicate;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

/**
 * Item handler split into input slots [0, inputs) and output slots [inputs, size).
 * Automation may only insert into inputs and only extract from outputs; the owning
 * block entity moves items freely via {@link #setStackInSlot}.
 */
public class MachineInventory extends ItemStackHandler {
    private final int inputs;
    private final BiPredicate<Integer, ItemStack> validInput;
    private final Runnable onChange;

    public MachineInventory(int inputs, int outputs, BiPredicate<Integer, ItemStack> validInput, Runnable onChange) {
        super(inputs + outputs);
        this.inputs = inputs;
        this.validInput = validInput;
        this.onChange = onChange;
    }

    public boolean isInput(int slot) {
        return slot < inputs;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return isInput(slot) && validInput.test(slot, stack);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return isInput(slot) ? super.insertItem(slot, stack, simulate) : stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return isInput(slot) ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        onChange.run();
    }
}
