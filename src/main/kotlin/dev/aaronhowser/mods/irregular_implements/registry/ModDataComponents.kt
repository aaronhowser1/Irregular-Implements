package dev.aaronhowser.mods.irregular_implements.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.item.WeatherEggItem
import dev.aaronhowser.mods.irregular_implements.item.component.*
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import dev.aaronhowser.mods.irregular_implements.util.SpecificEntity
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.util.Unit
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object ModDataComponents {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, IrregularImplements.ID)

	val LOCATION: DeferredHolder<DataComponentType<*>, DataComponentType<LocationDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("location") {
			it
				.persistent(LocationDataComponent.CODEC)
				.networkSynchronized(LocationDataComponent.STREAM_CODEC)
		}

	val ENTITY_TYPE: DeferredHolder<DataComponentType<*>, DataComponentType<EntityType<*>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("entity_type") {
			it
				.persistent(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
				.networkSynchronized(ByteBufCodecs.registry(Registries.ENTITY_TYPE))
		}

	val UUID: DeferredHolder<DataComponentType<*>, DataComponentType<UUID>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("uuid") { builder ->
			builder
				.persistent(OtherUtil.UUID_CODEC)
				.networkSynchronized(OtherUtil.UUID_STREAM_CODEC)
		}

	val BIOME: DeferredHolder<DataComponentType<*>, DataComponentType<Holder<Biome>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("biome") {
			it
				.persistent(Biome.CODEC)
				.networkSynchronized(ByteBufCodecs.holderRegistry(Registries.BIOME))
		}

	val PLAYER: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntity>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("player") {
			it
				.persistent(SpecificEntity.CODEC)
				.networkSynchronized(SpecificEntity.STREAM_CODEC)
		}

	val ENTITY_IDENTIFIER: DeferredHolder<DataComponentType<*>, DataComponentType<SpecificEntity>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("entity_identifier") {
			it
				.persistent(SpecificEntity.CODEC)
				.networkSynchronized(SpecificEntity.STREAM_CODEC)
		}

	val ENTITY_LIST: DeferredHolder<DataComponentType<*>, DataComponentType<List<CustomData>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("entity_list") {
			it
				.persistent(CustomData.CODEC_WITH_ID.listOf())
				.networkSynchronized(CustomData.STREAM_CODEC.apply(ByteBufCodecs.list()))
		}

	val FLUID_TAGS: DeferredHolder<DataComponentType<*>, DataComponentType<List<TagKey<Fluid>>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("fluid_tags") {
			it
				.persistent(
					TagKey.codec(Registries.FLUID)
						.listOf()
				)
				.networkSynchronized(
					OtherUtil.tagKeyStreamCodec(Registries.FLUID)
						.apply(ByteBufCodecs.list())
				)
		}

	val REDSTONE_REMOTE: DeferredHolder<DataComponentType<*>, DataComponentType<RedstoneRemoteDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("redstone_remote") {
			it
				.persistent(RedstoneRemoteDataComponent.CODEC)
				.networkSynchronized(RedstoneRemoteDataComponent.STREAM_CODEC)
		}

	@JvmField
	val LUBRICATED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("lubricated") {
			it
				.persistent(Unit.CODEC)
				.networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
		}

	val DURATION: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("duration") {
			it
				.persistent(Codec.INT)
				.networkSynchronized(ByteBufCodecs.VAR_INT)
		}

	val BIOME_POINTS: DeferredHolder<DataComponentType<*>, DataComponentType<BiomePointsDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("biome_points") {
			it
				.persistent(BiomePointsDataComponent.CODEC)
				.networkSynchronized(BiomePointsDataComponent.STREAM_CODEC)
		}

	val BLOCK_DATA: DeferredHolder<DataComponentType<*>, DataComponentType<BlockDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("block_data") {
			it
				.persistent(BlockDataComponent.CODEC)
				.networkSynchronized(BlockDataComponent.STREAM_CODEC)
		}

	val CHARGE: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("charge") {
			it
				.persistent(Codec.INT)
				.networkSynchronized(ByteBufCodecs.VAR_INT)
		}

	val COOLDOWN: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("cooldown") {
			it
				.persistent(Codec.INT)
				.networkSynchronized(ByteBufCodecs.VAR_INT)
		}

	val IS_ENABLED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("is_enabled") {
			it
				.persistent(Unit.CODEC)
				.networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
		}

	val IS_INVERTED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("is_inverted") {
			it
				.persistent(Unit.CODEC)
				.networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
		}

	val BLOCK_TAG: DeferredHolder<DataComponentType<*>, DataComponentType<TagKey<Block>>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("block_tag") {
			it
				.persistent(TagKey.codec(Registries.BLOCK))
				.networkSynchronized(OtherUtil.tagKeyStreamCodec(Registries.BLOCK))
		}

	val IS_ANCHORED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("is_anchored") {
			it
				.persistent(Unit.CODEC)
				.networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
		}

	val SIMPLE_FLUID_CONTENT: DeferredHolder<DataComponentType<*>, DataComponentType<SimpleFluidContent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("simple_fluid_content") {
			it
				.persistent(SimpleFluidContent.CODEC)
				.networkSynchronized(SimpleFluidContent.STREAM_CODEC)
		}

	val BLOCK: DeferredHolder<DataComponentType<*>, DataComponentType<Block>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("block") {
			it
				.persistent(BuiltInRegistries.BLOCK.byNameCodec())
				.networkSynchronized(ByteBufCodecs.registry(Registries.BLOCK))
		}

	val ITEM_FILTER_ENTRIES: DeferredHolder<DataComponentType<*>, DataComponentType<ItemFilterDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("item_filter") {
			it
				.persistent(ItemFilterDataComponent.CODEC)
				.networkSynchronized(ItemFilterDataComponent.STREAM_CODEC)
		}

	val WEATHER: DeferredHolder<DataComponentType<*>, DataComponentType<WeatherEggItem.Weather>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("weather") {
			val codec = StringRepresentable.fromEnum(WeatherEggItem.Weather::values)
			it
				.persistent(codec)
				.networkSynchronized(ByteBufCodecs.fromCodec(codec))
		}

	val FLOO_POWDER: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("floo_powder") {
			it
				.persistent(Codec.INT)
				.networkSynchronized(ByteBufCodecs.VAR_INT)
		}

	val PORTKEY_DISGUISE: DeferredHolder<DataComponentType<*>, DataComponentType<PortkeyDisguiseDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("portkey_disguise") {
			it
				.persistent(PortkeyDisguiseDataComponent.CODEC)
				.networkSynchronized(PortkeyDisguiseDataComponent.STREAM_CODEC)
		}

	val ENDER_LETTER_CONTENTS: DeferredHolder<DataComponentType<*>, DataComponentType<EnderLetterContentsDataComponent>> =
		DATA_COMPONENT_REGISTRY.registerComponentType("ender_letter_contents") {
			it
				.persistent(EnderLetterContentsDataComponent.CODEC)
				.networkSynchronized(EnderLetterContentsDataComponent.STREAM_CODEC)
		}

}