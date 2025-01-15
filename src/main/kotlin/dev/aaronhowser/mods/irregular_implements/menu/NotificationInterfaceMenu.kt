package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.menu.base.GhostSlot
import dev.aaronhowser.mods.irregular_implements.menu.base.MenuWithStrings
import dev.aaronhowser.mods.irregular_implements.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class NotificationInterfaceMenu(
    containerId: Int,
    private val container: Container
) : AbstractContainerMenu(ModMenuTypes.NOTIFICATION_INTERFACE.get(), containerId), MenuWithStrings {

    init {
        checkContainerSize(container, 1)

        this.addSlot(
            GhostSlot(
                container,
                0,
                8,
                31
            )
        )
    }

    companion object {
        const val TITLE_STRING_ID = 0
        const val DESCRIPTION_STRING_ID = 1
    }

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun stillValid(player: Player): Boolean {
        return container.stillValid(player)
    }

    override fun receiveString(stringId: Int, string: String) {
    }

    fun setTitle(title: String): Boolean {
        return true
    }

    fun setDescription(description: String): Boolean {
        return true
    }

}