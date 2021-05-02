package com.duckblade.osrs.dpscalc.calc;

import com.duckblade.osrs.dpscalc.model.CalcInput;
import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.EquipmentFlags;

public abstract class AbstractCalc
{

	protected abstract float damagePerTick(CalcInput input);

	protected boolean useSlayerMask(CalcInput input)
	{
		if (!input.isOnSlayerTask())
			return false;

		if (input.getWeaponMode().getMode() == CombatMode.MELEE && input.getEquipmentFlags().contains(EquipmentFlags.SLAYER_MASK))
			return true;

		return input.getEquipmentFlags().contains(EquipmentFlags.SLAYER_MASK_I);
	}

	protected int salveLevel(CalcInput input)
	{
		if (!input.getNpcTarget().isUndead())
			return 0;

		if (!input.getEquipmentFlags().contains(EquipmentFlags.SALVE))
			return 0;

		if (input.getWeaponMode().getMode() == CombatMode.MELEE)
			return input.getEquipmentFlags().contains(EquipmentFlags.SALVE_E) ? 2 : 1;
		else if (input.getEquipmentFlags().contains(EquipmentFlags.SALVE_I))
			return 1;

		return 0;
	}
	
}
