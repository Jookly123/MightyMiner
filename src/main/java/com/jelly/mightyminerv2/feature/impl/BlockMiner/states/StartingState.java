package com.jelly.mightyminerv2.feature.impl.BlockMiner.states;

import com.jelly.mightyminerv2.feature.impl.BlockMiner.BlockMiner;
import com.jelly.mightyminerv2.config.MightyMinerConfig;

/**
 * StartingState
 * <p>
 * Initial state in the BlockMiner state machine.
 * Acts as an entry point and determines the next appropriate state.
 */
public class StartingState implements BlockMinerState {

    @Override
    public void onStart(BlockMiner miner) {
        log("Entering Starting State");
    }

    @Override
    public BlockMinerState onTick(BlockMiner miner) {
        if (BlockMiner.getInstance().getPickaxeAbility() != BlockMiner.PickaxeAbility.NONE
                && miner.getPickaxeAbilityState() == BlockMiner.PickaxeAbilityState.AVAILABLE) {
            return new ApplyAbilityState();
        } else
            return new ChoosingBlockState();
    }

    @Override
    public void onEnd(BlockMiner miner) {
        log("Exiting Starting State");
    }
}
