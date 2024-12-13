package dev.aaronhowser.mods.irregular_implements.client.render

import dev.aaronhowser.mods.irregular_implements.item.LavaCharmItem
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.ClientUtil
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Mth

object RenderLavaProtectionOverlay {

    val LAYER_NAME = OtherUtil.modResource("lava_protection")
    private val SPRITE_LOCATION = OtherUtil.modResource("lava_protection")

    private const val FILE_WIDTH = 16
    private const val IMAGE_WIDTH = 11

    fun tryRender(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        val player = ClientUtil.localPlayer ?: return
        val lavaProtector = LavaCharmItem.getFirstLavaProtector(player) ?: return

        val charge = lavaProtector.get(ModDataComponents.CHARGE) ?: return
        val maxCharge = LavaCharmItem.MAX_CHARGE

        val percentCharged = charge.toFloat() / maxCharge.toFloat()
        val amountFullSprites = Mth.floor(percentCharged * 10f)

        //TODO: Add another partially filled sprite

        var leftPos = 0

        for (i in 0 until amountFullSprites) {
            guiGraphics.blitSprite(
                SPRITE_LOCATION,
                FILE_WIDTH,
                FILE_WIDTH,
                0,
                0,
                leftPos,
                0,
                IMAGE_WIDTH,
                IMAGE_WIDTH
            )
            leftPos += IMAGE_WIDTH
        }
    }

}