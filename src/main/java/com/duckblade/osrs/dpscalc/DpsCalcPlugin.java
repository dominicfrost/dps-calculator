package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.ui.DpsCalculatorPanel;
import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

@Slf4j
@PluginDescriptor(
		name = "DPS Calculator"
)
public class DpsCalcPlugin extends Plugin
{
	@Inject
	private Client client;
	
	@Inject
	private ClientToolbar toolbar;

	@Inject
	private DpsCalcConfig config;

	private NavigationButton navButton;

	@Override
	protected void startUp()
	{
		DpsCalculatorPanel panel = injector.getInstance(DpsCalculatorPanel.class);
		
		navButton = NavigationButton.builder()
				.priority(5)
				.icon(ImageUtil.loadImageResource(getClass(), "ui/equip/slot_0.png"))
				.panel(panel)
				.build();
		toolbar.addNavigation(navButton);
		
		log.info("DPS Calculator started!");
	}

	@Override
	protected void shutDown()
	{
		toolbar.removeNavigation(navButton);
		
		log.info("DPS Calculator stopped!");
	}

	@Provides
	DpsCalcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DpsCalcConfig.class);
	}
}
