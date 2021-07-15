package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.model.DartType;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Units;

@ConfigGroup("dpscalculator")
public interface DpsCalcConfig extends Config
{
	@ConfigItem(
		keyName = "showMinimenuEntry",
		name = "Right-Click Entry",
		description = "Whether to add a right-click entry to NPCs to quickly load them into the side panel.",
		position = 1
	)
	default boolean showMinimenuEntry()
	{
		return true;
	}

	@ConfigSection(
		name = "Overlay Settings",
		description = "Contains settings for the overlay DPS panel, which provides DPS calculations for the target you are currently fighting.",
		closedByDefault = true,
		position = 2
	)
	String OVERLAY_GROUP = "overlay";

	@ConfigItem(
		keyName = "defaultTbpDarts",
		name = "Default Blowpipe Darts",
		description = "Default darts to use for the DPS overlay. Due to client restrictions, this cannot be automatically detected.",
		section = OVERLAY_GROUP,
		position = 3
	)
	default DartType defaultTbpDarts()
	{
		return DartType.NONE;
	}

	@ConfigItem(
		keyName = "overlayEnabled",
		name = "Enable Overlay",
		description = "Whether to show the DPS overlay panel.",
		section = OVERLAY_GROUP,
		position = 4
	)
	default boolean overlayEnabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "overlayTimeoutSeconds",
		name = "Timeout Seconds",
		description = "How long to display the panel after you stop attacking enemies.",
		section = OVERLAY_GROUP,
		position = 5
	)
	@Units(Units.SECONDS)
	default int overlayTimeoutSeconds()
	{
		return 15;
	}

	@ConfigItem(
		keyName = "overlayShowDPS",
		name = "Show DPS",
		description = "Whether to include the player's estimated DPS against the target.",
		section = OVERLAY_GROUP,
		position = 6
	)
	default boolean overlayShowDPS()
	{
		return true;
	}

	@ConfigItem(
		keyName = "overlayShowMaxHit",
		name = "Show Max Hit",
		description = "Whether to include the player's max hit against the target.",
		section = OVERLAY_GROUP,
		position = 7
	)
	default boolean overlayShowMaxHit()
	{
		return true;
	}

	@ConfigItem(
		keyName = "overlayShowAccuracy",
		name = "Show Accuracy",
		description = "Whether to include the player's accuracy against the target.",
		section = OVERLAY_GROUP,
		position = 8
	)
	default boolean overlayShowAccuracy()
	{
		return true;
	}

	@ConfigItem(
		keyName = "overlayShowTTK",
		name = "Show Average TTK",
		description = "Whether to include the player's accuracy against the target.",
		section = OVERLAY_GROUP,
		position = 9
	)
	default boolean overlayShowTTK()
	{
		return true;
	}

}
