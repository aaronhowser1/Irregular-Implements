package dev.aaronhowser.mods.irregular_implements.event

import com.mojang.blaze3d.systems.RenderSystem
import dev.aaronhowser.mods.irregular_implements.IrregularImplements
import dev.aaronhowser.mods.irregular_implements.block.block_entity.BiomeSensorBlockEntity
import dev.aaronhowser.mods.irregular_implements.client.SpectreSpecialEffects
import dev.aaronhowser.mods.irregular_implements.client.render.DiviningRodRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.LavaProtectionOverlayRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.RedstoneToolRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.TargetPositionRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.block.CustomCraftingTableBlockEntityRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.block.DiaphanousBlockEntityRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.block.PlayerInterfaceBlockEntityRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.block.SpectreEnergyInjectorBlockEntityRenderer
import dev.aaronhowser.mods.irregular_implements.client.render.entity.*
import dev.aaronhowser.mods.irregular_implements.client.render.item.CustomCraftingTableBEWLR
import dev.aaronhowser.mods.irregular_implements.client.render.item.DiaphanousBEWLR
import dev.aaronhowser.mods.irregular_implements.client.render.item.SpectreIlluminatorBEWLR
import dev.aaronhowser.mods.irregular_implements.datagen.datapack.ModDimensions
import dev.aaronhowser.mods.irregular_implements.item.*
import dev.aaronhowser.mods.irregular_implements.particle.CubeParticle
import dev.aaronhowser.mods.irregular_implements.particle.FlooFlameParticle
import dev.aaronhowser.mods.irregular_implements.registry.*
import dev.aaronhowser.mods.irregular_implements.util.ClientUtil
import net.minecraft.client.color.item.ItemColor
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.BiomeColors
import net.minecraft.client.renderer.blockentity.ChestRenderer
import net.minecraft.client.renderer.entity.DisplayRenderer.BlockDisplayRenderer
import net.minecraft.client.renderer.entity.EntityRenderers
import net.minecraft.client.renderer.entity.NoopRenderer
import net.minecraft.client.renderer.entity.ThrownItemRenderer
import net.minecraft.client.renderer.item.ItemProperties
import net.minecraft.util.Mth
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GrassColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.neoforge.client.event.*
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

@EventBusSubscriber(
	modid = IrregularImplements.ID,
	value = [Dist.CLIENT]
)
object ClientEvents {

	private fun getItemColorFromDye(dyeColor: DyeColor): ItemColor {
		return ItemColor { _, _ -> dyeColor.textureDiffuseColor }
	}

	private fun getLocalFoliageColor(stack: ItemStack, int: Int): Int {
		val localPlayer = ClientUtil.localPlayer
		val localFoliageColor = localPlayer?.level()
			?.getBiome(localPlayer.blockPosition())
			?.value()
			?.foliageColor

		return localFoliageColor ?: GrassColor.getDefaultColor()
	}

	@SubscribeEvent
	fun registerItemColors(event: RegisterColorHandlersEvent.Item) {
		for (dyeColor in DyeColor.entries) {
			val seedItem = GrassSeedItem.getFromColor(dyeColor).get()
			val coloredGrassBlock = ModBlocks.getColoredGrass(dyeColor).get()

			val colorFunction = getItemColorFromDye(dyeColor)

			event.register(colorFunction, seedItem)
			event.register(colorFunction, coloredGrassBlock)
		}

		event.register(getItemColorFromDye(DyeColor.LIME), ModItems.GRASS_SEEDS)

		event.register(
			::getLocalFoliageColor,
			ModBlocks.BIOME_GLASS.get(),
			ModBlocks.BIOME_STONE.get(),
			ModBlocks.BIOME_COBBLESTONE.get(),
			ModBlocks.BIOME_STONE_BRICKS.get(),
			ModBlocks.BIOME_STONE_BRICKS_CHISELED.get(),
			ModBlocks.BIOME_STONE_BRICKS_CRACKED.get()
		)

		event.register(
			BiomeCapsuleItem::getItemColor,
			ModItems.BIOME_CAPSULE.get()
		)

		event.register(
			DiviningRodItem::getItemColor,
			ModItems.DIVINING_ROD.get()
		)

		event.register(
			BiomeCrystalItem::getItemColor,
			ModItems.BIOME_CRYSTAL.get()
		)

	}

