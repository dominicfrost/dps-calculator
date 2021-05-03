package com.duckblade.osrs.dpscalc.calc;

public abstract class AbstractCalc
{
	private static final float SECONDS_PER_TICK = 0.6f;
	
	abstract int attackRoll(CalcInput input);

	abstract int defenseRoll(CalcInput input);

	abstract int maxHit(CalcInput input);
	
	float hitChance(CalcInput input)
	{
		int attRoll = attackRoll(input);
		int defRoll = defenseRoll(input);
		
		if (attRoll > defRoll)
			return  1f - ((defRoll + 2f) / (2f * attRoll + 1f));
		else
			return attRoll / (2f * defRoll + 1f);
	}
	
	float calculateDPS(CalcInput input)
	{
		float weaponSpeed = input.getEquipmentStats().getSpeed();
		return (maxHit(input) * hitChance(input)) / (2f * weaponSpeed * SECONDS_PER_TICK);
	}
	
}
