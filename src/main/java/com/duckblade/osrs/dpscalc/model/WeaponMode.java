package com.duckblade.osrs.dpscalc.model;

import lombok.Getter;

public class WeaponMode
{
	
	@Getter
	private final String displayName;
	
	@Getter
	private final CombatMode mode;
	
	@Getter
	private final CombatFocus style;
	
	@Getter
	private final MeleeStyle meleeStyle;

	public WeaponMode(String displayName, CombatMode mode, CombatFocus style, MeleeStyle meleeStyle)
	{
		this.displayName = displayName;
		this.mode = mode;
		this.style = style;
		this.meleeStyle = meleeStyle;
		assert mode != CombatMode.MELEE || meleeStyle != null;
	}
}
