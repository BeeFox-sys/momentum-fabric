package xyz.beefox.momentum.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import xyz.beefox.momentum.Momentum;
import xyz.beefox.momentum.MomentumPlayerData;

@Mixin (AbstractBlock.class)
public class MomentumDeltaMixin {
    @Inject (at = @At("TAIL"), method = "calcBlockBreakingDelta", cancellable = true)
    public void calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos, CallbackInfoReturnable info) {
        if (!player.isSpectator() && !player.isCreative() && !player.getMainHandStack().isEmpty() && EnchantmentHelper.get(player.getMainHandStack()).containsKey(Momentum.MOMENTUM) && player.getMainHandStack().isSuitableFor(state)) 
        {
            Optional<BlockState> last = ((MomentumPlayerData) player).momentum_getLastBlockBroken();
            if(last.isPresent()){
                int harvest = player.canHarvest(state) ? 30 : 100;
                float hardness = state.getHardness(world, pos);
                float initalSpeed = player.getBlockBreakingSpeed(state) / hardness / (float) harvest;
                float speedFactor = initalSpeed;
                if(state == last.get()){
                    
                    //Set Up Vars
                    speedFactor = initalSpeed * (float) Math.pow(Math.pow(2, -1.0 / 16 * hardness + 3.0 / 16) + 1, ((MomentumPlayerData) player).momentum_getBlockComboCount()+1);

                    if (((MomentumPlayerData) player).momentum_getBlockComboCount() + 1 >= 8 * Math.sqrt(hardness)){
                        speedFactor = initalSpeed *  Math.min(22 * hardness / player.getBlockBreakingSpeed(state), speedFactor);
                    }
                    info.setReturnValue(speedFactor);
                } 
            }
        } 
    }
}