	//TODO: Make Rainbow Lamp use this instead of unique textures

	@SubscribeEvent
	fun registerBlockColors(event: RegisterColorHandlersEvent.Block) {
		for (dyeColor in DyeColor.entries) {
			val coloredGrassBlock = ModBlocks.getColoredGrass(dyeColor).get()

			event.register(
				{ _, _, _, tintIndex ->
					if (tintIndex == 0) dyeColor.textureDiffuseColor else 0xFFFFFF
				},
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

		ItemProperties.register(
			ModItems.REDSTONE_ACTIVATOR.get(),
			RedstoneActivatorItem.DURATION,
			RedstoneActivatorItem::getDurationFloat
		)

		for (item in listOf(
			ModItems.SPECTRE_CHARGER_BASIC.get(),
			ModItems.SPECTRE_CHARGER_REDSTONE.get(),
			ModItems.SPECTRE_CHARGER_ENDER.get(),
			ModItems.SPECTRE_CHARGER_GENESIS.get()
		)) {
			ItemProperties.register(
				item,
				SpectreChargerItem.IS_ENABLED,
				SpectreChargerItem::getEnabledForPredicate
			)
		}

		ItemProperties.register(
			ModItems.WEATHER_EGG.get(),
			WeatherEggItem.WEATHER,
			WeatherEggItem::getWeatherFloat
		)
	}

	@SubscribeEvent
	fun onClientSetup(event: FMLClientSetupEvent) {
		EntityRenderers.register(ModEntityTypes.INDICATOR_DISPLAY.get(), ::BlockDisplayRenderer)
		EntityRenderers.register(ModEntityTypes.SPECTRE_ILLUMINATOR.get(), ::SpectreIlluminatorEntityRenderer)
		EntityRenderers.register(ModEntityTypes.PORTKEY_ITEM.get(), ::PortkeyItemEntityRenderer)
		EntityRenderers.register(ModEntityTypes.TEMPORARY_FLOO_FIREPLACE.get(), ::NoopRenderer)
	}

	@SubscribeEvent
	fun registerGuiLayers(event: RegisterGuiLayersEvent) {
		event.registerAbove(
			VanillaGuiLayers.AIR_LEVEL,
			LavaProtectionOverlayRenderer.LAYER_NAME,
			LavaProtectionOverlayRenderer::tryRender
		)

		event.registerAbove(
			VanillaGuiLayers.CROSSHAIR,
			RedstoneToolRenderer.LAYER_NAME,
			RedstoneToolRenderer::tryRenderWireStrength
		)

		event.registerAbove(
			VanillaGuiLayers.CROSSHAIR,
			BiomeSensorBlockEntity.LAYER_NAME,
			BiomeSensorBlockEntity::tryRenderBiomeName
		)
	}

	@SubscribeEvent
	fun registerEntityRenderer(event: EntityRenderersEvent.RegisterRenderers) {
		event.registerBlockEntityRenderer(ModBlockEntities.DIAPHANOUS_BLOCK.get(), ::DiaphanousBlockEntityRenderer)
		event.registerBlockEntityRenderer(ModBlockEntities.SPECTRE_ENERGY_INJECTOR.get(), ::SpectreEnergyInjectorBlockEntityRenderer)
		event.registerBlockEntityRenderer(ModBlockEntities.CUSTOM_CRAFTING_TABLE.get(), ::CustomCraftingTableBlockEntityRenderer)
		event.registerBlockEntityRenderer(ModBlockEntities.NATURE_CHEST.get(), ::ChestRenderer)
		event.registerBlockEntityRenderer(ModBlockEntities.WATER_CHEST.get(), ::ChestRenderer)
		event.registerBlockEntityRenderer(ModBlockEntities.PLAYER_INTERFACE.get(), ::PlayerInterfaceBlockEntityRenderer)

		event.registerEntityRenderer(ModEntityTypes.GOLDEN_EGG.get(), ::ThrownItemRenderer)
		event.registerEntityRenderer(ModEntityTypes.WEATHER_EGG.get(), ::ThrownItemRenderer)
		event.registerEntityRenderer(ModEntityTypes.ARTIFICIAL_END_PORTAL.get(), ::ArtificialEndPortalRenderer)
		event.registerEntityRenderer(ModEntityTypes.GOLDEN_CHICKEN.get(), ::GoldenChickenRenderer)
		event.registerEntityRenderer(ModEntityTypes.WEATHER_CLOUD.get(), ::WeatherCloudRenderer)
		event.registerEntityRenderer(ModEntityTypes.SPIRIT.get(), ::SpiritEntityRenderer)
	}

	@SubscribeEvent
	fun registerClientExtensions(event: RegisterClientExtensionsEvent) {
		event.registerItem(
			DiaphanousBEWLR.clientItemExtensions,
			ModItems.DIAPHANOUS_BLOCK.get()
		)

		event.registerItem(
			CustomCraftingTableBEWLR.clientItemExtensions,
			ModItems.CUSTOM_CRAFTING_TABLE.get()
		)

		event.registerItem(
			SpectreIlluminatorBEWLR.clientItemExtensions,
			ModItems.SPECTRE_ILLUMINATOR.get()
		)
	}

	@SubscribeEvent
	fun onRegisterMenuScreens(event: RegisterMenuScreensEvent) {
		ModMenuTypes.registerScreens(event)
	}

	@SubscribeEvent
	fun tooltipEvent(event: ItemTooltipEvent) {
		ModArmorItems.tooltip(event)
		SpectreAnchorItem.tooltip(event)
	}

	private var changedAlpha = false

	@SubscribeEvent
	fun beforeRenderLiving(event: RenderLivingEvent.Pre<LivingEntity, HumanoidModel<LivingEntity>>) {
		val entity = event.entity

		val wearingFullSpectreArmor = entity.getItemBySlot(EquipmentSlot.HEAD).`is`(ModItems.SPECTRE_HELMET.get())
				&& entity.getItemBySlot(EquipmentSlot.CHEST).`is`(ModItems.SPECTRE_CHESTPLATE.get())
				&& entity.getItemBySlot(EquipmentSlot.LEGS).`is`(ModItems.SPECTRE_LEGGINGS.get())
				&& entity.getItemBySlot(EquipmentSlot.FEET).`is`(ModItems.SPECTRE_BOOTS.get())

		if (wearingFullSpectreArmor) {
			val alpha = Mth.sin(entity.tickCount.toFloat() * 0.025f) * 0.1f + 0.5f

			RenderSystem.enableBlend()
			RenderSystem.defaultBlendFunc()
			RenderSystem.setShaderColor(1f, 1f, 1f, alpha)

			changedAlpha = true
		}
	}

	@SubscribeEvent
	fun afterRenderLiving(event: RenderLivingEvent.Post<LivingEntity, HumanoidModel<LivingEntity>>) {
		if (changedAlpha) {
			RenderSystem.disableBlend()
			RenderSystem.setShaderColor(1f, 1f, 1f, 1f)

			changedAlpha = false
		}
	}

	@SubscribeEvent
	fun registerParticleProviders(event: RegisterParticleProvidersEvent) {
		event.registerSpriteSet(ModParticleTypes.FLOO_FLAME.get(), FlooFlameParticle::Provider)
		event.registerSpecial(ModParticleTypes.CUBE.get(), CubeParticle.Provider)
	}

	@SubscribeEvent
	fun registerDimensionSpecialEffects(event: RegisterDimensionSpecialEffectsEvent) {
		event.register(ModDimensions.SPECTRE_RL, SpectreSpecialEffects())
	}

	@SubscribeEvent
	fun afterClientTick(event: ClientTickEvent.Post) {
		DiviningRodRenderer.afterClientTick(event)
		RedstoneToolRenderer.afterClientTick(event)
		TargetPositionRenderer.afterClientTick(event)
	}

	@SubscribeEvent
	fun onRenderLevel(event: RenderLevelStageEvent) {
		DiviningRodRenderer.onRenderLevel(event)
		RedstoneToolRenderer.onRenderLevel(event)
		TargetPositionRenderer.onRenderLevel(event)
	}

}