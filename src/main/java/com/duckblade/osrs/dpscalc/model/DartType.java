package com.duckblade.osrs.dpscalc.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.api.ItemID;

@RequiredArgsConstructor
public enum DartType
{
	
	DRAGON(ItemID.DRAGON_DART),
	AMETHYST(ItemID.AMETHYST_DART),
	RUNE(ItemID.RUNE_DART),
	ADAMANT(ItemID.ADAMANT_DART),
	MITHRIL(ItemID.MITHRIL_DART),
	BLACK(ItemID.BLACK_DART),
	STEEL(ItemID.STEEL_DART),
	IRON(ItemID.IRON_DART),
	BRONZE(ItemID.BRONZE_DART),
	NONE(-1) // only used for default config value
	;
	
	@Getter
	private final int itemId;
	
	@Getter
	@Setter
	private ItemStats itemStats = null;
	
}
