package com.duckblade.osrs.dpscalc.calc;

import com.duckblade.osrs.dpscalc.model.CalcInput;
import com.duckblade.osrs.dpscalc.model.CombatFocus;
import com.duckblade.osrs.dpscalc.model.EquipmentFlags;
import javax.inject.Singleton;
import net.runelite.api.Skill;

@Singleton
// https://oldschool.runescape.wiki/w/Damage_per_second/Melee
public class MeleeDpsCalc extends AbstractCalc
{

	private float gearBonus(CalcInput input)
	{
		int salveLevel = salveLevel(input);
		if (salveLevel == 2)
			return 6f / 5f;
		else if (salveLevel == 1 || useSlayerMask(input))
			return 7f / 6f;
		else
			return 1f;
	}
	
	private int effectiveStrengthLevel(CalcInput input)
	{
		int str = input.getPlayerSkills().get(Skill.STRENGTH);
		str += input.getPlayerBoosts().get(Skill.STRENGTH);
		str = (int) (str * 1.0f); // TODO prayers
		
		if (input.getWeaponMode().getStyle() == CombatFocus.AGGRESSIVE)
			str += 3;
		else if (input.getWeaponMode().getStyle() == CombatFocus.CONTROLLED)
			str += 1;
		str += 8;
		
		if (input.getEquipmentFlags().contains(EquipmentFlags.VOID_MELEE))
			str = (int) (str * 1.1f);
		
		return str;
	}
	
	private int effectiveAttackLevel(CalcInput input)
	{
		int att = input.getPlayerSkills().get(Skill.ATTACK);
		att += input.getPlayerBoosts().get(Skill.ATTACK);
		att = (int) (att * 1.0f); // TODO prayers
		
		if (input.getWeaponMode().getStyle() == CombatFocus.ACCURATE)
			att += 3;
		else if (input.getWeaponMode().getStyle() == CombatFocus.CONTROLLED)
			att += 1;
		att += 8;
		
		if (input.getEquipmentFlags().contains(EquipmentFlags.VOID_MELEE))
			att = (int) (att * 1.1f);
		
		return att;
	}
	
	private int maximumHit(CalcInput input)
	{
		int maxHit = effectiveStrengthLevel(input) * (input.getEquipmentStats().getStrengthMelee() + 64);
		maxHit += 320;
		maxHit /= 640;
		
		return (int) (maxHit * gearBonus(input));
	}
	
	private int attackRoll(CalcInput input)
	{
		int attRoll = effectiveAttackLevel(input);
		switch (input.getWeaponMode().getMeleeStyle())
		{
			case STAB:
				attRoll *= input.getEquipmentStats().getAccuracyStab() + 64;
				break;
			case SLASH:
				attRoll *= input.getEquipmentStats().getAccuracySlash() + 64;
				break;
			case CRUSH:
				attRoll *= input.getEquipmentStats().getAccuracyCrush() + 64;
				break;
			default:
				throw new IllegalArgumentException("Invalid melee attack style");
		}
		
		return (int) (attRoll * gearBonus(input));
	}
	
	private int defenseRoll(CalcInput input)
	{
		int defLevel = input.getNpcTarget().getLevelDefense() + 9;
		switch (input.getWeaponMode().getMeleeStyle())
		{
			case STAB:
				return defLevel * (input.getNpcTarget().getBonusDefenseStab() + 64);
			case SLASH:
				return defLevel * (input.getNpcTarget().getBonusDefenseSlash() + 64);
			case CRUSH:
				return defLevel * (input.getNpcTarget().getBonusDefenseCrush() + 64);
			default:
				throw new IllegalArgumentException("Invalid melee attack style");
		}
	}
	
	private float hitChance(CalcInput input)
	{
		int attRoll = attackRoll(input);
		int defRoll = defenseRoll(input);
		if (attRoll > defRoll)
			return 1f - ((defRoll + 2f) / (2f * attRoll + 1f));
		else
			return attRoll / (2f * defRoll + 1f);
	}
	
	public float damagePerTick(CalcInput input)
	{
		float weaponSpeed = (float) input.getEquipmentStats().getSpeed();
		float dmgPerHit = maximumHit(input) * hitChance(input) / 2f;
		return dmgPerHit / weaponSpeed;
	}
	
}
