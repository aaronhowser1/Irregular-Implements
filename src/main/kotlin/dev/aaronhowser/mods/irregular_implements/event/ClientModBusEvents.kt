package dev.aaronhowser.mods.irregular_implements.event

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.item.EmeraldCompassItem
import dev.aaronhowser.mods.irregular_implements.item.GrassSeedItem
import dev.aaronhowser.mods.irregular_implements.registry.ModBlocks
import dev.aaronhowser.mods.irregular_implements.registry.ModEntityTypes
import dev.aaronhowser.mods.irregular_implements.registry.ModItems
import net.minecraft.client.color.item.ItemColor
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.entity.DisplayRenderer.BlockDisplayRenderer
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.GrassColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.ModelEvent
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent

@EventBusSubscriber(
    modid = IrregularImplements.ID,
    bus = EventBusSubscriber.Bus.MOD,
    value = [Dist.CLIENT]
)
object ClientModBusEvents {

    private fun getItemColorFromDye(dyeColor: DyeColor): ItemColor {
        return ItemColor { _, _ -> dyeColor.textureDiffuseColor }
    }

    @SubscribeEvent
    fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
        for (dyeColor in DyeColor.entries) {
            val seedItem = GrassSeedItem.getFromColor(dyeColor).get()
            val runeDustItem = ModItems.getRuneDust(dyeColor).get()
            val coloredGrassBlock = ModBlocks.getColoredGrass(dyeColor).get()

            val colorFunction = getItemColorFromDye(dyeColor)

            event.register(colorFunction, seedItem)
            event.register(colorFunction, runeDustItem)
            event.register(colorFunction, coloredGrassBlock)
        }

        event.register(getItemColorFromDye(DyeColor.LIME), ModItems.GRASS_SEEDS)

        event.register(
            { _, _ -> GrassColor.getDefaultColor() },
            ModBlocks.BIOME_GLASS.get(),
            ModBlocks.BIOME_STONE.get(),
            ModBlocks.BIOME_COBBLESTONE.get(),
            ModBlocks.BIOME_STONE_BRICKS.get(),
            ModBlocks.BIOME_STONE_BRICKS_CHISELED.get(),
            ModBlocks.BIOME_STONE_BRICKS_CRACKED.get()
        )
    }

    @SubscribeEvent
    fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {
        for (dyeColor in DyeColor.entries) {
            val coloredGrassBlock = ModBlocks.getColoredGrass(dyeColor).get()

            event.register(
                { _, _, _, _ -> dyeColor.textureDiffuseColor },
                coloredGrassBlock
            )
        }

        event.register(
            { _, level, pos, _ ->
                if (level == null || pos == null) {
                    GrassColor.getDefaultColor()
                } else {
                    BiomeColors.getAverageGrassColor(level, pos)
                }
            },
            ModBlocks.BIOME_GLASS.get(),
            ModBlocks.BIOME_STONE.get(),
            ModBlocks.BIOME_COBBLESTONE.get(),
            ModBlocks.BIOME_STONE_BRICKS.get(),
            ModBlocks.BIOME_STONE_BRICKS_CHISELED.get(),
            ModBlocks.BIOME_STONE_BRICKS_CRACKED.get()
        )
    }

    @SubscribeEvent
    fun onModelRegistry(event: ModelEvent.RegisterAdditional) {

        ItemProperties.register(
            ModItems.EMERALD_COMPASS.get(),
            EmeraldCompassItem.ANGLE,
            EmeraldCompassItem::getAngleFloat
        )

    }

    @SubscribeEvent
    fun onClientSetup(event: FMLClientSetupEvent) {
        EntityRenderers.register(ModEntityTypes.INDICATOR_DISPLAY.get(), ::BlockDisplayRenderer)
    }

}