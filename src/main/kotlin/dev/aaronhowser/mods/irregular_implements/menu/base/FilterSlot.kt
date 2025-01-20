package dev.aaronhowser.mods.irregular_implements.menu.base

import dev.aaronhowser.mods.irregular_implements.item.component.ItemFilterDataComponent
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.FilterEntry
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.NonInteractiveResultSlot
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

open class FilterSlot(
    private val filterStack: Supplier<ItemStack>,
    private val lookupProvider: HolderLookup.Provider,
    slotIndex: Int,
    x: Int,
    y: Int
) : NonInteractiveResultSlot(SimpleContainer(9), slotIndex, x, y) {

    private val stackComponent: ItemFilterDataComponent?
        get() = this.filterStack.get().get(ModDataComponents.ITEM_FILTER_ENTRIES)

    private val stackFilter: NonNullList<FilterEntry>?
        get() = stackComponent?.entries

    private val entryInThisSlot: FilterEntry?
        get() = stackFilter?.getOrNull(this.index)

    override fun getItem(): ItemStack {
        return entryInThisSlot?.getDisplayStack(lookupProvider) ?: ItemStack.EMPTY
    }

    // Treating this as basically a button that removes this slot's entry from the filter component
    override fun mayPickup(player: Player): Boolean {
        val stackFilter = this.stackFilter ?: return false

        val newFilter = stackFilter.toMutableList()
        newFilter[this.index] = FilterEntry.Empty

        this.filterStack.get().set(
            ModDataComponents.ITEM_FILTER_ENTRIES,
            ItemFilterDataComponent(
                newFilter,
                this.stackComponent?.isBlacklist ?: false
            )
        )

        setChanged()

        return false
    }

    override fun safeInsert(stack: ItemStack): ItemStack {
        if (stack.isEmpty) return stack
        if (this.entryInThisSlot !is FilterEntry.Empty && this.entryInThisSlot != null) return stack

        val oldFilter = stackFilter ?: return stack
        val newFilter = ItemFilterDataComponent.sanitizeEntries(oldFilter).toMutableList()

        newFilter[this.index] = FilterEntry.Item(
            stack.copyWithCount(1),
            requireSameComponents = false
        )

        this.filterStack.get().set(
            ModDataComponents.ITEM_FILTER_ENTRIES,
            ItemFilterDataComponent(
                newFilter,
                this.stackComponent?.isBlacklist ?: false
            )
        )

        setChanged()

        return stack
    }

    override fun setChanged() {
        //TODO: Update the buttons in the screen
    }

    override fun remove(amount: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun set(stack: ItemStack) {
        // Do nothing
    }

    override fun isHighlightable(): Boolean {
        return true
    }

    // Not true, but if it's fake then mayPlace never runs
    override fun isFake(): Boolean {
        return false
    }

}