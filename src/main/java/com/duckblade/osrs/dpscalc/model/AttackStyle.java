package com.duckblade.osrs.dpscalc.model;

import lombok.Data;
import lombok.Getter;

@Data
public class AttackStyle
{
	
	@Getter
	private final CombatMode mode;
	
	@Getter
	private final CombatFocus style;
	
	@Getter
	private final MeleeStyle meleeStyle;
	
}
