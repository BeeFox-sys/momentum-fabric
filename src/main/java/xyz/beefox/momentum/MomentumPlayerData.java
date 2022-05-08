package xyz.beefox.momentum;

import java.util.Optional;

import net.minecraft.block.BlockState;

public interface MomentumPlayerData {

    Optional<BlockState> momentum_getLastBlockBroken ();
    
    void momentum_setLastBlockBroken (Optional<BlockState> state);

    int momentum_getBlockComboCount ();

    void momentum_setBlockComboCount (int count);
    
}
