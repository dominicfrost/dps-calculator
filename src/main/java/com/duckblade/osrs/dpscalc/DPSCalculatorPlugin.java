package com.duckblade.osrs.dpscalc;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
		name = "DPS Calculator"
)
public class DPSCalculatorPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private DPSCalculatorConfig config;

	@Override
	protected void startUp()
	{
		log.info("DPS Calculator started!");
	}

	@Override
	protected void shutDown()
	{
		log.info("DPS Calculator stopped!");
	}

	@Provides
	DPSCalculatorConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DPSCalculatorConfig.class);
	}
}
