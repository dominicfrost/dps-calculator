package com.duckblade.osrs.dpscalc.model;

import lombok.Getter;

public enum MeleeStyle
{

	CRUSH("Crush"),
	SLASH("Slash"),
	STAB("Stab"),
	;
	
	@Getter
	private final String displayName;

	MeleeStyle(String displayName)
	{
		this.displayName = displayName;
	}
}
