package com.awesomesink.block.entity;

import java.util.function.BiPredicate;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Item handler split into input slots [0, inputs) and output slots [inputs, size).
 * Automation may only insert into inputs and only extract from outputs.
 *
 * <p>If an {@link InputConsumer} is installed, inserts into input slots are consumed immediately
 * (nothing is stored), giving unbounded intake throughput; otherwise inputs buffer normally.
 */
public class MachineInventory extends ItemStackHandler {
    /** Handles an inserted input stack; returns true if it was fully consumed. */
    public interface InputConsumer {
        boolean consume(ItemStack stack);
    }

    private final int inputs;
    private final BiPredicate<Integer, ItemStack> validInput;
    private final Runnable onChange;
    @Nullable
    private InputConsumer inputConsumer;

    public MachineInventory(int inputs, int outputs, BiPredicate<Integer, ItemStack> validInput, Runnable onChange) {
        super(inputs + outputs);
        this.inputs = inputs;
        this.validInput = validInput;
        this.onChange = onChange;
    }

    public void setInputConsumer(InputConsumer consumer) {
        this.inputConsumer = consumer;
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
        if (!isInput(slot) || stack.isEmpty() || !validInput.test(slot, stack)) {
            return stack;
        }
        if (inputConsumer != null) {
            return simulate || inputConsumer.consume(stack) ? ItemStack.EMPTY : super.insertItem(slot, stack, simulate);
        }
        return super.insertItem(slot, stack, simulate);
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
