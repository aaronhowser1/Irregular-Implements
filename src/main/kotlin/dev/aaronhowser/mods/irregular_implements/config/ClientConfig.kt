package dev.aaronhowser.mods.irregular_implements.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.apache.commons.lang3.tuple.Pair

class ClientConfig(
    private val builder: ModConfigSpec.Builder
) {

    companion object {
        private val configPair: Pair<ClientConfig, ModConfigSpec> = ModConfigSpec.Builder().configure(::ClientConfig)

        val CONFIG: ClientConfig = configPair.left
        val CONFIG_SPEC: ModConfigSpec = configPair.right

        lateinit var COLLAPSE_INVERTS_MOUSE: ModConfigSpec.BooleanValue
    }

    init {
        clientConfigs()
        builder.build()
    }

    private fun clientConfigs() {

        COLLAPSE_INVERTS_MOUSE = builder
            .comment("Should the Collapse Imbue invert the player's mouse sensitivity?")
            .define("collapseInvertsMouse", true)

    }

}