package com.duckblade.osrs.dpscalc.client;

import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.NpcStats;
import com.duckblade.osrs.dpscalc.model.Prayer;
import com.duckblade.osrs.dpscalc.model.Spell;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import com.duckblade.osrs.dpscalc.ui.equip.EquipmentPanel;
import com.duckblade.osrs.dpscalc.ui.npc.NpcStatsPanel;
import com.duckblade.osrs.dpscalc.ui.prayer.PrayerPanel;
import com.duckblade.osrs.dpscalc.ui.skills.SkillsPanel;
import java.util.Collection;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.Skill;

@Singleton
public class SwingCalcInputProvider implements CalcInputProvider
{
	
	@Inject
	private EquipmentPanel equipmentPanel;
	
	@Inject
	private NpcStatsPanel npcStatsPanel;
	
	@Inject
	private PrayerPanel prayerPanel;
	
	@Inject
	private SkillsPanel skillsPanel;

	@Override
	public CombatMode getCombatMode()
	{
		return getWeaponMode().getMode();
	}

	@Override
	public WeaponMode getWeaponMode()
	{
		return equipmentPanel.getWeaponMode();
	}

	@Override
	public NpcStats getNpcTarget()
	{
		return npcStatsPanel.toNpcStats();
	}

	@Override
	public Map<Skill, Integer> getPlayerSkills()
	{
		return skillsPanel.getSkills();
	}

	@Override
	public Map<Skill, Integer> getPlayerBoosts()
	{
		return skillsPanel.getBoosts();
	}

	@Override
	public EquipmentStats getEquipmentStats()
	{
		return EquipmentStats.fromMap(getPlayerEquipment(), getWeaponMode(), equipmentPanel.getTbpDarts());
	}

	@Override
	public Map<EquipmentInventorySlot, ItemStats> getPlayerEquipment()
	{
		return equipmentPanel.getEquipment();
	}

	@Override
	public Spell getSpell()
	{
		return equipmentPanel.getSpell();
	}

	@Override
	public Collection<Prayer> getEnabledPrayers()
	{
		return prayerPanel.getSelected();
	}

	@Override
	public int getPrayerDrain()
	{
		return prayerPanel.getDrain();
	}

	@Override
	public boolean isOnSlayerTask()
	{
		return equipmentPanel.isOnSlayerTask();
	}

	@Override
	public int getActiveHp()
	{
		return equipmentPanel.getActiveHp();
	}

	@Override
	public int getMaxHp()
	{
		return equipmentPanel.getMaxHp();
	}

	@Override
	public boolean isUsingCharge()
	{
		return false;
	}
}
