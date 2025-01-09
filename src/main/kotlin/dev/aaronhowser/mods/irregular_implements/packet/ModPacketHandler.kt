package dev.aaronhowser.mods.irregular_implements.packet

import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientChangedChatDetector
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedBlockDestabilizerButton
import dev.aaronhowser.mods.irregular_implements.packet.client_to_server.ClientClickedIronDropperButton
import dev.aaronhowser.mods.irregular_implements.packet.server_to_client.UpdateClientBlockDestabilizer
import dev.aaronhowser.mods.irregular_implements.packet.server_to_client.UpdateClientChatDetector
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler
import net.neoforged.neoforge.network.registration.PayloadRegistrar

object ModPacketHandler {

    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar("1")

        toServer(
            registrar,
            ClientChangedChatDetector.TYPE,
            ClientChangedChatDetector.STREAM_CODEC
        )

        toClient(
            registrar,
            UpdateClientChatDetector.TYPE,
            UpdateClientChatDetector.STREAM_CODEC
        )

        toServer(
            registrar,
            ClientClickedBlockDestabilizerButton.TYPE,
            ClientClickedBlockDestabilizerButton.STREAM_CODEC
        )

        toClient(
            registrar,
            UpdateClientBlockDestabilizer.TYPE,
            UpdateClientBlockDestabilizer.STREAM_CODEC
        )

        toServer(
            registrar,
            ClientClickedIronDropperButton.TYPE,
            ClientClickedIronDropperButton.STREAM_CODEC
        )

    }

    fun messageNearbyPlayers(packet: IModPacket, serverLevel: ServerLevel, origin: Vec3, radius: Double) {
        for (player in serverLevel.players()) {
            val distance = player.distanceToSqr(origin.x(), origin.y(), origin.z())
            if (distance < radius * radius) {
                messagePlayer(player, packet)
            }
        }
    }

    fun messagePlayer(player: ServerPlayer, packet: IModPacket) {
        PacketDistributor.sendToPlayer(player, packet)
    }

    fun messageAllPlayers(packet: IModPacket) {
        PacketDistributor.sendToAllPlayers(packet)
    }

    fun messageServer(packet: IModPacket) {
        PacketDistributor.sendToServer(packet)
    }

    private fun <T : IModPacket> toClient(
        registrar: PayloadRegistrar,
        packetType: CustomPacketPayload.Type<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>,
    ) {
        registrar.playToClient(
            packetType,
            streamCodec
        ) { packet, context -> packet.receiveOnClient(context) }
    }

    private fun <T : IModPacket> toServer(
        registrar: PayloadRegistrar,
        packetType: CustomPacketPayload.Type<T>,
        streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
    ) {
        registrar.playToServer(
            packetType,
            streamCodec
        ) { packet, context -> packet.receiveOnServer(context) }
    }

}