package com.duckblade.osrs.dpscalc.calc;

import com.duckblade.osrs.dpscalc.model.CalcInput;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CalcManager
{
	
	private static final float SECONDS_PER_TICK = 0.6f;

	private MageDpsCalc mageDpsCalc;
	private MeleeDpsCalc meleeDpsCalc;
	private RangedDpsCalc rangedDpsCalc;

	@Inject
	public CalcManager(MageDpsCalc mageDpsCalc, MeleeDpsCalc meleeDpsCalc, RangedDpsCalc rangedDpsCalc)
	{
		this.mageDpsCalc = mageDpsCalc;
		this.meleeDpsCalc = meleeDpsCalc;
		this.rangedDpsCalc = rangedDpsCalc;
	}

	public float calculateDPS(CalcInput input)
	{
		assert input.getWeaponMode() != null;
		assert input.getWeaponMode().getMode() != null;

		float dpt;
		switch (input.getWeaponMode().getMode())
		{
			case MAGE:
				dpt = mageDpsCalc.damagePerTick(input);
				break;
			case MELEE:
				dpt = meleeDpsCalc.damagePerTick(input);
				break;
			case RANGED:
				dpt = rangedDpsCalc.damagePerTick(input);
				break;
			default:
				throw new IllegalArgumentException("Missing weapon mode");
		}

		return dpt / SECONDS_PER_TICK;
	}
	

}
