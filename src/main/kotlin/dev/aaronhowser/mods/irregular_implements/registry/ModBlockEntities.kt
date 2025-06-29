package dev.aaronhowser.mods.irregular_implements.registry

import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.block.SpecialChestBlock
import dev.aaronhowser.mods.irregular_implements.block.block_entity.*
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
object ModBlockEntities {

	val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
		DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, IrregularImplements.ID)

	val RAIN_SHIELD: DeferredHolder<BlockEntityType<*>, BlockEntityType<RainShieldBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("rain_shield", Supplier {
			BlockEntityType.Builder.of(
				::RainShieldBlockEntity,
				ModBlocks.RAIN_SHIELD.get()
			).build(null)
		})

	val REDSTONE_INTERFACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<RedstoneInterfaceBasicBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("redstone_interface", Supplier {
			BlockEntityType.Builder.of(
				::RedstoneInterfaceBasicBlockEntity,
				ModBlocks.BASIC_REDSTONE_INTERFACE.get()
			).build(null)
		})

	val REDSTONE_OBSERVER: DeferredHolder<BlockEntityType<*>, BlockEntityType<RedstoneObserverBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("redstone_observer", Supplier {
			BlockEntityType.Builder.of(
				::RedstoneObserverBlockEntity,
				ModBlocks.REDSTONE_OBSERVER.get()
			).build(null)
		})

	val BLOCK_DESTABILIZER: DeferredHolder<BlockEntityType<*>, BlockEntityType<BlockDestabilizerBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("block_destabilizer", Supplier {
			BlockEntityType.Builder.of(
				::BlockDestabilizerBlockEntity,
				ModBlocks.BLOCK_DESTABILIZER.get()
			).build(null)
		})

	val BLOCK_BREAKER: DeferredHolder<BlockEntityType<*>, BlockEntityType<BlockBreakerBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("block_breaker", Supplier {
			BlockEntityType.Builder.of(
				::BlockBreakerBlockEntity,
				ModBlocks.BLOCK_BREAKER.get()
			).build(null)
		})

	val SPECTRE_LENS: DeferredHolder<BlockEntityType<*>, BlockEntityType<SpectreLensBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("spectre_lens", Supplier {
			BlockEntityType.Builder.of(
				::SpectreLensBlockEntity,
				ModBlocks.SPECTRE_LENS.get()
			).build(null)
		})

	val SPECTRE_ENERGY_INJECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<SpectreEnergyInjectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("spectre_energy_injector", Supplier {
			BlockEntityType.Builder.of(
				::SpectreEnergyInjectorBlockEntity,
				ModBlocks.SPECTRE_ENERGY_INJECTOR.get()
			).build(null)
		})

	val SPECTRE_COIL: DeferredHolder<BlockEntityType<*>, BlockEntityType<SpectreCoilBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("spectre_coil", Supplier {
			BlockEntityType.Builder.of(
				::SpectreCoilBlockEntity,
				ModBlocks.SPECTRE_COIL_BASIC.get(),
				ModBlocks.SPECTRE_COIL_REDSTONE.get(),
				ModBlocks.SPECTRE_COIL_ENDER.get(),
				ModBlocks.SPECTRE_COIL_NUMBER.get(),
				ModBlocks.SPECTRE_COIL_GENESIS.get()
			).build(null)
		})

	val MOON_PHASE_DETECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<MoonPhaseDetectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("moon_phase_detector", Supplier {
			BlockEntityType.Builder.of(
				::MoonPhaseDetectorBlockEntity,
				ModBlocks.MOON_PHASE_DETECTOR.get()
			).build(null)
		})

	val CHAT_DETECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<ChatDetectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("chat_detector", Supplier {
			BlockEntityType.Builder.of(
				::ChatDetectorBlockEntity,
				ModBlocks.CHAT_DETECTOR.get()
			).build(null)
		})

	val GLOBAL_CHAT_DETECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<GlobalChatDetectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("global_chat_detector", Supplier {
			BlockEntityType.Builder.of(
				::GlobalChatDetectorBlockEntity,
				ModBlocks.GLOBAL_CHAT_DETECTOR.get()
			).build(null)
		})

	val ONLINE_DETECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<OnlineDetectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("online_detector", Supplier {
			BlockEntityType.Builder.of(
				::OnlineDetectorBlockEntity,
				ModBlocks.ONLINE_DETECTOR.get()
			).build(null)
		})

	val DIAPHANOUS_BLOCK: DeferredHolder<BlockEntityType<*>, BlockEntityType<DiaphanousBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("diaphanous_block", Supplier {
			BlockEntityType.Builder.of(
				::DiaphanousBlockEntity,
				ModBlocks.DIAPHANOUS_BLOCK.get()
			).build(null)
		})

	val IRON_DROPPER: DeferredHolder<BlockEntityType<*>, BlockEntityType<IronDropperBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("iron_dropper", Supplier {
			BlockEntityType.Builder.of(
				::IronDropperBlockEntity,
				ModBlocks.IRON_DROPPER.get()
			).build(null)
		})

	val CUSTOM_CRAFTING_TABLE: DeferredHolder<BlockEntityType<*>, BlockEntityType<CustomCraftingTableBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("custom_crafting_table", Supplier {
			BlockEntityType.Builder.of(
				::CustomCraftingTableBlockEntity,
				ModBlocks.CUSTOM_CRAFTING_TABLE.get()
			).build(null)
		})

	val IGNITER: DeferredHolder<BlockEntityType<*>, BlockEntityType<IgniterBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("igniter", Supplier {
			BlockEntityType.Builder.of(
				::IgniterBlockEntity,
				ModBlocks.IGNITER.get()
			).build(null)
		})

	val NOTIFICATION_INTERFACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<NotificationInterfaceBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("notification_interface", Supplier {
			BlockEntityType.Builder.of(
				::NotificationInterfaceBlockEntity,
				ModBlocks.NOTIFICATION_INTERFACE.get()
			).build(null)
		})

	val IMBUING_STATION: DeferredHolder<BlockEntityType<*>, BlockEntityType<ImbuingStationBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("imbuing_station", Supplier {
			BlockEntityType.Builder.of(
				::ImbuingStationBlockEntity,
				ModBlocks.IMBUING_STATION.get()
			).build(null)
		})

	val FILTERED_PLATFORM: DeferredHolder<BlockEntityType<*>, BlockEntityType<FilteredPlatformBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("filtered_platform", Supplier {
			BlockEntityType.Builder.of(
				::FilteredPlatformBlockEntity,
				ModBlocks.FILTERED_SUPER_LUBRICANT_PLATFORM.get()
			).build(null)
		})

	val INVENTORY_TESTER: DeferredHolder<BlockEntityType<*>, BlockEntityType<InventoryTesterBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("inventory_tester", Supplier {
			BlockEntityType.Builder.of(
				::InventoryTesterBlockEntity,
				ModBlocks.INVENTORY_TESTER.get()
			).build(null)
		})

	val ITEM_COLLECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<ItemCollectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("item_collector", Supplier {
			BlockEntityType.Builder.of(
				::ItemCollectorBlockEntity,
				ModBlocks.ITEM_COLLECTOR.get()
			).build(null)
		})

	val ADVANCED_ITEM_COLLECTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<AdvancedItemCollectorBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("advanced_item_collector", Supplier {
			BlockEntityType.Builder.of(
				::AdvancedItemCollectorBlockEntity,
				ModBlocks.ADVANCED_ITEM_COLLECTOR.get()
			).build(null)
		})

	@JvmField
	val NATURE_CHEST: DeferredHolder<BlockEntityType<*>, BlockEntityType<SpecialChestBlock.NatureBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("nature_chest", Supplier {
			BlockEntityType.Builder.of(
				{ pos, state -> SpecialChestBlock.NatureBlockEntity(pos, state) },
				ModBlocks.NATURE_CHEST.get()
			).build(null)
		})

	@JvmField
	val WATER_CHEST: DeferredHolder<BlockEntityType<*>, BlockEntityType<SpecialChestBlock.WaterBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("water_chest", Supplier {
			BlockEntityType.Builder.of(
				{ pos, state -> SpecialChestBlock.WaterBlockEntity(pos, state) },
				ModBlocks.WATER_CHEST.get()
			).build(null)
		})

	val PEACE_CANDLE: DeferredHolder<BlockEntityType<*>, BlockEntityType<PeaceCandleBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("peace_candle", Supplier {
			BlockEntityType.Builder.of(
				::PeaceCandleBlockEntity,
				ModBlocks.PEACE_CANDLE.get()
			).build(null)
		})

	val PLAYER_INTERFACE: DeferredHolder<BlockEntityType<*>, BlockEntityType<PlayerInterfaceBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("player_interface", Supplier {
			BlockEntityType.Builder.of(
				::PlayerInterfaceBlockEntity,
				ModBlocks.PLAYER_INTERFACE.get()
			).build(null)
		})

	val FLOO_BRICK: DeferredHolder<BlockEntityType<*>, BlockEntityType<FlooBrickBlockEntity>> =
		BLOCK_ENTITY_REGISTRY.register("floo_brick", Supplier {
			BlockEntityType.Builder.of(
				::FlooBrickBlockEntity,
				ModBlocks.FLOO_BRICK.get()
			).build(null)
		})

}