package dev.aaronhowser.mods.irregular_implements.client.render.block

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import dev.aaronhowser.mods.irregular_implements.block.block_entity.SpectreEnergyInjectorBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3
import kotlin.random.Random

class SpectreEnergyInjectorBlockEntityRenderer(
    val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<SpectreEnergyInjectorBlockEntity> {

    companion object {
        private val XP = Vec3(1.0, 0.0, 0.0)
        private val YP = Vec3(0.0, 1.0, 0.0)
        private val ZP = Vec3(0.0, 0.0, 1.0)

    }

    override fun render(
        blockEntity: SpectreEnergyInjectorBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val tesselator = Tesselator.getInstance()

        val animationCounter = ((blockEntity.level?.gameTime ?: 0) + partialTick) / 200f

        poseStack.pushPose()
        poseStack.translate(0.5, 0.6, 0.5)

        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.disableCull()
        RenderSystem.depthMask(false)

        for (i in 0 until 25) {
            poseStack.pushPose()

            val size1 = Random.nextFloat() * 0.2f + 0.1f
            val size2 = Random.nextFloat() * 0.1f

            val red = ((Mth.sin(animationCounter) / 2 + 0.5).toFloat() * 80 + 40).toInt()
            val green = 230
            val blue = 226
            val alpha = 255

            val bufferBuilder = tesselator.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR)

            bufferBuilder.addVertex(poseStack.last().pose(), 0.0f, 0.0f, 0.0f).setColor(0, 0, 0, 255)

            bufferBuilder.addVertex(poseStack.last().pose(), -0.866f * size2, size1, -0.5f * size2).setColor(red, green, blue, alpha)
            bufferBuilder.addVertex(poseStack.last().pose(), 0.866f * size2, size1, -0.5f * size2).setColor(red, green, blue, alpha)
            bufferBuilder.addVertex(poseStack.last().pose(), 0.0f, size1, size2).setColor(red, green, blue, alpha)
            bufferBuilder.addVertex(poseStack.last().pose(), -0.866f * size2, size1, -0.5f * size2).setColor(red, green, blue, alpha)

            poseStack.popPose()
        }

        RenderSystem.depthMask(true)
        RenderSystem.enableCull()
        RenderSystem.disableBlend()
        poseStack.popPose()
    }
}