package dev.aaronhowser.mods.irregular_implements.client.render

import dev.aaronhowser.mods.irregular_implements.item.LavaCharmItem
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.ClientUtil
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Mth

object RenderLavaCharmOverlay {

    val LAYER_NAME = OtherUtil.modResource("lava_protection")
    private val SPRITE_LOCATION = OtherUtil.modResource("lava_protection.png")

    private const val FILE_WIDTH = 16
    private const val IMAGE_WIDTH = 11

    fun tryRender(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
        val player = ClientUtil.localPlayer ?: return
        val lavaProtector = LavaCharmItem.getFirstLavaProtector(player) ?: return

        val charge = lavaProtector.get(ModDataComponents.CHARGE) ?: return

        val count = Mth.floor(charge / 2f / 10f)

        var left = 0

        for (i in 0 until count) {

            guiGraphics.blitSprite(
                SPRITE_LOCATION,
                FILE_WIDTH,
                FILE_WIDTH,
                0,
                0,
                left,
                0,
                IMAGE_WIDTH,
                IMAGE_WIDTH
            )

            left += IMAGE_WIDTH
        }
    }

}