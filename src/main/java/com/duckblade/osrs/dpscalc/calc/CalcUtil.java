package com.duckblade.osrs.dpscalc.calc;

import com.duckblade.osrs.dpscalc.model.CombatMode;
import lombok.experimental.UtilityClass;

import static com.duckblade.osrs.dpscalc.calc.EquipmentRequirement.*;

@UtilityClass
public class CalcUtil
{
	
	public boolean blackMask(CalcInput input)
	{
		if (!input.isOnSlayerTask())
			return false;

		if (input.getCombatMode() == CombatMode.MELEE)
			return BLACK_MASK_MELEE.isSatisfied(input);
		
		return BLACK_MASK_MAGE_RANGED.isSatisfied(input);
	}
	
	// salve has 3 levels - off, on, enhanced
	public int salveLevel(CalcInput input)
	{
		if (!input.getNpcTarget().isUndead())
			return 0;

		if (input.getCombatMode() == CombatMode.MELEE)
		{
			if (!SALVE_MELEE.isSatisfied(input))
				return 0;
		}
		else if (!SALVE_MAGE_RANGED.isSatisfied(input))
			return 0;
		
		return SALVE_ENHANCED.isSatisfied(input) ? 2 : 1;
	}
	
	// void also has 3 levels, but only for ranged/mage
	public int voidLevel(CalcInput input)
	{
		if (input.getCombatMode() == CombatMode.MELEE)
			return VOID_MELEE.isSatisfied(input) ? 1 : 0;
		
		if (input.getCombatMode() == CombatMode.RANGED && !VOID_RANGED.isSatisfied(input))
			return 0;
		if (input.getCombatMode() == CombatMode.MAGE && !VOID_MAGE.isSatisfied(input))
			return 0;
		
		return VOID_ELITE.isSatisfied(input) ? 2 : 1;
	}
	
}
