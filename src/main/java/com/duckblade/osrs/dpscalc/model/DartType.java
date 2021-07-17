package com.duckblade.osrs.dpscalc.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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

	static List<DartType> NON_NULL_VALUES =
		Arrays.stream(values())
			.filter(dt -> dt != NONE)
			.collect(Collectors.toList());

	@Getter
	private final int itemId;

	@Getter
	@Setter
	private ItemStats itemStats = null;

	public String getName()
	{
		return itemStats == null ? "None" : itemStats.getName();
	}

}
