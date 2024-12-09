package dev.aaronhowser.mods.irregular_implements.item

import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider
import dev.aaronhowser.mods.irregular_implements.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.irregular_implements.datagen.tag.ModFluidTagsProvider
import dev.aaronhowser.mods.irregular_implements.registry.ModArmorMaterials
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil.isTrue
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.damagesource.FallLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent

object ModArmorItems {

    // These are lazy so they don't freak out due
    // to data components not being available yet when
    // BlockStateBaseMixin calls FluidWalkingBoots#checkCollisionShape

    val WATER_WALKING_BOOTS by lazy {
        ArmorItem(
            ModArmorMaterials.WATER_WALKING,
            ArmorItem.Type.BOOTS,
            Item.Properties()
                .durability(ArmorItem.Type.BOOTS.getDurability(15))
                .rarity(Rarity.RARE)
                .component(
                    ModDataComponents.FLUID_TAGS,
                    listOf(ModFluidTagsProvider.ALLOWS_WATER_WALKING)
                )
        )
    }

    val OBSIDIAN_WATER_WALKING_BOOTS by lazy {
        ArmorItem(
            ModArmorMaterials.OBSIDIAN_WATER_WALKING,
            ArmorItem.Type.BOOTS,
            Item.Properties()
                .durability(ArmorItem.Type.BOOTS.getDurability(15))
                .rarity(Rarity.RARE)
                .component(
                    ModDataComponents.FLUID_TAGS,
                    listOf(ModFluidTagsProvider.ALLOWS_WATER_WALKING)
                )
        )
    }

    val LAVA_WADERS by lazy {
        ArmorItem(
            ModArmorMaterials.LAVA_WADERS,
            ArmorItem.Type.BOOTS,
            Item.Properties()
                .durability(ArmorItem.Type.BOOTS.getDurability(15))
                .rarity(Rarity.RARE)
                .component(
                    ModDataComponents.FLUID_TAGS,
                    listOf(ModFluidTagsProvider.ALLOWS_LAVA_WALKING)
                )
        )
    }

    val MAGIC_HOOD by lazy {
        ArmorItem(
            ModArmorMaterials.MAGIC,
            ArmorItem.Type.HELMET,
            Item.Properties()
                .durability(ArmorItem.Type.HELMET.getDurability(15))
                .rarity(Rarity.RARE)
        )
    }

    fun shouldEntityStandOnFluid(livingEntity: LivingEntity, fluidState: FluidState): Boolean {
        if (livingEntity.isCrouching || livingEntity.isUnderWater) return false

        val footArmor = livingEntity.getItemBySlot(EquipmentSlot.FEET)
        val fluidTags = footArmor.get(ModDataComponents.FLUID_TAGS) ?: return false

        return fluidTags.any { fluidState.`is`(it) }
    }

    @JvmStatic
    fun checkCollisionShape(
        level: BlockGetter,
        pos: BlockPos,
        context: CollisionContext,
        original: VoxelShape?
    ): VoxelShape? {
        if (context !is EntityCollisionContext) return null

        val fluidState = level.getFluidState(pos)
        val fluidHeight = fluidState.getHeight(level, pos)
        if (fluidHeight <= 0) return null

        val entity = context.entity as? LivingEntity ?: return null
        if (!shouldEntityStandOnFluid(entity, fluidState)) return null

        val shape = FLUID_SHAPES.computeIfAbsent(fluidHeight) {
            Block.box(0.0, 0.0, 0.0, 16.0, (it * 16).toDouble(), 16.0)
        }

        if (!context.isAbove(shape, pos, true)) return null

        return if (original == null) shape else Shapes.or(original, shape)
    }

    private val FLUID_SHAPES: MutableMap<Float, VoxelShape> = HashMap()

    fun tooltip(event: ItemTooltipEvent) {
        if (event.itemStack.has(ModDataComponents.LUBRICATED)) {
            event.toolTip.add(
                ModLanguageProvider.Tooltips.LUBRICATED
                    .toComponent()
                    .withColor(0xFCF4DD)
            )
        }
    }

    @JvmStatic
    val FLUID_BOOT_FALL = FallLocation("fluid_boot_fall")

    @JvmStatic
    fun fluidWalkingFallLocation(entity: LivingEntity): FallLocation? {
        val fluidBelow = entity.level().getFluidState(entity.blockPosition())
        if (!shouldEntityStandOnFluid(entity, fluidBelow)) return null

        return FLUID_BOOT_FALL
    }

    @JvmStatic
    fun fluidWalkingDeathMessage(entity: LivingEntity): Component {
        val fluidBelow = entity.level().getFluidState(entity.blockPosition())
        val bootArmor = entity.getItemBySlot(EquipmentSlot.FEET)

        val bootWasResponsible = bootArmor.get(ModDataComponents.FLUID_TAGS)?.any { fluidBelow.`is`(it) }.isTrue
        return if (bootWasResponsible) ModLanguageProvider.Messages.FLUID_FALL_DEATH_BOOT.toComponent(
            entity.displayName ?: entity.name,
            fluidBelow.fluidType.description,
            bootArmor.displayName
        ) else ModLanguageProvider.Messages.FLUID_FALL_DEATH_GENERIC.toComponent(
            entity.displayName ?: entity.name,
            fluidBelow.fluidType.description,
        )
    }

}