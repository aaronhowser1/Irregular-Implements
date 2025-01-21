package dev.aaronhowser.mods.irregular_implements.menu

import dev.aaronhowser.mods.irregular_implements.block.block_entity.AdvancedItemCollectorBlockEntity
import dev.aaronhowser.mods.irregular_implements.menu.base.MenuWithButtons
import dev.aaronhowser.mods.irregular_implements.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class AdvancedItemCollectorMenu(
    containerId: Int,
    playerInventory: Inventory,
    private val container: Container,
    private val containerData: ContainerData
) : AbstractContainerMenu(ModMenuTypes.ADVANCED_ITEM_COLLECTOR.get(), containerId), MenuWithButtons {

    constructor(containerId: Int, playerInventory: Inventory) :
            this(
                containerId,
                playerInventory,
                SimpleContainer(1),
                SimpleContainerData(1)
            )

    override fun quickMoveStack(player: Player, index: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun stillValid(player: Player): Boolean {
        return this.container.stillValid(player)
    }

    var xRadius: Int
        get() = this.containerData.get(AdvancedItemCollectorBlockEntity.X_RADIUS_INDEX)
        private set(value) = this.containerData.set(AdvancedItemCollectorBlockEntity.X_RADIUS_INDEX, value)

    var yRadius: Int
        get() = this.containerData.get(AdvancedItemCollectorBlockEntity.Y_RADIUS_INDEX)
        private set(value) = this.containerData.set(AdvancedItemCollectorBlockEntity.Y_RADIUS_INDEX, value)

    var zRadius: Int
        get() = this.containerData.get(AdvancedItemCollectorBlockEntity.Z_RADIUS_INDEX)
        private set(value) = this.containerData.set(AdvancedItemCollectorBlockEntity.Z_RADIUS_INDEX, value)

    override fun handleButtonPressed(buttonId: Int) {
        when (buttonId) {
            LOWER_X_BUTTON_ID -> xRadius--
            RAISE_X_BUTTON_ID -> xRadius++

            LOWER_Y_BUTTON_ID -> yRadius--
            RAISE_Y_BUTTON_ID -> yRadius++

            LOWER_Z_BUTTON_ID -> zRadius--
            RAISE_Z_BUTTON_ID -> zRadius++
        }
    }

    companion object {
        const val LOWER_X_BUTTON_ID = 0
        const val RAISE_X_BUTTON_ID = 1
        const val LOWER_Y_BUTTON_ID = 2
        const val RAISE_Y_BUTTON_ID = 3
        const val LOWER_Z_BUTTON_ID = 4
        const val RAISE_Z_BUTTON_ID = 5
    }

}