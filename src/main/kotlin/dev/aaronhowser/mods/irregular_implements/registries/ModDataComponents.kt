package dev.aaronhowser.mods.irregular_implements.registries

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.item.component.LocationItemComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.world.entity.EntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, IrregularImplements.ID)

    val LOCATION: DeferredHolder<DataComponentType<*>, DataComponentType<LocationItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("location") {
            it
                .persistent(LocationItemComponent.CODEC)
                .networkSynchronized(LocationItemComponent.STREAM_CODEC)
        }

    val ENTITY_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<EntityType<*>>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("entity_type") {
            it
                .persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
                .networkSynchronized(ByteBufCodecs.registry(Registries.ENTITY_TYPE))
        }

}