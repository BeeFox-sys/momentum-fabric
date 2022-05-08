package xyz.beefox.momentum.mixins;

import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import xyz.beefox.momentum.Momentum;
import xyz.beefox.momentum.MomentumPlayerData;

@Mixin(PlayerEntity.class)
abstract class MomentumDataMixin extends LivingEntity implements MomentumPlayerData{


    private static final TrackedData<Optional<BlockState>> momentum_lastBlockBroken = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE); 
    private static final TrackedData<Integer> momentum_blockComboCount = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected MomentumDataMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject (at = @At("Tail"), method="initDataTracker()V")
    private void initDataTracker(CallbackInfo info){
        this.dataTracker.startTracking(momentum_lastBlockBroken, Optional.empty());
        this.dataTracker.startTracking(momentum_blockComboCount, 0);
    }

    @Inject (at = @At("Tail"), method="tick()V")
    private void tick(CallbackInfo info){
        if(!world.isClient()){
        if(!EnchantmentHelper.get(((PlayerEntity)(Object) this).getMainHandStack()).containsKey(Momentum.MOMENTUM)){
                if(((MomentumPlayerData) this).momentum_getBlockComboCount() > 0){
                    ((MomentumPlayerData) this).momentum_setBlockComboCount(0);
                    this.playSound(SoundEvents.ITEM_AXE_WAX_OFF, 1f, 2f);
                }
            }
        }
    }

    @Override
    public Optional<BlockState> momentum_getLastBlockBroken () {
        return this.dataTracker.get(momentum_lastBlockBroken);
    }
    @Override
    public void momentum_setLastBlockBroken (Optional<BlockState> state){
        this.dataTracker.set(momentum_lastBlockBroken, state);
    }    
    @Override
    public int momentum_getBlockComboCount() {
        return this.dataTracker.get(momentum_blockComboCount);
    }
    @Override
    public void momentum_setBlockComboCount(int count){
        this.dataTracker.set(momentum_blockComboCount, count);
    }
    
}
