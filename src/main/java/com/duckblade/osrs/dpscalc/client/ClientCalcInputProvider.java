package com.duckblade.osrs.dpscalc.client;

import com.duckblade.osrs.dpscalc.DpsCalcConfig;
import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.NpcDataManager;
import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.NpcStats;
import com.duckblade.osrs.dpscalc.model.Prayer;
import com.duckblade.osrs.dpscalc.model.Spell;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import com.duckblade.osrs.dpscalc.model.WeaponType;
import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;

@Singleton
public class ClientCalcInputProvider implements CalcInputProvider
{

	private final Client client;
	private final DpsCalcConfig config;
	private final ItemDataManager itemDataManager;
	private final NpcDataManager npcDataManager;

	private Integer lastNpcTarget;

	@Inject
	public ClientCalcInputProvider(Client client,
								   DpsCalcConfig config,
								   ItemDataManager itemDataManager,
								   NpcDataManager npcDataManager,
								   EventBus eventBus)
	{
		this.client = client;
		this.config = config;
		this.itemDataManager = itemDataManager;
		this.npcDataManager = npcDataManager;
		eventBus.register(this);
	}

	@Override
	public CombatMode getCombatMode()
	{
		assert client.isClientThread();
		return null; // todo
	}

	@Override
	public WeaponMode getWeaponMode()
	{
		assert client.isClientThread();

		ItemStats weapon = getPlayerEquipment().get(EquipmentInventorySlot.WEAPON);
		WeaponType weaponType = weapon != null ? weapon.getWeaponType() : WeaponType.UNARMED;
		List<WeaponMode> weaponModes = weaponType.getWeaponModes();

		int weaponModeVarp = client.getVar(VarPlayer.ATTACK_STYLE);
		return weaponModes.stream()
			.filter(wm -> wm.getVarpValue() == weaponModeVarp)
			.findAny()
			.orElse(null);
	}

	@Override
	public NpcStats getNpcTarget()
	{
		assert client.isClientThread();
		if (lastNpcTarget == null)
		{
			return null;
		}

		return npcDataManager.getNpcStatsById(lastNpcTarget);
	}

	@Override
	public Map<Skill, Integer> getPlayerSkills()
	{
		assert client.isClientThread();
		return Stream.of(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.PRAYER)
			.collect(Collectors.toMap(s -> s, client::getRealSkillLevel));
	}

	@Override
	public Map<Skill, Integer> getPlayerBoosts()
	{
		assert client.isClientThread();
		return Stream.of(Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.MAGIC, Skill.RANGED, Skill.PRAYER)
			.collect(Collectors.toMap(
				s -> s,
				s -> client.getBoostedSkillLevel(s) - client.getRealSkillLevel(s)
			));
	}

	@Override
	public EquipmentStats getEquipmentStats()
	{
		assert client.isClientThread();
		return EquipmentStats.fromMap(getPlayerEquipment(), getWeaponMode(), config.defaultTbpDarts().getItemStats());
	}

	@Override
	public Map<EquipmentInventorySlot, ItemStats> getPlayerEquipment()
	{
		assert client.isClientThread();

		ItemContainer equipped = client.getItemContainer(InventoryID.EQUIPMENT);
		if (equipped == null)
		{
			return Collections.emptyMap();
		}

		ImmutableMap.Builder<EquipmentInventorySlot, ItemStats> builder = ImmutableMap.builder();
		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			Item equippedItem = equipped.getItem(slot.getSlotIdx());
			ItemStats stats = equippedItem == null ? null
				: itemDataManager.getItemStatsById(equippedItem.getId());
			if (stats == null)
			{
				continue;
			}

			builder.put(slot, stats);
		}

		return builder.build();
	}

	@Override
	public Spell getSpell()
	{
		assert client.isClientThread();
		return null; // todo
	}

	@Override
	public Collection<Prayer> getEnabledPrayers()
	{
		assert client.isClientThread();
		return Arrays.stream(Prayer.values())
			.filter(p -> client.isPrayerActive(p.getRlPrayer()))
			.collect(Collectors.toList());
	}

	@Override
	public int getPrayerDrain()
	{
		return getEnabledPrayers().stream()
			.mapToInt(Prayer::getDrainRate)
			.sum();
	}

	@Override
	public boolean isOnSlayerTask()
	{
		assert client.isClientThread();
		return false; // todo
	}

	@Override
	public int getActiveHp()
	{
		assert client.isClientThread();
		return client.getBoostedSkillLevel(Skill.HITPOINTS);
	}

	@Override
	public int getMaxHp()
	{
		assert client.isClientThread();
		return client.getRealSkillLevel(Skill.HITPOINTS);
	}

	@Override
	public boolean isUsingCharge()
	{
		assert client.isClientThread();
		return false; // todo
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged ev)
	{
		if (ev.getSource() != client.getLocalPlayer() || !(ev.getTarget() instanceof NPC))
		{
			return;
		}

		lastNpcTarget = ((NPC) ev.getTarget()).getId();
	}

}
