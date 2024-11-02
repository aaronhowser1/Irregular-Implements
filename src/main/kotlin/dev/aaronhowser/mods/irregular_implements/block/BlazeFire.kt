package dev.aaronhowser.mods.irregular_implements.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FireBlock
import net.minecraft.world.level.block.state.BlockState

class BlazeFire : FireBlock(
    Properties
        .ofFullCopy(Blocks.FIRE)
) {



    override fun getIgniteOdds(state: BlockState): Int {
        return super.getIgniteOdds(state) * 3
    }

    override fun getBurnOdds(state: BlockState): Int {
        return super.getBurnOdds(state) * 3
    }

    public override fun getStateForPlacement(level: BlockGetter, pos: BlockPos): BlockState {
        return super.getStateForPlacement(level, pos)
    }

}