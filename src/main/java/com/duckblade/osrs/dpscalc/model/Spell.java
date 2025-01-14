package com.duckblade.osrs.dpscalc.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static net.runelite.api.ItemID.*;

@RequiredArgsConstructor
public enum Spell
{

	// powered staves/specials
	SALAMANDER_BLAZE(-1, "Salamander Blaze", 0, Spellbook.POWERED_STAVES),
	MAGIC_DART(-1, "Magic Dart", 10, Spellbook.POWERED_STAVES),
	FLAMES_OF_ZAMORAK(-1, "Flames of Zamorak", 20, Spellbook.POWERED_STAVES),
	CLAWS_OF_GUTHIX(-1, "Claws of Guthix", 20, Spellbook.POWERED_STAVES),
	SARADOMIN_STRIKE(-1, "Saradomin Strike", 20, Spellbook.POWERED_STAVES),
	CRUMBLE_UNDEAD(-1, "Crumble Undead", 15, Spellbook.POWERED_STAVES),
	IBAN_BLAST(-1, "Iban Blast", 25, Spellbook.POWERED_STAVES),
	SANGUINESTI(-1, "Sanguinesti Staff", 24, Spellbook.POWERED_STAVES),
	SWAMP(-1, "Trident of the Swamp", 23, Spellbook.POWERED_STAVES),
	SEAS(-1, "Trident of the Seas", 20, Spellbook.POWERED_STAVES),
	GAUNTLET_1(-1, "Crystal Staff (basic)", 23, Spellbook.POWERED_STAVES),
	GAUNTLET_2(-1, "Crystal Staff (attuned)", 31, Spellbook.POWERED_STAVES),
	GAUNTLET_3(-1, "Crystal Staff (perfected)", 39, Spellbook.POWERED_STAVES),
	
	// ancient spells
	ICE_BARRAGE(46, "Ice Barrage", 30, Spellbook.ANCIENT),
	BLOOD_BARRAGE(45, "Blood Barrage", 29, Spellbook.ANCIENT),
	SHADOW_BARRAGE(44, "Shadow Barrage", 28, Spellbook.ANCIENT),
	SMOKE_BARRAGE(43, "Smoke Barrage", 27, Spellbook.ANCIENT),
	ICE_BLITZ(42, "Ice Blitz", 26, Spellbook.ANCIENT),
	BLOOD_BLITZ(41, "Blood Blitz", 25, Spellbook.ANCIENT),
	SHADOW_BLITZ(40, "Shadow Blitz", 24, Spellbook.ANCIENT),
	SMOKE_BLITZ(39, "Smoke Blitz", 23, Spellbook.ANCIENT),
	ICE_BURST(38, "Ice Burst", 22, Spellbook.ANCIENT),
	BLOOD_BURST(37, "Blood Burst", 21, Spellbook.ANCIENT),
	SHADOW_BURST(36, "Shadow Burst", 18, Spellbook.ANCIENT),
	SMOKE_BURST(35, "Smoke Burst", 17, Spellbook.ANCIENT),
	ICE_RUSH(34, "Ice Rush", 16, Spellbook.ANCIENT),
	BLOOD_RUSH(33, "Blood Rush", 15, Spellbook.ANCIENT),
	SHADOW_RUSH(32, "Shadow Rush", 14, Spellbook.ANCIENT),
	SMOKE_RUSH(31, "Smoke Rush", 13, Spellbook.ANCIENT),
	
