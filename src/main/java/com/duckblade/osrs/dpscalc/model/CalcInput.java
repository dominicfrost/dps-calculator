package com.duckblade.osrs.dpscalc.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Skill;

@Data
@Builder
public class CalcInput
{
	
	private final WeaponMode weaponMode;
	
	private final NpcStats npcTarget;
	
	private final Map<Skill, Integer> playerSkills;
	
	private final Map<Skill, Integer> playerBoosts;
	
	private final EquipmentStats equipmentStats;
	
	private final Map<EquipmentInventorySlot, ItemStats> playerEquipment;
	
	private final List<EquipmentFlags> equipmentFlags;
	
	@Builder.Default
	private boolean onSlayerTask = false;
	
	@Builder.Default
	private int health = 1;
	
}
