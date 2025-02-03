package dev.aaronhowser.mods.irregular_implements.block.block_entity

import dev.aaronhowser.mods.irregular_implements.registry.ModBlockEntities
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil.getUuidOrNull
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.PlayerArmorInvWrapper
import net.neoforged.neoforge.items.wrapper.PlayerOffhandInvWrapper
import net.neoforged.neoforge.items.wrapper.RangedWrapper
import java.util.*

//TODO: What if BER that makes the owner's head float above the block and look at you
class PlayerInterfaceBlockEntity(
    pPos: BlockPos,
    pBlockState: BlockState
) : BlockEntity(ModBlockEntities.PLAYER_INTERFACE.get(), pPos, pBlockState) {

    companion object {

        private var PLAYER_PREDICATE: (Player, BlockEntity) -> Boolean = { _, _ -> true }

        // Mostly intended to be called from KubeJS
        @JvmStatic
        fun setPlayerPredicate(predicate: (Player, BlockEntity) -> Boolean) {
            PLAYER_PREDICATE = predicate
        }

        const val OWNER_UUID_NBT = "OwnerUuid"

    }

    private enum class InventorySection {
        ARMOR,
        HOTBAR,
        OFFHAND,
        MAIN;

        companion object {
            fun fromDirection(direction: Direction?): InventorySection {
                return when (direction) {
                    Direction.UP -> ARMOR
                    Direction.DOWN -> HOTBAR
                    Direction.NORTH -> OFFHAND
                    else -> MAIN
                }
            }
        }
    }

    var ownerUuid: UUID = UUID.randomUUID()

    private fun getPlayer(): Player? {
        val level = this.level as? ServerLevel ?: return null
        return level.server.playerList.getPlayer(ownerUuid)
    }

    fun getItemHandler(direction: Direction?): IItemHandler? {
        val owner = getPlayer() ?: return null
        if (!PLAYER_PREDICATE.invoke(owner, this)) return null

        val section = InventorySection.fromDirection(direction)

        return when (section) {
            InventorySection.ARMOR -> getArmorHandler(owner)
            InventorySection.HOTBAR -> getHotbarHandler(owner)
            InventorySection.OFFHAND -> getOffhandHandler(owner)
            InventorySection.MAIN -> getMainHandler(owner)
        }
    }

    private fun getHotbarHandler(owner: Player): IItemHandler {
        return LimitedInventoryWrapper(owner.inventory, 0, 9)
    }

    private fun getMainHandler(owner: Player): IItemHandler {
        return LimitedInventoryWrapper(owner.inventory, 9, 9 * 3)
    }

    private fun getOffhandHandler(owner: Player): IItemHandler {
        return PlayerOffhandInvWrapper(owner.inventory)
    }

    private fun getArmorHandler(owner: Player): IItemHandler {
        return PlayerArmorInvWrapper(owner.inventory)
    }

    private class LimitedInventoryWrapper(
        private val inventory: Inventory,
        minSlot: Int,
        maxSlot: Int
    ) : RangedWrapper(InvWrapper(inventory), minSlot, maxSlot) {
        override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
            val rest = super.insertItem(slot, stack, simulate)
            if (rest.count != stack.count) {
                val inSlot = getStackInSlot(slot)
                if (!inSlot.isEmpty) {
                    val player = inventory.player

                    if (player.level().isClientSide) {
                        inSlot.popTime = 5
                    } else if (player is ServerPlayer) {
                        player.containerMenu.broadcastChanges()
                    }
                }
            }
            return rest
        }
    }

    override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.loadAdditional(tag, registries)

        val uuid = tag.getUuidOrNull(OWNER_UUID_NBT)
        if (uuid != null) this.ownerUuid = uuid
    }

    override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
        super.saveAdditional(tag, registries)

        tag.putUUID(OWNER_UUID_NBT, ownerUuid)
    }

}