	// standard spells
	FIRE_SURGE(51, "Fire Surge", 24, Spellbook.STANDARD),
	EARTH_SURGE(50, "Earth Surge", 23, Spellbook.STANDARD),
	WATER_SURGE(49, "Water Surge", 22, Spellbook.STANDARD),
	WIND_SURGE(48, "Wind Surge", 21, Spellbook.STANDARD),
	FIRE_WAVE(16, "Fire Wave", 20, Spellbook.STANDARD),
	EARTH_WAVE(15, "Earth Wave", 19, Spellbook.STANDARD),
	WATER_WAVE(14, "Water Wave", 18, Spellbook.STANDARD),
	WIND_WAVE(13, "Wind Wave", 17, Spellbook.STANDARD),
	FIRE_BLAST(12, "Fire Blast", 16, Spellbook.STANDARD),
	EARTH_BLAST(11, "Earth Blast", 15, Spellbook.STANDARD),
	WATER_BLAST(10, "Water Blast", 14, Spellbook.STANDARD),
	WIND_BLAST(9, "Wind Blast", 13, Spellbook.STANDARD),
	FIRE_BOLT(8, "Fire Bolt", 12, Spellbook.STANDARD),
	EARTH_BOLT(7, "Earth Bolt", 11, Spellbook.STANDARD),
	WATER_BOLT(6, "Water Bolt", 10, Spellbook.STANDARD),
	WIND_BOLT(5, "Wind Bolt", 9, Spellbook.STANDARD),
	FIRE_STRIKE(4, "Fire Strike", 8, Spellbook.STANDARD),
	EARTH_STRIKE(3, "Earth Strike", 6, Spellbook.STANDARD),
	WATER_STRIKE(2, "Water Strike", 4, Spellbook.STANDARD),
	WIND_STRIKE(1, "Wind Strike", 2, Spellbook.STANDARD),
	
	// arceuus
	INFERIOR_DEMONBANE(53, "Inferior Demonbane", 16, Spellbook.ARCEUUS),
	SUPERIOR_DEMONBANE(54, "Superior Demonbane", 23, Spellbook.ARCEUUS),
	DARK_DEMONBANE(55, "Dark Demonbane", 30, Spellbook.ARCEUUS),
	GHOSTLY_GRASP(56, "Ghostly Grasp", 12, Spellbook.ARCEUUS),
	SKELETAL_GRASP(57, "Skeletal Grasp", 17, Spellbook.ARCEUUS),
	UNDEAD_GRASP(58, "Undead Grasp", 24, Spellbook.ARCEUUS),
	;

	public enum Spellbook
	{
		STANDARD,
		ANCIENT,
		ARCEUUS,
		POWERED_STAVES,
		;
	}
	
	public static final int SPELL_SELECTED_VARBIT = 276;
	
	@Getter
	private final int varbValue;

	@Getter
	private final String displayName;

	@Getter
	private final int baseMaxHit;

	private final Spellbook spellbook;
	
	public static final List<Spell> DEMONBANE_SPELLS = Arrays.asList(
		INFERIOR_DEMONBANE,
		SUPERIOR_DEMONBANE,
		DARK_DEMONBANE
	);

	private static final List<Spell> STANDARD_SPELLS =
			Arrays.stream(values())
					.filter(s -> s.spellbook == Spellbook.STANDARD)
					.collect(Collectors.toList());

	private static final List<Spell> STANDARD_AND_ANCIENT_SPELLS = // there are no staves that can do ancient but not standard
			Arrays.stream(values())
					.filter(s -> s.spellbook == Spellbook.STANDARD || s.spellbook == Spellbook.ANCIENT)
					.collect(Collectors.toList());
	
	private static final List<Spell> ARCEUUS_SPELLS =
		Arrays.stream(values())
			.filter(s -> s.spellbook == Spellbook.ARCEUUS)
			.collect(Collectors.toList());
	
	private static final List<Spell> ALL_SPELLBOOKS =
		Arrays.stream(values())
			.filter(s -> s.spellbook != Spellbook.POWERED_STAVES)
			.collect(Collectors.toList());

