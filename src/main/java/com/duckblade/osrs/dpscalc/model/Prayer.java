package com.duckblade.osrs.dpscalc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Varbits;

@RequiredArgsConstructor
@Getter
public enum Prayer
{

	THICK_SKIN("Thick Skin", net.runelite.api.Prayer.THICK_SKIN, PrayerGroup.UTILITY, 1f, 1f, 3),
	BURST_OF_STRENGTH("Burst of Strength", net.runelite.api.Prayer.BURST_OF_STRENGTH, PrayerGroup.MELEE, 1f, 1.05f, 3),
	CLARITY_OF_THOUGHT("Clarity of Thought", net.runelite.api.Prayer.CLARITY_OF_THOUGHT, PrayerGroup.MELEE, 1.05f, 1f, 3),
	SHARP_EYE("Sharp Eye", net.runelite.api.Prayer.SHARP_EYE, PrayerGroup.RANGED, 1.05f, 1f, 3),
	MYSTIC_WILL("Mystic Will", net.runelite.api.Prayer.MYSTIC_WILL, PrayerGroup.MAGE, 1.05f, 1f, 3),
	ROCK_SKIN("Rock Skin", net.runelite.api.Prayer.ROCK_SKIN, PrayerGroup.UTILITY, 1f, 1f, 6),
	SUPERHUMAN_STRENGTH("Superhuman Strength", net.runelite.api.Prayer.SUPERHUMAN_STRENGTH, PrayerGroup.MELEE, 1f, 1.1f, 6),
	IMPROVED_REFLEXES("Improved Reflexes", net.runelite.api.Prayer.IMPROVED_REFLEXES, PrayerGroup.MELEE, 1.1f, 1f, 6),
	RAPID_RESTORE("Rapid Restore", net.runelite.api.Prayer.RAPID_RESTORE, PrayerGroup.UTILITY, 1f, 1f, 1),
	RAPID_HEAL("Rapid Heal", net.runelite.api.Prayer.RAPID_HEAL, PrayerGroup.UTILITY, 1f, 1f, 2),
	PROTECT_ITEM("Protect Item", net.runelite.api.Prayer.PROTECT_ITEM, PrayerGroup.UTILITY, 1f, 1f, 2),
	HAWK_EYE("Hawk Eye", net.runelite.api.Prayer.HAWK_EYE, PrayerGroup.RANGED, 1.1f, 1f, 6),
	MYSTIC_LORE("Mystic Lore", net.runelite.api.Prayer.MYSTIC_LORE, PrayerGroup.MAGE, 1.1f, 1f, 6),
	STEEL_SKIN("Steel Skin", net.runelite.api.Prayer.STEEL_SKIN, PrayerGroup.UTILITY, 1f, 1f, 12),
	ULTIMATE_STRENGTH("Ultimate Strength", net.runelite.api.Prayer.ULTIMATE_STRENGTH, PrayerGroup.MELEE, 1f, 1.15f, 12),
	INCREDIBLE_REFLEXES("Incredible Reflexes", net.runelite.api.Prayer.INCREDIBLE_REFLEXES, PrayerGroup.MELEE, 1.15f, 1f, 12),
	PROTECT_FROM_MAGIC("Protect from Magic", net.runelite.api.Prayer.PROTECT_FROM_MAGIC, PrayerGroup.UTILITY, 1f, 1.05f, 12),
	PROTECT_FROM_MISSILES("Protect from Missiles", net.runelite.api.Prayer.PROTECT_FROM_MISSILES, PrayerGroup.UTILITY, 1f, 1.05f, 12),
	PROTECT_FROM_MELEE("Protect from Melee", net.runelite.api.Prayer.PROTECT_FROM_MELEE, PrayerGroup.UTILITY, 1f, 1.05f, 12),
	EAGLE_EYE("Eagle Eye", net.runelite.api.Prayer.EAGLE_EYE, PrayerGroup.RANGED, 1.15f, 1f, 12),
	MYSTIC_MIGHT("Mystic Might", net.runelite.api.Prayer.MYSTIC_MIGHT, PrayerGroup.MAGE, 1.15f, 1f, 12),
	RETRIBUTION("Retribution", net.runelite.api.Prayer.RETRIBUTION, PrayerGroup.UTILITY, 1f, 1f, 3),
	REDEMPTION("Redemption", net.runelite.api.Prayer.REDEMPTION, PrayerGroup.UTILITY, 1f, 1f, 6),
	SMITE("Smite", net.runelite.api.Prayer.SMITE, PrayerGroup.UTILITY, 1f, 1f, 18),
	PRESERVE("Preserve", net.runelite.api.Prayer.PRESERVE, PrayerGroup.UTILITY, 1f, 1f, 2),
	CHIVALRY("Chivalry", net.runelite.api.Prayer.CHIVALRY, PrayerGroup.MELEE, 1.15f, 1.18f, 24),
	PIETY("Piety", net.runelite.api.Prayer.PIETY, PrayerGroup.MELEE, 1.2f, 1.23f, 24),
	RIGOUR("Rigour", net.runelite.api.Prayer.RIGOUR, PrayerGroup.RANGED, 1.2f, 1.23f, 24),
	AUGURY("Augury", net.runelite.api.Prayer.AUGURY, PrayerGroup.MAGE, 1.25f, 1f, 24),
	;

	public enum PrayerGroup
	{
		MAGE,
		MELEE,
		RANGED,
		UTILITY,
		;
	}

	private final String displayName;
	private final net.runelite.api.Prayer rlPrayer;
	private final PrayerGroup prayerGroup;
	private final float attackMod;
	private final float strengthMod;
	private final int drainRate;

}
