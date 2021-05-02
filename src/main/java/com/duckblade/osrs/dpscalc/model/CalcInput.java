package com.duckblade.osrs.dpscalc.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Skill;

@Data
@Builder
public class CalcInput
{
	
	@Getter
	private final WeaponMode weaponMode;
	
	@Getter
	private final NpcStats npcTarget;
	
	@Getter
	private final Map<Skill, Integer> playerSkills;
	
	@Getter
	private final Map<Skill, Integer> playerBoosts;
	
	@Getter
	private final EquipmentStats equipmentStats;
	
	@Getter
	private final Map<EquipmentInventorySlot, ItemStats> playerEquipment;
	
	@Getter
	private final List<EquipmentFlags> equipmentFlags;
	
	@Builder.Default
	private boolean onSlayerTask = false;
	
	@Builder.Default
	private int health = 1;
	
}
