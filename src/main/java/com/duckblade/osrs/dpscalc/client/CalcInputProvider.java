package com.duckblade.osrs.dpscalc.client;

import com.duckblade.osrs.dpscalc.calc.CalcInput;
import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.NpcStats;
import com.duckblade.osrs.dpscalc.model.Prayer;
import com.duckblade.osrs.dpscalc.model.Spell;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import java.util.Collection;
import java.util.Map;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Skill;

public interface CalcInputProvider
{

	CombatMode getCombatMode();

	WeaponMode getWeaponMode();

	NpcStats getNpcTarget();

	Map<Skill, Integer> getPlayerSkills();

	Map<Skill, Integer> getPlayerBoosts();

	EquipmentStats getEquipmentStats();

	Map<EquipmentInventorySlot, ItemStats> getPlayerEquipment();

	Spell getSpell();

	Collection<Prayer> getEnabledPrayers();

	int getPrayerDrain();

	boolean isOnSlayerTask();

	int getActiveHp();

	int getMaxHp();

	boolean isUsingCharge();

	default CalcInput.CalcInputBuilder builder()
	{
		return CalcInput.builder()
			.combatMode(getCombatMode())
			.weaponMode(getWeaponMode())
			.npcTarget(getNpcTarget())
			.playerSkills(getPlayerSkills())
			.playerBoosts(getPlayerBoosts())
			.equipmentStats(getEquipmentStats())
			.playerEquipment(getPlayerEquipment())
			.spell(getSpell())
			.enabledPrayers(getEnabledPrayers())
			.prayerDrain(getPrayerDrain())
			.onSlayerTask(isOnSlayerTask())
			.activeHp(getActiveHp())
			.maxHp(getMaxHp())
			.usingCharge(isUsingCharge());
	}

	default CalcInput createInput()
	{
		return builder().build();
	}

}
