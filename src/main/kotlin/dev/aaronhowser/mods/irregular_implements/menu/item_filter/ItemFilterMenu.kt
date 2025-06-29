package dev.aaronhowser.mods.irregular_implements.menu.item_filter

import dev.aaronhowser.mods.irregular_implements.item.component.ItemFilterDataComponent
import dev.aaronhowser.mods.irregular_implements.menu.MenuWithButtons
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import dev.aaronhowser.mods.irregular_implements.registry.ModMenuTypes
import dev.aaronhowser.mods.irregular_implements.util.FilterEntry
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil.isTrue
import net.minecraft.core.NonNullList
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ItemFilterMenu(
	containerId: Int,
	private val playerInventory: Inventory
) : AbstractContainerMenu(ModMenuTypes.ITEM_FILTER.get(), containerId), MenuWithButtons {

	private val holderLookup = this.playerInventory.player.level().registryAccess()

	// Uses a getter because when it mutates it only does so on server, and doesn't mutate the one on the client's copy of the menu
	private val filterStack: ItemStack
		get() =
			if (playerInventory.player.mainHandItem.`is`(ModItems.ITEM_FILTER.get())) {
				playerInventory.player.mainHandItem
			} else {
				playerInventory.player.offhandItem
			}

	private var usingMainHand = playerInventory.player.getItemInHand(InteractionHand.MAIN_HAND) === filterStack

	private val filterComponent: ItemFilterDataComponent?
		get() = filterStack.get(ModDataComponents.ITEM_FILTER_ENTRIES)

	val filter: NonNullList<FilterEntry>?
		get() = filterComponent?.entries

	var isBlacklist: Boolean
		get() = filterComponent?.isBlacklist.isTrue
		private set(value) {
			if (value == this.isBlacklist) return

			val filterComponent = this.filterComponent ?: return

			filterStack.set(
				ModDataComponents.ITEM_FILTER_ENTRIES,
				filterComponent.copy(isBlacklist = value)
			)
		}

	init {
		for (index in 0 until 9) {
			val x = 8 + index * 18
			val y = 26

			val slot = ItemFilterSlot(::filterStack, this.holderLookup, x, y)

			this.addSlot(slot)
		}

		// Add the 27 slots of the player inventory
		for (row in 0..2) {
			for (column in 0..8) {
				val slotIndex = column + row * 9 + 9
				val x = 8 + column * 18
				val y = 59 + row * 18

				this.addSlot(Slot(playerInventory, slotIndex, x, y))
			}
		}

		// Add the 9 slots of the player hotbar
		for (hotbarIndex in 0..8) {
			val x = 8 + hotbarIndex * 18
			val y = 117

			this.addSlot(Slot(playerInventory, hotbarIndex, x, y))
		}
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		val hand = if (usingMainHand) InteractionHand.MAIN_HAND else InteractionHand.OFF_HAND
		return player.getItemInHand(hand).`is`(ModItems.ITEM_FILTER)
	}

	override fun handleButtonPressed(buttonId: Int) {
		when (buttonId) {
			TOGGLE_BLACKLIST_BUTTON_ID -> this.isBlacklist = !this.isBlacklist

			in 1..9 -> pressLeftButton(buttonId - 1)

			in 10..18 -> pressRightButton(buttonId - 10)
		}
	}

	// Toggles between Item Filter and Tag Filter
	private fun pressLeftButton(slotIndex: Int) {
		val filter = this.filter ?: return
		val entry = filter.getOrNull(slotIndex) ?: return

		val newEntry = when (entry) {

			// If it's an ItemTag, return a SpecificItem
			is FilterEntry.Tag -> entry.getAsSpecificItemEntry()

			// If it's a SpecificItem, return an ItemTag
			is FilterEntry.Item -> {
				val tag = entry.stack.tags.toList().firstOrNull() ?: return
				FilterEntry.Tag(
					tag,
					entry.stack.copy()
				)
			}

			else -> return
		}

		val newFilter = filter.toMutableList()
		newFilter[slotIndex] = newEntry

		filterStack.set(
			ModDataComponents.ITEM_FILTER_ENTRIES,
			ItemFilterDataComponent(newFilter, this.filterComponent!!.isBlacklist)
		)
	}

	// If it's an Item Filter, toggles between requiring the same components or not
	// If it's a Tag Filter, cycles which Tag it's filtering
	private fun pressRightButton(slotIndex: Int) {
		val filter = this.filter ?: return
		val entry = filter.getOrNull(slotIndex) ?: return

		if (entry is FilterEntry.Item) {
			toggleNeedsComponents(slotIndex, filter, entry)
		} else if (entry is FilterEntry.Tag) {
			cycleTag(slotIndex, filter, entry)
		}

	}

	private fun toggleNeedsComponents(slotIndex: Int, filter: NonNullList<FilterEntry>, entry: FilterEntry.Item) {
		val newEntry = entry.copy(requireSameComponents = !entry.requireSameComponents)

		val newFilter = filter.toMutableList()
		newFilter[slotIndex] = newEntry

		filterStack.set(
			ModDataComponents.ITEM_FILTER_ENTRIES,
			ItemFilterDataComponent(newFilter, this.filterComponent!!.isBlacklist)
		)
	}

	private fun cycleTag(slotIndex: Int, filter: NonNullList<FilterEntry>, entry: FilterEntry.Tag) {

		val nextTag = entry.getNextTag()
		val newEntry = entry.copy(tagKey = nextTag)

		val newFilter = filter.toMutableList()
		newFilter[slotIndex] = newEntry

		filterStack.set(
			ModDataComponents.ITEM_FILTER_ENTRIES,
			ItemFilterDataComponent(newFilter, this.filterComponent!!.isBlacklist)
		)
	}

	companion object {
		// Toggles between Item Filter and Tag Filter
		fun getLeftButtonId(slotIndex: Int): Int {
			return slotIndex + 1
		}

		// If it's an Item Filter, toggles between requiring the same components or not
		// If it's a Tag Filter, cycles which Tag it's filtering
		fun getRightButtonId(slotIndex: Int): Int {
			return slotIndex + 10
		}

		const val TOGGLE_BLACKLIST_BUTTON_ID = 0
	}

}