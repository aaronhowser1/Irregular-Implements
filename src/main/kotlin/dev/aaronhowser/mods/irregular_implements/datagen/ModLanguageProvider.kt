package dev.aaronhowser.mods.irregular_implements.datagen

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.registries.ModBlocks
import dev.aaronhowser.mods.irregular_implements.registries.ModItems
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(
    output: PackOutput
) : LanguageProvider(output, IrregularImplements.ID, "en_us") {

    companion object {
        fun String.toComponent(vararg args: Any?): MutableComponent = Component.translatable(this, *args)

        fun getInfoString(itemLike: ItemLike): String {
            val location = BuiltInRegistries.ITEM.getKey(itemLike.asItem())

            return StringBuilder()
                .append("info.")
                .append(location.namespace)
                .append(".")
                .append(location.path)
                .toString()
        }
    }

    object Items {
        const val CREATIVE_TAB = "itemGroup.irregular_implements"
    }

    fun addInfo(itemLike: ItemLike, infoString: String) {
        add(getInfoString(itemLike), infoString)
    }

    object Info {
        const val BIOME_BLOCKS = "Changes color to match the biome."
    }

    override fun addTranslations() {

        add(Items.CREATIVE_TAB, "Irregular Implements")

        addItem(ModItems.ANALOG_EMITTER, "Analog Emitter")
        addItem(ModItems.STABLE_ENDER_PEARL, "Stable Ender Pearl")
        addItem(ModItems.BIOME_CRYSTAL, "Biome Crystal")
        addItem(ModItems.POSITION_FILTER, "Position Filter")
        addItem(ModItems.SUMMONING_PENDULUM, "Summoning Pendulum")
        addItem(ModItems.BEAN, "Bean")
        addItem(ModItems.LESSER_MAGIC_BEAN, "Lesser Magic Bean")
        addItem(ModItems.MAGIC_BEAN, "Magic Bean")
        addItem(ModItems.BEAN_STEW, "Bean Stew")
        addItem(ModItems.WATER_WALKING_BOOTS, "Water Walking Boots")
        addItem(ModItems.LOOT_GENERATOR, "Loot Generator")
        addItem(ModItems.LAVA_CHARM, "Lava Charm")
        addItem(ModItems.LAVA_WADERS, "Lava Waders")
        addItem(ModItems.OBSIDIAN_SKULL, "Obsidian Skull")
        addItem(ModItems.OBSIDIAN_SKULL_RING, "Obsidian Skull Ring")
        addItem(ModItems.OBSIDIAN_WATER_WALKING_BOOTS, "Obsidian Water Walking Boots")
        addItem(ModItems.MAGIC_HOOD, "Magic Hood")
        addItem(ModItems.BOTTLE_OF_AIR, "Bottle of Air")
        addItem(ModItems.BLOOD_STONE, "Blood Stone")
        addItem(ModItems.ENDER_LETTER, "Ender Letter")
        addItem(ModItems.ENTITY_FILTER, "Entity Filter")
        addItem(ModItems.SAKANADE_SPORES, "Sakande Spores")
        addItem(ModItems.EVIL_TEAR, "Evil Tear")
        addItem(ModItems.ECTOPLASM, "Ectoplasm")
        addItem(ModItems.BIOME_SENSOR, "Biome Sensor")
        addItem(ModItems.LUMINOUS_POWDER, "Luminous Powder")
        addItem(ModItems.PLATE_BASE, "Plate Base")
        addItem(ModItems.PRECIOUS_EMERALD, "Precious Emerald")
        addItem(ModItems.LOTUS_BLOSSOM, "Lotus Blossom")
        addItem(ModItems.GOLDEN_EGG, "Golden Egg")
        addItem(ModItems.BLACKOUT_POWDER, "Blackout Powder")
        addItem(ModItems.ITEM_FILTER, "Item Filter")
        addItem(ModItems.GOLDEN_COMPASS, "Golden Compass")
        addItem(ModItems.EMERALD_COMPASS, "Emerald Compass")
        addItem(ModItems.BLAZE_AND_STEEL, "Blaze and Steel")
        addItem(ModItems.RUNE_PATTERN, "Rune Pattern")
        addItem(ModItems.ID_CARD, "ID Card")
        addItem(ModItems.PORTKEY, "Portkey")
        addItem(ModItems.LOTUS_SEEDS, "Lotus Seeds")
        addItem(ModItems.ESCAPE_ROPE, "Escape Rope")
        addItem(ModItems.WEATHER_EGG, "Weather Egg")
        addItem(ModItems.ENDER_BUCKET, "Ender Bucket")
        addItem(ModItems.REINFORCED_ENDER_BUCKET, "Reinforced Ender Bucket")
        addItem(ModItems.CHUNK_ANALYZER, "Chunk Analyzer")
        addItem(ModItems.TIME_IN_A_BOTTLE, "Time in a Bottle")
        addItem(ModItems.ECLIPSED_CLOCK, "Eclipsed Clock")
        addItem(ModItems.GRASS_SEEDS, "Grass Seeds")
        addItem(ModItems.RUNE_DUST, "Rune Dust")
        addItem(ModItems.DIVINING_ROD, "Divining Rod")

        addItem(ModItems.FIRE_IMBUE, "Fire Imbue")
        addItem(ModItems.POISON_IMBUE, "Poison Imbue")
        addItem(ModItems.EXPERIENCE_IMBUE, "Experience Imbue")
        addItem(ModItems.WITHER_IMBUE, "Wither Imbue")

        addItem(ModItems.SPECTRE_INGOT, "Spectre Ingot")
        addItem(ModItems.SPECTRE_STRING, "Spectre String")
        addItem(ModItems.SPECTRE_ILLUMINATOR, "Spectre Illuminator")
        addItem(ModItems.SPECTRE_KEY, "Spectre Key")
        addItem(ModItems.SPECTRE_ANCHOR, "Spectre Anchor")
        addItem(ModItems.SPECTRE_SWORD, "Spectre Sword")
        addItem(ModItems.SPECTRE_PICKAXE, "Spectre Pickaxe")
        addItem(ModItems.SPECTRE_AXE, "Spectre Axe")
        addItem(ModItems.SPECTRE_SHOVEL, "Spectre Shovel")
        addItem(ModItems.SPECTRE_CHARGER, "Spectre Charger")

        addItem(ModItems.ADVANCED_REDSTONE_REPEATER, "Advanced Redstone Repeater")
        addItem(ModItems.ADVANCED_REDSTONE_TORCH, "Advanced Redstone Torch")
        addItem(ModItems.REDSTONE_TOOL, "Redstone Tool")
        addItem(ModItems.REDSTONE_ACTIVATOR, "Redstone Activator")
        addItem(ModItems.REDSTONE_REMOTE, "Redstone Remote")

        addItem(ModItems.SUPER_LUBRICANT_TINCTURE, "Super Lubricant Tincture")
        addItem(ModItems.SUPER_LUBRICANT_BOOTS, "Super Lubricant Boots")

        addItem(ModItems.FLOO_POWDER, "Floo Powder")
        addItem(ModItems.FLOO_SIGN, "Floo Sign")
        addItem(ModItems.FLOO_TOKEN, "Floo Token")
        addItem(ModItems.FLOO_POUCH, "Floo Pouch")

        addItem(ModItems.SOUND_PATTERN, "Sound Pattern")
        addItem(ModItems.SOUND_RECORDER, "Sound Recorder")
        addItem(ModItems.PORTABLE_SOUND_DAMPENER, "Portable Sound Dampener")

        addBlock(ModBlocks.FERTILIZED_DIRT, "Fertilized Dirt")
        addBlock(ModBlocks.LAPIS_GLASS, "Lapis Glass")
        addBlock(ModBlocks.LAPIS_LAMP, "Lapis Lamp")
        addBlock(ModBlocks.DYEING_MACHINE, "Dyeing Machine")
        addBlock(ModBlocks.ENDER_BRIDGE, "Ender Bridge")
        addBlock(ModBlocks.PRISMARINE_ENDER_BRIDGE, "Prismarine Ender Bridge")
        addBlock(ModBlocks.ENDER_ANCHOR, "Ender Anchor")
        addBlock(ModBlocks.BEAN_POD, "Bean Pod")
        addBlock(ModBlocks.LIGHT_REDIRECTOR, "Light Redirector")
        addBlock(ModBlocks.IMBUING_STATION, "Imbuing Station")
        addBlock(ModBlocks.NATURE_CHEST, "Nature Chest")
        addBlock(ModBlocks.WATER_CHEST, "Water Chest")
        addBlock(ModBlocks.ANALOG_EMITTER, "Analog Emitter Block")
        addBlock(ModBlocks.FLUID_DISPLAY, "Fluid Display")
        addBlock(ModBlocks.CUSTOM_WORKBENCH, "Custom Workbench")
        addBlock(ModBlocks.ENDER_MAILBOX, "Ender Mailbox")
        addBlock(ModBlocks.PITCHER_PLANT, "Pitcher Plant")
        addBlock(ModBlocks.PLATFORM, "Platform")
        addBlock(ModBlocks.QUARTZ_LAMP, "Quartz Lamp")
        addBlock(ModBlocks.QUARTZ_GLASS, "Quartz Glass")
        addBlock(ModBlocks.POTION_VAPORIZER, "Potion Vaporizer")
        addBlock(ModBlocks.CONTACT_BUTTON, "Contact Button")
        addBlock(ModBlocks.CONTACT_LEVER, "Contact Lever")
        addBlock(ModBlocks.RAIN_SHIELD, "Rain Shield")
        addBlock(ModBlocks.BLOCK_BREAKER, "Block Breaker")
        addBlock(ModBlocks.COMPRESSED_SLIME_BLOCK, "Compressed Slime Block")
        addBlock(ModBlocks.REDSTONE_OBSERVER, "Redstone Observer")
        addBlock(ModBlocks.BIOME_RADAR, "Biome Radar")
        addBlock(ModBlocks.IRON_DROPPER, "Iron Dropper")
        addBlock(ModBlocks.IGNITER, "Igniter")
        addBlock(ModBlocks.BLOCK_OF_STICKS, "Block of Sticks")
        addBlock(ModBlocks.RETURNING_BLOCK_OF_STICKS, "Returning Block of Sticks")
        addBlock(ModBlocks.LUMINOUS_BLOCK, "Luminous Block")
        addBlock(ModBlocks.TRANSLUCENT_LUMINOUS_BLOCK, "Translucent Luminous Block")
        addBlock(ModBlocks.INVENTORY_REROUTER, "Inventory Rerouter")
        addBlock(ModBlocks.SLIME_CUBE, "Slime Cube")
        addBlock(ModBlocks.PEACE_CANDLE, "Peace Candle")
        addBlock(ModBlocks.GLOWING_MUSHROOM, "Glowing Mushroom")
        addBlock(ModBlocks.INVENTORY_TESTER, "Inventory Tester")
        addBlock(ModBlocks.TRIGGER_GLASS, "Trigger Glass")
        addBlock(ModBlocks.BLOCK_DESTABILIZER, "Block Destabilizer")
        addBlock(ModBlocks.SOUND_BOX, "Sound Box")
        addBlock(ModBlocks.SOUND_DAMPENER, "Sound Dampener")
        addBlock(ModBlocks.DIAPHANOUS_BLOCK, "Diaphanous Block")
        addBlock(ModBlocks.SIDED_BLOCK_OF_REDSTONE, "Sided Block of Redstone")
        addBlock(ModBlocks.ITEM_COLLECTOR, "Item Collector")
        addBlock(ModBlocks.ADVANCED_ITEM_COLLECTOR, "Advanced Item Collector")
        addBlock(ModBlocks.NATURE_CORE, "Nature Core")
        addBlock(ModBlocks.COLORED_GRASS, "Colored Grass")
        addBlock(ModBlocks.RAINBOW_LAMP, "Rainbow Lamp")
        addBlock(ModBlocks.STAINED_BRICKS, "Stained Bricks")
        addBlock(ModBlocks.LUMINOUS_STAINED_BRICKS, "Luminous Stained Bricks")

        addBlock(ModBlocks.SUPER_LUBRICANT_ICE, "Super Lubricant Ice")
        addBlock(ModBlocks.SUPER_LUBRICANT_PLATFORM, "Super Lubricant Platform")
        addBlock(ModBlocks.FILTERED_SUPER_LUBRICANT_PLATFORM, "Filtered Super Lubricant Platform")
        addBlock(ModBlocks.SUPER_LUBRICANT_STONE, "Super Lubricant Stone")

        addBlock(ModBlocks.ONLINE_DETECTOR, "Online Detector")
        addBlock(ModBlocks.CHAT_DETECTOR, "Chat Detector")
        addBlock(ModBlocks.GLOBAL_CHAT_DETECTOR, "Global Chat Detector")
        addBlock(ModBlocks.ENTITY_DETECTOR, "Entity Detector")

        addBlock(ModBlocks.PLAYER_INTERFACE, "Player Interface")
//        addBlock(ModBlocks.CREATIVE_PLAYER_INTERFACE, "Creative Player Interface")
        addBlock(ModBlocks.NOTIFICATION_INTERFACE, "Notification Interface")
        addBlock(ModBlocks.BASIC_REDSTONE_INTERFACE, "Basic Redstone Interface")
        addBlock(ModBlocks.ADVANCED_REDSTONE_INTERFACE, "Advanced Redstone Interface")

        addBlock(ModBlocks.SPECTRE_BLOCK, "Spectre Block")
        addBlock(ModBlocks.SPECTRE_LENS, "Spectre Lens")
        addBlock(ModBlocks.SPECTRE_ENERGY_INJECTOR, "Spectre Energy Injector")
        addBlock(ModBlocks.SPECTRE_COIL, "Spectre Coil")
        addBlock(ModBlocks.SPECTRE_COIL_REDSTONE, "Spectre Coil Redstone")
        addBlock(ModBlocks.SPECTRE_COIL_ENDER, "Spectre Coil Ender")
        addBlock(ModBlocks.SPECTRE_COIL_NUMBER, "Spectre Coil Number")
        addBlock(ModBlocks.SPECTRE_COIL_GENESIS, "Spectre Coil Genesis")
        addBlock(ModBlocks.SPECTRE_PLANKS, "Spectre Planks")
        addBlock(ModBlocks.SPECTRE_SAPLING, "Spectre Sapling")
        addBlock(ModBlocks.SPECTRE_WOOD, "Spectre Wood")
        addBlock(ModBlocks.SPECTRE_LEAVES, "Spectre Leaves")

        addBlock(ModBlocks.BIOME_COBBLESTONE, "Biome Cobblestone")
        addBlock(ModBlocks.BIOME_STONE, "Biome Stone")
        addBlock(ModBlocks.BIOME_STONE_BRICKS, "Biome Stone Bricks")
        addBlock(ModBlocks.BIOME_STONE_BRICKS_CRACKED, "Cracked Biome Stone Bricks")
        addBlock(ModBlocks.BIOME_STONE_BRICKS_CHISELED, "Chiseled Biome Stone Bricks")
        addBlock(ModBlocks.BIOME_GLASS, "Biome Glass")

        addBlock(ModBlocks.PROCESSING_PLATE, "Processing Plate")
        addBlock(ModBlocks.REDIRECTOR_PLATE, "Redirector Plate")
        addBlock(ModBlocks.FILTERED_REDIRECTOR_PLATE, "Filtered Redirector Plate")
        addBlock(ModBlocks.REDSTONE_PLATE, "Redstone Plate")
        addBlock(ModBlocks.CORRECTOR_PLATE, "Corrector Plate")
        addBlock(ModBlocks.ITEM_SEALER_PLATE, "Item Sealer Plate")
        addBlock(ModBlocks.ITEM_REJUVINATOR_PLATE, "Item Rejuvinator Plate")
        addBlock(ModBlocks.ACCELERATOR_PLATE, "Accelerator Plate")
        addBlock(ModBlocks.DIRECTIONAL_ACCELERATOR_PLATE, "Directional Accelerator Plate")
        addBlock(ModBlocks.BOUNCY_PLATE, "Bouncy Plate")
        addBlock(ModBlocks.COLLECTION_PLATE, "Collection Plate")
        addBlock(ModBlocks.EXTRACTION_PLATE, "Extraction Plate")

        addBlock(ModBlocks.FLOO_BRICK, "Floo Bricks")
        addBlock(ModBlocks.ANCIENT_BRICK, "Ancient Brick")

        addInfo(ModBlocks.FERTILIZED_DIRT, "Fertilized Dirt does not require hydration, grows crops 3 times faster, and can't be trampled.\n\nYou still have to till it with a Hoe.")
        addInfo(ModBlocks.PLAYER_INTERFACE, "Exposes the inventory of the block's owner, as if it was the block's inventory.")
        addInfo(ModBlocks.LAPIS_GLASS, "Solid for players, not solid for anything else.")
        addInfo(ModBlocks.LAPIS_LAMP, "Provides false light, which changes visibility but does not affect mob spawning.")
        addInfo(ModBlocks.DYEING_MACHINE, "Allows you to add a color filter to either an item's texture, or its enchantment glint.")
        addInfo(ModBlocks.ONLINE_DETECTOR, "Emits a Redstone signal if the specified player is online.")
        addInfo(ModBlocks.CHAT_DETECTOR, "Emits a Redstone pulse if the block's owner sends a chat message containing some specified text.")
        addInfo(ModBlocks.ENDER_BRIDGE, "Upon the Ender Bridge receiving a Redstone Signal, it looks for an Ender Anchor in front of it.\n\nIt searches at 20 blocks per second, and then teleports the player standing on top to it.\n\nThere's no distance limit, though there can be no blocks between (save for the block directly in front of the Bridge).")
        addInfo(ModBlocks.PRISMARINE_ENDER_BRIDGE, "Upon the Prismarine Ender Bridge receiving a Redstone Signal, it looks for an Ender Anchor in front of it (with no distance limit, though it must be loaded, and no other blocks can be in the way).\n\nIt searches at 200 blocks per second, and then teleports the player standing on top to it.\n\nThere's no distance limit, though there can be no blocks between (save for the block directly in front of the Bridge).")
        addInfo(ModBlocks.ENDER_ANCHOR, "Works with the Ender Bridge or Prismarine Ender Bridge.")
        addInfo(ModBlocks.BEAN_POD, "Found at the top of a grown Magic Bean.\n\nContains loot!")
        addInfo(ModBlocks.LIGHT_REDIRECTOR, "Swaps the appearance of blocks placed on opposite sides.\n\nYou can set a side to not be changed by right-clicking it. This does not prevent the opposite side from copying it.")
        addInfo(ModBlocks.SPECTRE_BLOCK, "An indestructible block that spawns in the Spectre Dimension.")
        addInfo(ModBlocks.ANALOG_EMITTER, "Emits a configurable Redstone signal.")
        addInfo(ModBlocks.FLUID_DISPLAY, "A solid block that uses the texture of a fluid.\n\nSet the fluid by clicking it with a filled Bucket or fluid container.\n\nRight-click to toggle between still and flowing, and sneak right-click to rotate it.")
        addInfo(ModBlocks.ENDER_MAILBOX, "Allows players to send and receive Ender Letters.")
        addInfo(ModBlocks.PITCHER_PLANT, "Acts as an infinite water source.\n\nSlowly fills adjacent fluid containers.")
        addInfo(ModBlocks.PLATFORM, "Only solid to non-sneaking entities.")
        addInfo(ModBlocks.ENTITY_DETECTOR, "Emits a Redstone signal if a configured entity is within a configured range.")
        addInfo(ModBlocks.QUARTZ_LAMP, "Provides invisible light, which can't be seen but affects mob spawning.")   //TODO: Make some joke about radiation
        addInfo(ModBlocks.QUARTZ_GLASS, "Solid for everything but players.")
        addInfo(ModBlocks.POTION_VAPORIZER, "Allows you to fill a room with a potion effect.\n\nInsert Potions and Furnace fuel, and it will scan the area in front of it to check if it's an enclosed area.\n\nIf it is, it'll fill it with the potion effect.")
        addInfo(ModBlocks.CONTACT_LEVER, "Acts like a Lever, but if the block in front of it is clicked instead.")
        addInfo(ModBlocks.CONTACT_BUTTON, "Acts like a Button, but if the block in front of it is clicked instead.")
        addInfo(ModBlocks.RAIN_SHIELD, "Prevents rain in a 5 chunk radius.")
        addInfo(ModBlocks.BLOCK_BREAKER, "Breaks blocks in front of it with the effectiveness of an Iron Pickaxe. Drops are inserted into the inventory behind it, or dropped.\n\nCan be disabled with a Redstone signal.")
        addInfo(ModBlocks.SUPER_LUBRICANT_ICE, "No friction.")
        addInfo(ModBlocks.SUPER_LUBRICANT_STONE, "No friction.")
        addInfo(ModBlocks.COMPRESSED_SLIME_BLOCK, "Create by clicking a Slime Block with a Shovel. Can be compressed multiple times.\n\nLaunches entities into the air.")
        addInfo(ModBlocks.REDSTONE_OBSERVER, "Emits the same Redstone signal as a linked block.\n\nUse a Redstone Tool to link it to a block.")
        addInfo(ModBlocks.BIOME_RADAR, "Insert a Biome Crystal and provide a Redstone signal, and it will attempt to find the Crystal's biome.\n\nIf it succeeds, it will spawn particles aiming in its direction.\n\nUsing a Paper on it will create a Position Filter for the biome.")
        addInfo(ModBlocks.IRON_DROPPER, "An upgraded Dropper, where the following can be configured:\n\n• Pickup Delay\n• Item motion randomness\n• Particle/Sound toggle\n• Redstone control")
        addInfo(ModBlocks.IGNITER, "Sets or extinguishes the block on front of it, depending on the Redstone signal.\n\nCan be set to keep the fire lit.")
        addInfo(ModBlocks.BLOCK_OF_STICKS, "Cheap building material that automatically breaks after 10 seconds.")
        addInfo(ModBlocks.RETURNING_BLOCK_OF_STICKS, "Works like a Block of Sticks, but the item drop is teleported to the nearest player.")
        addInfo(ModBlocks.INVENTORY_REROUTER, "??????????")
        addInfo(ModBlocks.SLIME_CUBE, "Causes Slimes to spawn in great numbers in the chunk its in.\n\nWhen powered, it instead prevents Slimes from spawning in the chunk.")
        addInfo(ModBlocks.PEACE_CANDLE, "Prevents natural mob spawns in a 3 chunk radius.\n\nCan be found in roughly one third of Villager Churches.")
        addInfo(ModBlocks.NOTIFICATION_INTERFACE, "Sends a configurable toast notification to the block's owner when a Redstone signal is received.")
        addInfo(ModBlocks.INVENTORY_TESTER, "When placed on an inventory, constantly simulates attempting to insert a configured Item Stack.\n\nIf the simulation would succeed, emits a Redstone signal.")
        addInfo(ModBlocks.GLOBAL_CHAT_DETECTOR, "Emits a redstone pulse if any player sends a chat message containing the specified text.\n\nCan have a player whitelist using ID Cards.")
        addInfo(ModBlocks.TRIGGER_GLASS, "When given a Redstone pulse, loses its collision for 3 seconds.\n\nAlso triggers adjacent Trigger Glass.")
        addInfo(ModBlocks.BLOCK_DESTABILIZER, "When given a Redstone pulse, scans the blocks in front of it.\n\nIt then gives the nearest 50 connected blocks gravity, making them fall.\n\nYou can set it to \"lazy\" mode, which makes it remember the shape, and only destabilize those blocks.")
        addInfo(ModBlocks.SOUND_BOX, "When given a Redstone pulse, plays the held Sound Pattern.")
        addInfo(ModBlocks.SOUND_DAMPENER, "Prevents sounds matching held Sound Patterns from being heard, within 10 blocks.")
        addInfo(ModBlocks.DIAPHANOUS_BLOCK, "Can be set to look like any regular block. Loses opacity the closer a player is to them, and has no collision when invisible.\n\nCraft it by itself to invert this behavior, so it gains opacity the closer a player is.")
        addInfo(ModBlocks.SIDED_BLOCK_OF_REDSTONE, "Emits a Redstone signal on only one side.")
        addInfo(ModBlocks.SPECTRE_LENS, "Place on a Beacon, and its effects will be granted to you no matter the distance.\n\nStill requires you be in the same dimension, and you can only have one Lens per dimension.")
        addInfo(ModBlocks.SPECTRE_ENERGY_INJECTOR, "Acts like an Ender Chest for FE.\n\nThe Energy Injector can insert FE, Spectre Coils are required to extract it.\n\nA player's Spectre Energy Buffer can store up to 1,000,000 FE by default.")
        addInfo(ModBlocks.SPECTRE_COIL, "Outputs FE from the Spectre Energy Buffer, which is filled using Spectre Energy Injectors.\n\nHas a rate of 1,024 FE/t.")
        addInfo(ModBlocks.SPECTRE_COIL_REDSTONE, "Outputs FE from the Spectre Energy Buffer, which is filled using Spectre Energy Injectors.\n\nHas a rate of 4,096 FE/t.")
        addInfo(ModBlocks.SPECTRE_COIL_ENDER, "Outputs FE from the Spectre Energy Buffer, which is filled using Spectre Energy Injectors.\n\nHas a rate of 20,480 FE/t.")
        addInfo(ModBlocks.SPECTRE_COIL_NUMBER, "Generates 128 FE/t for free, inserting it into the block it's placed on.")
        addInfo(ModBlocks.SPECTRE_COIL_GENESIS, "Once every 10 seconds, tries to insert an infinite amount of FE into the block it's placed on.")
        addInfo(ModItems.ADVANCED_REDSTONE_REPEATER, "A Redstone Repeater that can have both its Step Up and Step Down delays configured.")
        addInfo(ModItems.ADVANCED_REDSTONE_TORCH, "A Redstone Torch that can have its Powered and Unpowered output strengths configured")
        addInfo(ModBlocks.SPECTRE_SAPLING, "Created by using Ectoplasm on a Sapling.\n\nGrows into a Spectre Tree, whose blocks have a chance of dropping Ectoplasm.")
        addInfo(ModBlocks.ITEM_COLLECTOR, "Collects items in a 3 block radius and inserts it into the inventory it's placed on.")
        addInfo(ModBlocks.ADVANCED_ITEM_COLLECTOR, "Collects items in a configurable radius and inserts it into the inventory it's placed on.\n\nYou can also insert an Item Filter for more control.")
        addInfo(ModBlocks.BIOME_GLASS, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.BIOME_STONE_BRICKS, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.BIOME_STONE_BRICKS_CHISELED, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.BIOME_STONE_BRICKS_CRACKED, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.BIOME_STONE, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.BIOME_COBBLESTONE, Info.BIOME_BLOCKS)
        addInfo(ModBlocks.RAINBOW_LAMP, "Changes color depending on the Redstone signal strength.\n\nOnly the texture changes, the light stays the same color.")
        addInfo(ModBlocks.BASIC_REDSTONE_INTERFACE, "Powers the linked block with the Redstone signal it's receiving.\n\nUse a Redstone Tool to link it to a block.")
        addInfo(ModBlocks.ADVANCED_REDSTONE_INTERFACE, "Powers up to 9 linked blocks with the Redstone signal it's receiving.\n\nUse a Redstone Tool to link it to blocks.")
        addInfo(ModBlocks.REDIRECTOR_PLATE, "Has two \"enabled\" sides. Entities that enter from one side are sent to the other side.")
        addInfo(ModBlocks.FILTERED_REDIRECTOR_PLATE, "Has two \"input\" sides, and two color-coded \"output\" sides.\n\nBoth output sides have a slot for an Entity Filter, and any entity that enters from the input sides are teleported to the output with a matching Filter.")
        addInfo(ModBlocks.REDSTONE_PLATE, "Has an \"input\" side, a \"default\" side, and a \"powered\" side.\n\nEntities that entier from the input side are teleported to the default side if the Plate is unpowered, or the powered side if it is.")
        addInfo(ModBlocks.CORRECTOR_PLATE, "Entities moving on the Plate are centered along the axis they're moving.\n\nFor example, if they're moving South, they're teleported to the middle of the North-South axis of the block.")
        addInfo(ModBlocks.ITEM_SEALER_PLATE, "Dropped item entities that pass over this Plate have their pickup delay set to 30 seconds.")
        addInfo(ModBlocks.ITEM_REJUVINATOR_PLATE, "Dropped item entities that pass over this Plate have their despawn timer set to 4 minutes.")
        addInfo(ModBlocks.ACCELERATOR_PLATE, "Entities moving on this Plate are sped up slightly, to a limit.")
        addInfo(ModBlocks.DIRECTIONAL_ACCELERATOR_PLATE, "Entities moving on this Plate are sped up slightly, to a limit, in the direction the Plate is facing.")
        addInfo(ModBlocks.BOUNCY_PLATE, "Entities on this Plate are propelled upwards.")
        addInfo(ModBlocks.COLLECTION_PLATE, "Dropped item entities that pass over this Plate attempt to insert into adjacent inventories.")
        addInfo(ModBlocks.EXTRACTION_PLATE, "Has an \"input\" side and an \"output\" side. Extracts stacks from the input side, and drops or inserts them on the output side, depending on if there's an inventory there.\n\nRight-click with an empty hand to change the output side, and do so while sneaking to change the input side.")



    }
}