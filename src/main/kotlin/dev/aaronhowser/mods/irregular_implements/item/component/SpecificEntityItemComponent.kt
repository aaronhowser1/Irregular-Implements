package dev.aaronhowser.mods.irregular_implements.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs
import java.util.*

class SpecificEntityItemComponent(
    val uuid: UUID,
    val name: Component
) {

    companion object {

        val UUID_CODEC: Codec<UUID> = Codec.STRING.xmap(
            { UUID.fromString(it) },
            { it.toString() }
        )

        val UUID_STREAM_CODEC: StreamCodec<ByteBuf, UUID> = ByteBufCodecs.STRING_UTF8.map(
            { UUID.fromString(it) },
            { it.toString() }
        )

        val CODEC: Codec<SpecificEntityItemComponent> = RecordCodecBuilder.create { instance ->
            instance.group(
                NeoForgeExtraCodecs
                    .aliasedFieldOf(UUID_CODEC, "uuid")
                    .forGetter(SpecificEntityItemComponent::uuid),
                NeoForgeExtraCodecs
                    .aliasedFieldOf(ComponentSerialization.CODEC, "name")
                    .forGetter(SpecificEntityItemComponent::name)
            ).apply(instance, ::SpecificEntityItemComponent)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SpecificEntityItemComponent> = StreamCodec.composite(
            UUID_STREAM_CODEC, SpecificEntityItemComponent::uuid,
            ComponentSerialization.STREAM_CODEC, SpecificEntityItemComponent::name,
            ::SpecificEntityItemComponent
        )

    }

}