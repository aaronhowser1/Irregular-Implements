package dev.aaronhowser.mods.irregular_implements.registries

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

    val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, IrregularImplements.ID)

    val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = TABS_REGISTRY.register("creative_tab", Supplier {
        CreativeModeTab.builder()
            .title(Component.literal("A"))
            .icon { ModItems.SPECTRE_PICKAXE.toStack() }
            .displayItems { displayContext: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
                val regularItems =
                    ModItems.ITEM_REGISTRY.entries

                val itemsToDisplay = buildList {
                    addAll(regularItems.map { (it as DeferredItem).toStack() })

//                    add(EntityDnaItem.getOrganicStack(EntityType.PIG))
//                    add(EntityDnaItem.getCell(EntityType.PIG))
//                    addAll(DnaHelixItem.getAllHelices(displayContext.holders))
//                    addAll(PlasmidItem.getAllPlasmids(displayContext.holders))
                }

                output.acceptAll(itemsToDisplay)
            }
            .build()
    })

}