package com.duckblade.osrs.dpscalc.model;

import lombok.Getter;

public class WeaponMode
{
	
	@Getter
	private final String displayName;
	
	@Getter
	private final CombatMode mode;
	
	@Getter
	private final CombatFocus combatFocus;
	
	@Getter
	private final MeleeStyle meleeStyle;

	public WeaponMode(String displayName, CombatMode mode, CombatFocus combatFocus, MeleeStyle meleeStyle)
	{
		this.displayName = displayName;
		this.mode = mode;
		this.combatFocus = combatFocus;
		this.meleeStyle = meleeStyle;
		assert mode != CombatMode.MELEE || meleeStyle != null;
	}
}
