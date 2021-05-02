package com.duckblade.osrs.dpscalc.model;

import java.util.Map;
import lombok.Builder;
import lombok.Data;
import net.runelite.api.EquipmentInventorySlot;

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

	public static EquipmentStats fromMap(Map<EquipmentInventorySlot, ItemStats> slotMap)
	{
		EquipmentStatsBuilder b = EquipmentStats.builder();
		for (ItemStats i : slotMap.values())
		{
			if (i == null)
				continue;
			
			b.accuracyStab += i.getEquipStats().getAccuracyStab();
			b.accuracySlash += i.getEquipStats().getAccuracySlash();
			b.accuracyCrush += i.getEquipStats().getAccuracyCrush();
			b.accuracyMagic += i.getEquipStats().getAccuracyMagic();
			b.accuracyRanged += i.getEquipStats().getAccuracyRanged();
			b.strengthMelee += i.getEquipStats().getStrengthMelee();
			b.strengthRanged += i.getEquipStats().getStrengthRanged();
			b.strengthMagic += i.getEquipStats().getStrengthMagic();
			b.prayer += i.getEquipStats().getPrayer();
		}
		
		b.speed = slotMap.get(EquipmentInventorySlot.WEAPON).getEquipStats().getSpeed();
		return b.build();
	}

}
