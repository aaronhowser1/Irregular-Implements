package dev.aaronhowser.mods.irregular_implements.util

import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

}