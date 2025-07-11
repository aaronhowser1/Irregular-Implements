package dev.aaronhowser.mods.irregular_implements.handler.floo

import dev.aaronhowser.mods.irregular_implements.block.block_entity.FlooBrickBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import org.jline.utils.Levenshtein
import java.util.*

class FlooNetworkSavedData : SavedData() {

	private val fireplaces: MutableList<FlooFireplace> = mutableListOf()
	fun getFireplaces(): List<FlooFireplace> = fireplaces.toList()

	fun addFireplace(fireplace: FlooFireplace) {
		fireplaces.add(fireplace)
		setDirty()
	}

	fun addFireplace(masterUuid: UUID, name: String?, blockPos: BlockPos) {
		addFireplace(FlooFireplace(masterUuid, name, blockPos))
	}

	fun createFireplace(
		masterUuid: UUID,
		name: String?,
		blockPos: BlockPos,
		connectedBricks: List<BlockPos>
	): Boolean {
		for (fireplace in fireplaces) {
			if (connectedBricks.contains(fireplace.masterBlockPos)) return false

			val theirName = fireplace.name
			if (theirName != null && theirName.lowercase() == name?.lowercase()) return false
		}

		addFireplace(masterUuid, name, blockPos)

		return true
	}

	fun findFireplace(name: String): FlooFireplace? {
		val fireplacesWithNames = fireplaces.filter { !it.name.isNullOrBlank() }

		val maxDistance = 5

		return fireplacesWithNames.map { it to Levenshtein.distance(it.name!!.lowercase(), name.lowercase()) }
			.filter { it.second <= maxDistance }
			.minByOrNull { it.second }
			?.first
	}

	fun findFireplace(flooBrickBlockEntity: FlooBrickBlockEntity): FlooFireplace? {
		return fireplaces.firstOrNull { it.masterUuid == flooBrickBlockEntity.uuid || it.masterUuid == flooBrickBlockEntity.masterUUID }
	}

	fun findFireplace(uuid: UUID): FlooFireplace? {
		return fireplaces.firstOrNull { it.masterUuid == uuid }
	}

	fun removeFireplace(uuid: UUID) {
		fireplaces.removeIf { it.masterUuid == uuid }
		setDirty()
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val fireplaceList = tag.getList(NBT_FIREPLACES, Tag.TAG_LIST.toInt())

		for (fireplace in fireplaces) {
			fireplaceList.add(fireplace.toTag())
		}

		tag.put(NBT_FIREPLACES, fireplaceList)

		return tag
	}

	companion object {
		const val NBT_FIREPLACES = "Fireplaces"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): FlooNetworkSavedData {
			val data = FlooNetworkSavedData()

			val fireplaceList = tag.getList(NBT_FIREPLACES, Tag.TAG_COMPOUND.toInt())
			for (i in fireplaceList.indices) {
				val fireplaceTag = fireplaceList.getCompound(i)
				data.fireplaces.add(FlooFireplace.fromTag(fireplaceTag))
			}

			return data
		}

		fun get(level: ServerLevel): FlooNetworkSavedData {
			return level.dataStorage.computeIfAbsent(
				Factory(::FlooNetworkSavedData, ::load),
				"floo_network"
			)
		}

	}

}