package dev.aaronhowser.mods.irregular_implements.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.aaronhowser.mods.irregular_implements.block.block_entity.SpectreEnergyInjectorBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.util.RandomSource
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.sqrt

class SpectreEnergyInjectorBlockEntityRenderer(
    val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<SpectreEnergyInjectorBlockEntity> {

    companion object {
        private val HALF_SQRT_3: Float = (sqrt(3.0) / 2.0).toFloat()

        fun renderRays(
            poseStack: PoseStack,
            time: Float,
            vertexConsumer: VertexConsumer,
            centerColor: Int = 0xFF000000.toInt(),
            outerColor: Int = 0x002C6A70,
            amountRays: Int = 15,
            rayLength: Float = 0.325f,
            rayWidth: Float = 0.15f
        ) {
            poseStack.pushPose()

            val randomSource = RandomSource.create(432L)
            val vec0 = Vector3f()
            val vec1 = Vector3f()
            val vec2 = Vector3f()
            val vec3 = Vector3f()
            val quaternionf = Quaternionf()

            for (rayIndex in 0 until amountRays) {
                quaternionf
                    .rotateXYZ(
                        randomSource.nextFloat() * (Math.PI * 2).toFloat(),
                        randomSource.nextFloat() * (Math.PI * 2).toFloat(),
                        randomSource.nextFloat() * (Math.PI * 2).toFloat()
                    )
                    .rotateXYZ(
                        randomSource.nextFloat() * (Math.PI * 2).toFloat(),
                        randomSource.nextFloat() * (Math.PI * 2).toFloat(),
                        randomSource.nextFloat() * (Math.PI * 2).toFloat() + time * (Math.PI / 2).toFloat()
                    )

                poseStack.mulPose(quaternionf)

                vec1.set(-HALF_SQRT_3 * rayWidth, rayLength, -0.5F * rayWidth)
                vec2.set(HALF_SQRT_3 * rayWidth, rayLength, -0.5F * rayWidth)
                vec3.set(0.0F, rayLength, rayWidth)

                val pose = poseStack.last()

                vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
                vertexConsumer.addVertex(pose, vec1).setColor(outerColor)
                vertexConsumer.addVertex(pose, vec2).setColor(outerColor)

                vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
                vertexConsumer.addVertex(pose, vec2).setColor(outerColor)
                vertexConsumer.addVertex(pose, vec3).setColor(outerColor)

                vertexConsumer.addVertex(pose, vec0).setColor(centerColor)
                vertexConsumer.addVertex(pose, vec3).setColor(outerColor)
                vertexConsumer.addVertex(pose, vec1).setColor(outerColor)
            }

            poseStack.popPose()
        }
    }

    override fun render(
        blockEntity: SpectreEnergyInjectorBlockEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val time = ((blockEntity.level?.gameTime ?: 0) + partialTick) / 200f

        poseStack.pushPose()
        poseStack.translate(0.5f, 0.6f, 0.5f)

        renderRays(poseStack, time, bufferSource.getBuffer(RenderType.dragonRays()))
        renderRays(poseStack, time, bufferSource.getBuffer(RenderType.dragonRaysDepth()))

        poseStack.popPose()
    }

    override fun shouldRenderOffScreen(blockEntity: SpectreEnergyInjectorBlockEntity): Boolean {
        return true
    }

}