package dev.aaronhowser.mods.irregular_implements.datagen.model

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.block.ContactLever
import dev.aaronhowser.mods.irregular_implements.block.RainbowLampBlock
import dev.aaronhowser.mods.irregular_implements.block.TriggerGlass
import dev.aaronhowser.mods.irregular_implements.registries.ModBlocks
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.DirectionalBlock
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.common.data.ExistingFileHelper

class ModBlockStateProvider(
    output: PackOutput,
    private val existingFileHelper: ExistingFileHelper
) : BlockStateProvider(output, IrregularImplements.ID, existingFileHelper) {

    override fun registerStatesAndModels() {

        for (block in singleTextureBlocks) {
            simpleBlockWithItem(block.get(), cubeAll(block.get()))
        }

        for (block in singleTextureTransparentBlocks) {
            singleTextureTransparent(block.get())
        }

        rainbowLamp()

        oneUniqueFace(
            ModBlocks.ANALOG_EMITTER.get(),
            uniqueTexture = modLoc("block/analog_emitter_front"),
            otherTexture = modLoc("block/analog_emitter_side")
        )
        oneUniqueFace(
            ModBlocks.SIDED_BLOCK_OF_REDSTONE.get(),
            uniqueTexture = modLoc("block/sided_redstone_front"),
            otherTexture = modLoc("block/sided_redstone_side")
        )

        triggerGlass()
        platforms()
        luminousBlocks()
        stainedBricks()
        coloredGrass()
        customCraftingTable()
        contactLever()
    }

    private fun contactLever() {
        val block = ModBlocks.CONTACT_LEVER.get()

        getVariantBuilder(block)
            .forAllStates {
                val facing = it.getValue(DirectionalBlock.FACING)
                val enabled = it.getValue(ContactLever.ENABLED)

                val yRotation = when (facing) {
                    Direction.NORTH -> 0
                    Direction.EAST -> 90
                    Direction.SOUTH -> 180
                    Direction.WEST -> 270
                    else -> 0
                }

                val xRotation = when (facing) {
                    Direction.UP -> 270
                    Direction.DOWN -> 90
                    else -> 0
                }

                val modelName = name(block) + if (enabled) "_on" else "_off"
                val sideTexture = "block/contact_lever/" + if (enabled) "side_on" else "side_off"

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .orientable(
                                modelName,
                                modLoc(sideTexture),
                                modLoc("block/contact_lever/front"),
                                modLoc(sideTexture)
                            )
                    )
                    .rotationY(yRotation)
                    .rotationX(xRotation)
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/contact_lever_off"),
                existingFileHelper
            )
        )
    }

    private fun customCraftingTable() {
        val block = ModBlocks.CUSTOM_CRAFTING_TABLE.get()

        val model = models()
            .withExistingParent(name(block), mcLoc("block/bed"))

        simpleBlockWithItem(block, model)
    }

    private fun coloredGrass() {
        for (color in DyeColor.entries) {
            val block = ModBlocks.getColoredGrass(color).get()

            //TODO: Figure out how to color
            val model = models()
                .withExistingParent(name(block), mcLoc("block/grass_block"))

            simpleBlockWithItem(block, model)
        }

    }

    private fun luminousBlocks() {

        for (color in DyeColor.entries) {

            val opaqueBlock = ModBlocks.getLuminousBlock(color).get()
            val translucentBlock = ModBlocks.getLuminousBlockTranslucent(color).get()

            val opaqueTexture = modLoc("block/luminous_block/$color")
            val translucentTexture = modLoc("block/luminous_block/translucent/$color")

            val opaqueModel = models()
                .withExistingParent(name(opaqueBlock), mcLoc("block/block"))
                .texture("all", opaqueTexture)
                .texture("particle", translucentTexture)

                .element()
                .cube("#all")
                .ao(false)
                .emissivity(15, 15)
                .end()

            val translucentModel = models()
                .withExistingParent(name(translucentBlock), "block/block")
                .renderType(RenderType.translucent().name)
                .texture("all", translucentTexture)
                .texture("particle", translucentTexture)

                .element()
                .cube("#all")
                .ao(false)
                .emissivity(15, 15)
                .end()

            simpleBlockWithItem(opaqueBlock, opaqueModel)
            simpleBlockWithItem(translucentBlock, translucentModel)

        }
    }

    private fun stainedBricks() {

        for (color in DyeColor.entries) {

            val regular = ModBlocks.getStainedBrick(color).get()
            val luminous = ModBlocks.getStainedBrickLuminous(color).get()

            val regularTexture = modLoc("block/stained_bricks/$color")
            val luminousBaseTexture = modLoc("block/luminous_stained_brick/base/$color")
            val luminousTintTexture = modLoc("block/luminous_stained_brick/tint/$color")

            val regularModel = models()
                .cubeAll(name(regular), regularTexture)

            val luminousModel = models()
                .withExistingParent(name(luminous), mcLoc("block/block"))
                .texture("base", luminousBaseTexture)
                .texture("tint", luminousTintTexture)
                .texture("particle", luminousTintTexture)
                .renderType(RenderType.cutoutMipped().name)

                .element()
                .cube("#base")
                .end()

                .element()
                .cube("#tint")
                .ao(false)
                .emissivity(15, 15)
                .end()

            simpleBlockWithItem(regular, regularModel)
            simpleBlockWithItem(luminous, luminousModel)
        }

    }

    private fun platforms() {
        val platformTextureMap = mapOf(
            ModBlocks.OAK_PLATFORM to mcLoc("block/oak_planks"),
            ModBlocks.SPRUCE_PLATFORM to mcLoc("block/spruce_planks"),
            ModBlocks.BIRCH_PLATFORM to mcLoc("block/birch_planks"),
            ModBlocks.JUNGLE_PLATFORM to mcLoc("block/jungle_planks"),
            ModBlocks.ACACIA_PLATFORM to mcLoc("block/acacia_planks"),
            ModBlocks.DARK_OAK_PLATFORM to mcLoc("block/dark_oak_planks"),
            ModBlocks.CRIMSON_PLATFORM to mcLoc("block/crimson_planks"),
            ModBlocks.WARPED_PLATFORM to mcLoc("block/warped_planks"),
            ModBlocks.MANGROVE_PLATFORM to mcLoc("block/mangrove_planks"),
            ModBlocks.BAMBOO_PLATFORM to mcLoc("block/bamboo_planks"),
            ModBlocks.CHERRY_PLATFORM to mcLoc("block/cherry_planks"),
            ModBlocks.SUPER_LUBRICANT_PLATFORM to modLoc("block/super_lubricant_ice"),
            ModBlocks.FILTERED_SUPER_LUBRICANT_PLATFORM to modLoc("block/filtered_super_lubricant_platform")
        )

        for ((deferred, texture) in platformTextureMap) {
            val modelName = "block/" + name(deferred.get())

            val model = models()
                .withExistingParent(modelName, mcLoc("block/block"))
                .texture("texture", texture)
                .texture("particle", texture)

                .element()
                .from(0f, 15f, 0f)
                .to(16f, 16f, 16f)
                .textureAll("#texture")
                .end()

            simpleBlockWithItem(deferred.get(), model)
        }
    }

    private fun triggerGlass() {
        val block = ModBlocks.TRIGGER_GLASS.get()

        getVariantBuilder(block)
            .forAllStates {
                val notSolid = it.getValue(TriggerGlass.NOT_SOLID)
                val modelName = name(block) + if (notSolid) "_triggered" else ""
                val textureLocation = if (notSolid) "block/trigger_glass_triggered" else "block/trigger_glass"

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .cubeAll(
                                modelName,
                                modLoc(textureLocation)
                            )
                            .renderType(RenderType.translucent().name)
                    )
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/trigger_glass"),
                existingFileHelper
            )
        )
    }

    private fun oneUniqueFace(
        block: Block,
        uniqueTexture: ResourceLocation,
        otherTexture: ResourceLocation
    ) {
        val name = name(block)

        getVariantBuilder(block)
            .forAllStates {
                val facing = it.getValue(DirectionalBlock.FACING)

                val yRotation = when (facing) {
                    Direction.NORTH -> 0
                    Direction.EAST -> 90
                    Direction.SOUTH -> 180
                    Direction.WEST -> 270
                    else -> 0
                }

                val xRotation = when (facing) {
                    Direction.UP -> 270
                    Direction.DOWN -> 90
                    else -> 0
                }

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .orientable(
                                name,
                                otherTexture,
                                uniqueTexture,
                                otherTexture
                            )
                    )
                    .rotationY(yRotation)
                    .rotationX(xRotation)
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/$name"),
                existingFileHelper
            )
        )
    }

    private fun rainbowLamp() {
        val block = ModBlocks.RAINBOW_LAMP.get()

        getVariantBuilder(block)
            .forAllStates {
                val colorInt = it.getValue(RainbowLampBlock.COLOR)
                val colorString = when (colorInt) {
                    0 -> "white"
                    1 -> "light_gray"
                    2 -> "gray"
                    3 -> "black"
                    4 -> "red"
                    5 -> "orange"
                    6 -> "yellow"
                    7 -> "lime"
                    8 -> "green"
                    9 -> "light_blue"
                    10 -> "cyan"
                    11 -> "blue"
                    12 -> "purple"
                    13 -> "magenta"
                    14 -> "pink"
                    15 -> "brown"
                    else -> error("Invalid color")
                }

                ConfiguredModel
                    .builder()
                    .modelFile(
                        models().cubeAll(
                            "${name(block)}_$colorString",
                            modLoc("block/rainbow_lamp/$colorString")
                        )
                    )
                    .build()
            }

        simpleBlockItem(
            block,
            ItemModelBuilder(
                modLoc("block/rainbow_lamp_white"),
                existingFileHelper
            )
        )
    }

    private val singleTextureTransparentBlocks = listOf(
        ModBlocks.BLOCK_OF_STICKS,
        ModBlocks.RETURNING_BLOCK_OF_STICKS,
        ModBlocks.BIOME_GLASS,
        ModBlocks.LAPIS_GLASS,
        ModBlocks.QUARTZ_GLASS
    )

    private fun singleTextureTransparent(block: Block) {
        val model = models()
            .cubeAll(name(block), blockTexture(block))
            .renderType(RenderType.translucent().name)

        simpleBlockWithItem(block, model)
    }

    private val singleTextureBlocks = listOf(
        ModBlocks.SUPER_LUBRICANT_STONE,
        ModBlocks.SPECTRE_PLANKS,
        ModBlocks.SOUND_DAMPENER,
        ModBlocks.REDSTONE_OBSERVER,
        ModBlocks.QUARTZ_LAMP,
        ModBlocks.ENTITY_DETECTOR,
        ModBlocks.BEAN_POD
    )

    private fun name(block: Block): String {
        return BuiltInRegistries.BLOCK.getKey(block).path
    }

}