	private static final List<Spell> WAVE_AND_SURGE = Arrays.asList(
			WIND_WAVE, WATER_WAVE, EARTH_WAVE, FIRE_WAVE, WIND_SURGE, WATER_SURGE, EARTH_SURGE, FIRE_SURGE, CRUMBLE_UNDEAD
	);
	private static final List<Spell> SLAYER_STAFF = Stream.concat(WAVE_AND_SURGE.stream(), Stream.of(MAGIC_DART, CRUMBLE_UNDEAD)).collect(Collectors.toList());
	private static final List<Spell> VOID_MACE = Stream.concat(WAVE_AND_SURGE.stream(), Stream.of(CLAWS_OF_GUTHIX, CRUMBLE_UNDEAD)).collect(Collectors.toList());
	private static final List<Spell> GUTHIX_STAFF = Stream.of(MAGIC_DART, CLAWS_OF_GUTHIX, CRUMBLE_UNDEAD).collect(Collectors.toList());
	private static final List<Spell> SARA_STAFF = Stream.of(MAGIC_DART, SARADOMIN_STRIKE, CRUMBLE_UNDEAD).collect(Collectors.toList());
	private static final List<Spell> ZAM_STAFF = Stream.concat(
		Stream.of(MAGIC_DART, FLAMES_OF_ZAMORAK, CRUMBLE_UNDEAD),
		ARCEUUS_SPELLS.stream()
	).collect(Collectors.toList());

	public static List<Spell> forWeapon(int staffId, boolean ahrimsDamned)
	{
		switch (staffId)
		{
			case TRIDENT_OF_THE_SEAS:
			case TRIDENT_OF_THE_SEAS_E:
			case TRIDENT_OF_THE_SEAS_FULL:
				return Collections.singletonList(SEAS);

			case TRIDENT_OF_THE_SWAMP:
			case TRIDENT_OF_THE_SWAMP_E:
				return Collections.singletonList(SWAMP);
				
			case CRYSTAL_STAFF_BASIC:
			case CORRUPTED_STAFF_BASIC:
				return Collections.singletonList(GAUNTLET_1);
				
			case CRYSTAL_STAFF_ATTUNED:
			case CORRUPTED_STAFF_ATTUNED:
				return Collections.singletonList(GAUNTLET_2);
				
			case CRYSTAL_STAFF_PERFECTED:
			case CORRUPTED_STAFF_PERFECTED:
				return Collections.singletonList(GAUNTLET_3);
				
			case SANGUINESTI_STAFF:
			case HOLY_SANGUINESTI_STAFF:
				return Collections.singletonList(SANGUINESTI);
				
			case IBANS_STAFF:
			case IBANS_STAFF_U:
				return Collections.singletonList(IBAN_BLAST);
			
			case SLAYERS_STAFF:
			case SLAYERS_STAFF_E:
				return SLAYER_STAFF;
				
			case STAFF_OF_BALANCE:
				return GUTHIX_STAFF;

			case STAFF_OF_LIGHT:
				return SARA_STAFF;
				
			case STAFF_OF_THE_DEAD:
			case TOXIC_STAFF_OF_THE_DEAD:
				return ZAM_STAFF;
				
			case VOID_KNIGHT_MACE:
			case VOID_KNIGHT_MACE_L:
				return VOID_MACE;
				
			case ANCIENT_STAFF:
			case ANCIENT_STAFF_20431:
			case NIGHTMARE_STAFF:
			case ELDRITCH_NIGHTMARE_STAFF:
			case VOLATILE_NIGHTMARE_STAFF:
				return STANDARD_AND_ANCIENT_SPELLS;
				
			case MASTER_WAND:
			case KODAI_WAND:
				return ALL_SPELLBOOKS;
				
			case SWAMP_LIZARD:
			case ORANGE_SALAMANDER:
			case RED_SALAMANDER:
			case BLACK_SALAMANDER:
				return Collections.singletonList(SALAMANDER_BLAZE);
				
			case SKULL_SCEPTRE:
				return ARCEUUS_SPELLS;
				
			case SKULL_SCEPTRE_I:
				return Stream.concat(ARCEUUS_SPELLS.stream(), Stream.of(CRUMBLE_UNDEAD)).collect(Collectors.toList());

			case AHRIMS_STAFF:
			case AHRIMS_STAFF_0:
			case AHRIMS_STAFF_25:
			case AHRIMS_STAFF_50:
			case AHRIMS_STAFF_75:
			case AHRIMS_STAFF_100:
				if (ahrimsDamned)
					return ALL_SPELLBOOKS;
				else
					return Stream.concat(STANDARD_SPELLS.stream(), ARCEUUS_SPELLS.stream()).collect(Collectors.toList());
				
			default:
				return STANDARD_SPELLS;
		}
	}

}
