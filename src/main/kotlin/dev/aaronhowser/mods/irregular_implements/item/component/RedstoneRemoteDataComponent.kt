package dev.aaronhowser.mods.irregular_implements.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.irregular_implements.registry.ModDataComponents
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponentType
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler

data class RedstoneRemoteDataComponent(
	private val stacks: NonNullList<ItemStack>
) : ItemInventoryItemHandler.InventoryDataComponent {

	constructor() : this(NonNullList.withSize(18, ItemStack.EMPTY))

	override fun getType(): DataComponentType<RedstoneRemoteDataComponent> = ModDataComponents.REDSTONE_REMOTE.get()

	override fun getInventory(): NonNullList<ItemStack> = stacks
	override fun setInventory(stack: ItemStack, inventory: NonNullList<ItemStack>) {
		stack.set(getType(), RedstoneRemoteDataComponent(inventory))
	}

	fun getLocation(index: Int): ItemStack = stacks.getOrNull(index) ?: ItemStack.EMPTY
	fun getDisplay(index: Int): ItemStack = stacks.getOrNull(index + 9) ?: ItemStack.EMPTY
	fun getPair(index: Int): Pair<ItemStack, ItemStack> = getLocation(index) to getDisplay(index)

	companion object {

		fun getCapability(stack: ItemStack, any: Any?): ItemStackHandler {
			return object : ItemInventoryItemHandler<RedstoneRemoteDataComponent>(stack, ModDataComponents.REDSTONE_REMOTE.get()) {
				override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
					if (slot < 9) return stack.has(ModDataComponents.LOCATION)
					return true
				}
			}
		}

		val CODEC: Codec<RedstoneRemoteDataComponent> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					NonNullList.codecOf(ItemStack.OPTIONAL_CODEC)
						.fieldOf("stacks")
						.forGetter(RedstoneRemoteDataComponent::stacks),
				).apply(instance, ::RedstoneRemoteDataComponent)
			}

		val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RedstoneRemoteDataComponent> =
			StreamCodec.composite(
				ByteBufCodecs.fromCodec(NonNullList.codecOf(ItemStack.OPTIONAL_CODEC)), RedstoneRemoteDataComponent::stacks,
				::RedstoneRemoteDataComponent
			)

	}

}