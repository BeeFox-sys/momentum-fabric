package xyz.beefox.momentum;

import net.minecraft.enchantment.EfficiencyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class MomentumEnchantment extends Enchantment{
    public MomentumEnchantment(){
        super(
            Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, new EquipmentSlot[] {EquipmentSlot.MAINHAND}
        );
    }
    @Override
    public int getMinPower(int level) {
        return 1 + 10 * (level - 1);
    }
    @Override
    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }
    @Override
    public int getMaxLevel() {
        return 1;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.isOf(Items.SHEARS)) {
            return true;
        }
        return super.isAcceptableItem(stack);
    }
    @Override
    public boolean canAccept(Enchantment other) {
        if (other instanceof EfficiencyEnchantment) {
            return false;
        }
        return super.canAccept(other);
    }
    @Override
    public boolean isTreasure(){
        return true;
    }
}
