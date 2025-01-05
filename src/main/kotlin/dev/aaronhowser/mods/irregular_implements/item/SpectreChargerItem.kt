package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.savedata.SpectreCoilSavedData.Companion.spectreCoilSavedData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import java.awt.Color
import java.util.function.Supplier

class SpectreChargerItem private constructor(
    private val type: Type
) : Item(Properties().stacksTo(1)) {

    companion object {
        val BASIC = SpectreChargerItem(Type.BASIC)
        val REDSTONE = SpectreChargerItem(Type.REDSTONE)
        val ENDER = SpectreChargerItem(Type.ENDER)
        val GENESIS = SpectreChargerItem(Type.GENESIS)

        const val CHARGE_DELAY = 5
    }

    override fun inventoryTick(stack: ItemStack, level: Level, player: Entity, slotId: Int, isSelected: Boolean) {
        if (level !is ServerLevel || player !is Player || level.gameTime % CHARGE_DELAY != 0L) return

        val amountToCharge = this.type.amountGetter.get() * CHARGE_DELAY

        val coil = level.spectreCoilSavedData.getCoil(player.uuid)

        for (inventoryStack in player.inventory.items) {
            val energyCapability = inventoryStack.getCapability(Capabilities.EnergyStorage.ITEM)
            if (energyCapability == null || !energyCapability.canReceive()) continue

            if (this.type == Type.GENESIS) {
                energyCapability.receiveEnergy(amountToCharge, false)
                continue
            }

            val available = coil.extractEnergy(amountToCharge, true)
            if (available <= 0) return

            val sent = energyCapability.receiveEnergy(available, false)
            coil.extractEnergy(sent, false)
        }
    }

    enum class Type(val color: Int, val amountGetter: Supplier<Int>) {
        BASIC(
            color = Color.CYAN.rgb,
            amountGetter = { 1024 }
        ),
        REDSTONE(
            color = Color.RED.rgb,
            amountGetter = { 4096 }
        ),
        ENDER(
            color = Color(200, 0, 210).rgb,
            amountGetter = { 20480 }
        ),
        GENESIS(
            color = Color.ORANGE.rgb,
            amountGetter = { Int.MAX_VALUE }
        )
    }

}