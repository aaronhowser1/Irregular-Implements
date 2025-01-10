package dev.aaronhowser.mods.irregular_implements.packet.server_to_client

import dev.aaronhowser.mods.irregular_implements.packet.IModPacket
import dev.aaronhowser.mods.irregular_implements.util.OtherUtil
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateClientChatDetector(
    val regexString: String
) : IModPacket {

    override fun receiveOnClient(context: IPayloadContext) {
        context.enqueueWork {
        }
    }

    override fun type(): CustomPacketPayload.Type<UpdateClientChatDetector> {
        return TYPE
    }

    companion object {

        val TYPE: CustomPacketPayload.Type<UpdateClientChatDetector> =
            CustomPacketPayload.Type(OtherUtil.modResource("update_client_chat_detector"))

        val STREAM_CODEC: StreamCodec<ByteBuf, UpdateClientChatDetector> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, UpdateClientChatDetector::regexString,
                ::UpdateClientChatDetector
            )
    }

}