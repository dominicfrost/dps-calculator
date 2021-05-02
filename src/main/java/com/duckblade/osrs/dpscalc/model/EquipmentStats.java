package com.duckblade.osrs.dpscalc.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;

@Data
@Builder
public class EquipmentStats
{

	private final int accuracyStab;
	private final int accuracySlash;
	private final int accuracyCrush;
	private final int accuracyMagic;
	private final int accuracyRanged;

	private final int strengthMelee;
	private final int strengthRanged;
	private final int strengthMagic;

	private final int prayer;
	private final int speed;

	private static void addValues(EquipmentStatsBuilder builder, ItemStats i)
	{
		if (i == null)
			return;

		builder.accuracyStab += i.getEquipStats().getAccuracyStab();
		builder.accuracySlash += i.getEquipStats().getAccuracySlash();
		builder.accuracyCrush += i.getEquipStats().getAccuracyCrush();
		builder.accuracyMagic += i.getEquipStats().getAccuracyMagic();
		builder.accuracyRanged += i.getEquipStats().getAccuracyRanged();
		builder.strengthMelee += i.getEquipStats().getStrengthMelee();
		builder.strengthRanged += i.getEquipStats().getStrengthRanged();
		builder.strengthMagic += i.getEquipStats().getStrengthMagic();
		builder.prayer += i.getEquipStats().getPrayer();
	}
	
	public static EquipmentStats fromMap(Map<EquipmentInventorySlot, ItemStats> slotMap, WeaponMode weaponMode, ItemStats tbpDarts)
	{
		ItemStats weapon = slotMap.get(EquipmentInventorySlot.WEAPON);
		EquipmentStatsBuilder b = EquipmentStats.builder();
		slotMap.values().forEach(i -> addValues(b, i));
		
		b.speed = weapon == null ? 4 : weapon.getEquipStats().getSpeed();
		if (weaponMode != null && weaponMode.getCombatFocus() == CombatFocus.RAPID)
			b.speed -= 1;
		
		if (weapon != null && weapon.getItemId() == ItemID.TOXIC_BLOWPIPE)
			addValues(b, tbpDarts);
		
		return b.build();
	}

}
