package com.duckblade.osrs.dpscalc.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemManager;

import static net.runelite.api.EquipmentInventorySlot.AMULET;
import static net.runelite.api.EquipmentInventorySlot.BODY;
import static net.runelite.api.EquipmentInventorySlot.HEAD;
import static net.runelite.api.EquipmentInventorySlot.LEGS;
import static net.runelite.api.ItemID.*;

public enum EquipmentFlags
{

	VOID_MAGE(new EquipmentInventorySlot[] {HEAD, BODY, LEGS}, new Integer[][] {{VOID_MAGE_HELM}, {VOID_KNIGHT_TOP, ELITE_VOID_TOP}, {VOID_KNIGHT_ROBE, ELITE_VOID_ROBE}}),
	VOID_MELEE(new EquipmentInventorySlot[] {HEAD, BODY, LEGS}, new Integer[][] {{VOID_MELEE_HELM}, {VOID_KNIGHT_TOP, ELITE_VOID_TOP}, {VOID_KNIGHT_ROBE, ELITE_VOID_ROBE}}),
	VOID_RANGED(new EquipmentInventorySlot[] {HEAD, BODY, LEGS}, new Integer[][] {{VOID_RANGER_HELM}, {VOID_KNIGHT_TOP, ELITE_VOID_TOP}, {VOID_KNIGHT_ROBE, ELITE_VOID_ROBE}}),
	VOID_ELITE(new EquipmentInventorySlot[] {HEAD, BODY, LEGS}, new Integer[][] {{VOID_MAGE_HELM, VOID_MELEE_HELM, VOID_RANGER_HELM}, {ELITE_VOID_TOP}, {ELITE_VOID_ROBE}}),
	SLAYER_MASK(new EquipmentInventorySlot[] {HEAD}, new Integer[][] {{BLACK_MASK, SLAYER_HELMET}}),
	SLAYER_MASK_I(new EquipmentInventorySlot[] {HEAD}, new Integer[][] {{BLACK_MASK_I, SLAYER_HELMET_I}}),
	SALVE(new EquipmentInventorySlot[] {AMULET}, new Integer[][] {{SALVE_AMULET, SALVE_AMULET_E, SALVE_AMULETI, SALVE_AMULETEI}}),
	SALVE_E(new EquipmentInventorySlot[] {AMULET}, new Integer[][] {{SALVE_AMULET_E, SALVE_AMULETEI}}),
	SALVE_I(new EquipmentInventorySlot[] {AMULET}, new Integer[][] {{SALVE_AMULETI, SALVE_AMULETEI}}),
	SALVE_EI(new EquipmentInventorySlot[] {AMULET}, new Integer[][] {{SALVE_AMULETEI}}),
	;

	private final BiFunction<Map<EquipmentInventorySlot, ItemStats>, ItemManager, Boolean> checker;

	private static boolean arrayContains(Integer[] arr, int value)
	{
		// java really is missing this in its stdlib huh
		for (int a : arr)
			if (a == value)
				return true;
		return false;
	}

	EquipmentFlags(EquipmentInventorySlot[] slots, Integer[][] items)
	{
		this((equipment, itemManager) ->
		{
			for (int i = 0; i < slots.length; i++)
			{
				ItemStats slotItem = equipment.get(slots[i]);
				if (slotItem == null)
					return false;
				
				int canonical = itemManager != null ? itemManager.canonicalize(slotItem.getItemId()) : slotItem.getItemId();
				if (!arrayContains(items[i], canonical))
					return false;
			}
			return true;
		});
	}

	EquipmentFlags(BiFunction<Map<EquipmentInventorySlot, ItemStats>, ItemManager, Boolean> checker)
	{
		this.checker = checker;
	}

	public static List<EquipmentFlags> fromMap(Map<EquipmentInventorySlot, ItemStats> equipment, ItemManager itemManager)
	{
		return Arrays.stream(values())
				.filter(b -> b.checker.apply(equipment, itemManager))
				.collect(Collectors.toList());
	}

}
