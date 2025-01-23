package dev.aaronhowser.mods.irregular_implements.datagen.modonomicon.categories

import com.klikli_dev.modonomicon.api.datagen.CategoryProvider
import com.klikli_dev.modonomicon.api.datagen.ModonomiconProviderBase
import com.klikli_dev.modonomicon.api.datagen.book.BookEntryModel
import com.klikli_dev.modonomicon.api.datagen.book.BookIconModel
import dev.aaronhowser.mods.irregular_implements.datagen.modonomicon.entries.BaseEntryProvider
import dev.aaronhowser.mods.irregular_implements.registry.ModItems

class ItemsCategoryProvider(
    parent: ModonomiconProviderBase
) : CategoryProvider(parent) {

    private val realThis = this

    override fun categoryId(): String = "items"
    override fun categoryName(): String = "Items"
    override fun categoryIcon(): BookIconModel = BookIconModel.create(ModItems.LAVA_CHARM)

    override fun generateEntryMap(): Array<String> {
        return emptyArray()     // Return nothing because it's going to use the list mode
    }

    override fun generateEntries() {

        this.add(stableEnderPearl())
        this.add(evilTear())
        this.add(portkey())
        this.add(biomeCrystal())
        this.add(summoningPendulum())

    }

    private fun stableEnderPearl(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Stable Entry Pearl",
            ModItems.STABLE_ENDER_PEARL,
            "stable_ender_pearl"
        ) {
            override fun generatePages() {
                textPage(
                    "Stable Ender Pearl",
                    paragraphs(
                        "Test 1"
                    )
                )

                spotlightPage(
                    ModItems.STABLE_ENDER_PEARL,
                    "Test"
                )
            }
        }

        return entry.generate()
    }

    private fun evilTear(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Evil Tear",
            ModItems.EVIL_TEAR,
            "evil_tear"
        ) {
            override fun generatePages() {
                textPage(
                    "",
                    paragraphs(

                    )
                )

                spotlightPage(
                    ModItems.EVIL_TEAR,
                    ""
                )
            }
        }

        return entry.generate()
    }

    private fun portkey(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Portkey",
            ModItems.PORTKEY,
            "portkey"
        ) {
            override fun generatePages() {
                textPage(
                    "",
                    paragraphs(

                    )
                )

                spotlightPage(
                    ModItems.PORTKEY,
                    ""
                )
            }
        }

        return entry.generate()
    }

    private fun biomeCrystal(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Biome Crystal",
            ModItems.BIOME_CRYSTAL,
            "biome_crystal"
        ) {
            override fun generatePages() {
                textPage(
                    "",
                    paragraphs(

                    )
                )

                spotlightPage(
                    ModItems.BIOME_CRYSTAL,
                    ""
                )
            }
        }

        return entry.generate()
    }

    private fun summoningPendulum(): BookEntryModel {
        val entry = object : BaseEntryProvider(
            realThis,
            "Summoning Pendulum",
            ModItems.SUMMONING_PENDULUM,
            "summoning_pendulum"
        ) {
            override fun generatePages() {
                textPage(
                    "",
                    paragraphs(

                    )
                )

                spotlightPage(
                    ModItems.SUMMONING_PENDULUM,
                    ""
                )
            }
        }

        return entry.generate()
    }

    private fun lootGenerator() {

    }

    private fun bottleOfAir() {

    }

    private fun enderLetter() {

    }

    private fun goldenEgg() {

    }

    private fun emeraldCompass() {

    }

    private fun blazeAndSteel() {

    }

    private fun escapeRope() {

    }

    private fun chunkAnalyzer() {

    }

    private fun lavaCharm() {

    }

    private fun obsidianSkull() {

    }

    private fun obsidianSkullRing() {

    }

    private fun diviningRod() {

    }

    private fun luminousPowder() {

    }

    // Merge all bucket stuff
    private fun enderBuckets() {

    }

    private fun sakanadeSpores() {

    }

    // Merge all lotus stuff
    private fun lotus() {

    }

    // Merge all bean stuff
    private fun bean() {

    }

    private fun magicHood() {

    }

    // And obsidian
    private fun waterWalkingBoots() {

    }

    private fun lavaWaders() {

    }

    private fun spectreArmor() {

    }

    private fun weatherEggs() {

    }

    private fun locationFilter() {

    }

    private fun itemFilter() {

    }

    private fun entityFilter() {

    }

    private fun idCard() {

    }

    private fun imbues() {

    }

    // And blackout powder
    private fun spectreIlluminator() {

    }

    private fun spectreKey() {

    }

    private fun spectreAnchor() {

    }

    private fun spectreChargers() {

    }

    private fun spectreTools() {

    }

    private fun redstoneTool() {

    }

    // Merge remote?
    private fun redstoneActivator() {

    }

    private fun redstoneRemote() {

    }

    private fun floo() {

    }

    private fun soundPattern() {

    }

    private fun soundRecorder() {

    }

    private fun portableSoundDampener() {

    }

    private fun biomeCapsule() {

    }

    private fun biomePainter() {

    }

    // include voiding
    private fun dropFilter() {

    }

    private fun voidStone() {

    }

    private fun whiteStone() {

    }

    private fun magneticForce() {

    }

    private fun portableEnderBridge() {

    }

    private fun blockMover() {

    }

    private fun diamondBreaker() {

    }

    private fun blockReplacer() {

    }

    private fun grassSeeds() {

    }

}