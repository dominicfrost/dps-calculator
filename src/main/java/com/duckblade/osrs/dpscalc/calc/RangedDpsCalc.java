package com.duckblade.osrs.dpscalc.calc;

import com.duckblade.osrs.dpscalc.model.CalcInput;
import com.duckblade.osrs.dpscalc.model.CombatFocus;
import com.duckblade.osrs.dpscalc.model.EquipmentFlags;
import javax.inject.Singleton;
import net.runelite.api.Skill;

@Singleton
// https://oldschool.runescape.wiki/w/Damage_per_second/Ranged
public class RangedDpsCalc extends AbstractCalc
{
	
	private int effectiveRangedStrength(CalcInput input)
	{
		int rngStrength = input.getPlayerSkills().get(Skill.RANGED) + input.getPlayerBoosts().get(Skill.RANGED);
		rngStrength = (int) (rngStrength * 1.0f); // todo prayer boost
		
		if (input.getWeaponMode().getCombatFocus() == CombatFocus.ACCURATE)
			rngStrength += 3;
		rngStrength += 8;
		
		if (input.getEquipmentFlags().contains(EquipmentFlags.VOID_RANGED))
		{
			float voidMod = input.getEquipmentFlags().contains(EquipmentFlags.VOID_ELITE) ? 1.125f : 1.1f;
			rngStrength = (int) (rngStrength * voidMod);
		}
		return rngStrength;
	}
	
	private int maxHit(CalcInput input)
	{
		int maxHit = effectiveRangedStrength(input);
		maxHit *= (input.getEquipmentStats().getStrengthRanged() + 64);
		maxHit += 320;
		maxHit /= 640;
		
		if (salveLevel(input) > 0 || useSlayerMask(input))
			maxHit = (int) (maxHit * (6f / 5f));
		return maxHit;
	}
	
	private int effectiveRangedAttack(CalcInput input)
	{
		// note that this method is the same as effectiveRangedStrength,
		// other than the voidMod
		// prayer boost for rigour will also be different when implemented
		int rngAttack = input.getPlayerSkills().get(Skill.RANGED) + input.getPlayerBoosts().get(Skill.RANGED);
		rngAttack = (int) (rngAttack * 1.0f); // todo prayer boost

		if (input.getWeaponMode().getCombatFocus() == CombatFocus.ACCURATE)
			rngAttack += 3;
		rngAttack += 8;

		if (input.getEquipmentFlags().contains(EquipmentFlags.VOID_RANGED))
		{
			float voidMod = 1.1f;
			rngAttack = (int) (rngAttack * voidMod);
		}
		return rngAttack;
	}
	
	private int attRoll(CalcInput input)
	{
		int attRoll = effectiveRangedAttack(input);
		attRoll *= (input.getEquipmentStats().getAccuracyRanged() + 64);
		if (salveLevel(input) > 0 || useSlayerMask(input))
			attRoll = (int) (attRoll * (6f / 5f));
		return attRoll;
	}
	
	private int defRoll(CalcInput input)
	{
		int defRoll = input.getNpcTarget().getLevelDefense() + 9;
		defRoll *= (input.getNpcTarget().getBonusDefenseRange() + 64);
		return defRoll;
	}

	private float hitChance(CalcInput input)
	{
		int attRoll = attRoll(input);
		int defRoll = defRoll(input);
		if (attRoll > defRoll)
			return 1f - ((defRoll + 2f) / (2f * attRoll + 1f));
		else
			return attRoll / (2f * defRoll + 1f);
	}
	
	public float damagePerTick(CalcInput input)
	{
		float weaponSpeed = (float) input.getEquipmentStats().getSpeed();
		float dmgPerHit = maxHit(input) * hitChance(input) / 2f;
		return dmgPerHit / weaponSpeed;
	}
	
}
