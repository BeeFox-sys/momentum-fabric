package xyz.beefox.momentum;

import java.util.Optional;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;


public class Momentum implements ModInitializer {
	public static Enchantment MOMENTUM = Registry.register(
		Registry.ENCHANTMENT,
		new Identifier("momentum", "momentum"),
		new MomentumEnchantment()
	);



	private static final Identifier ABANDONED_MINESHAFT_LOOT_ID = LootTables.ABANDONED_MINESHAFT_CHEST;
	private static final Identifier SIMPLE_DUNGON_LOOT_ID = LootTables.SIMPLE_DUNGEON_CHEST;


	@Override
    public void onInitialize() {

		//Block Broken
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (!player.isSpectator() && !player.isCreative() && !player.getMainHandStack().isEmpty() && EnchantmentHelper.get(player.getMainHandStack()).containsKey(Momentum.MOMENTUM) && player.getMainHandStack().isSuitableFor(state) && !world.isClient()) {
			Optional<BlockState> last = ((MomentumPlayerData) player).momentum_getLastBlockBroken();
			if(last.isPresent()){
				if(state == last.get()){
					((MomentumPlayerData) player).momentum_setBlockComboCount(((MomentumPlayerData) player).momentum_getBlockComboCount()+1);

				} else {
					((MomentumPlayerData) player).momentum_setBlockComboCount(0);

				}

			}
			((MomentumPlayerData) player).momentum_setLastBlockBroken(Optional.of(state));
			} else {
				((MomentumPlayerData) player).momentum_setBlockComboCount(0);
			}
		});

		// Loot Spawning
	

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, table, setter) -> {
			if (ABANDONED_MINESHAFT_LOOT_ID.equals(id) || SIMPLE_DUNGON_LOOT_ID.equals(id)) {
				FabricLootPoolBuilder poolBuilder = FabricLootPoolBuilder.builder()
					.withCondition(RandomChanceLootCondition.builder(0.25f).build())
					.with(ItemEntry.builder(Items.ENCHANTED_BOOK).weight(1))
					.withFunction(new EnchantRandomlyLootFunction.Builder().add(MOMENTUM).build());
					table.pool(poolBuilder);
			}
		});

    }


	
}
