package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.calc.CalcInput;
import com.duckblade.osrs.dpscalc.calc.CalcManager;
import com.duckblade.osrs.dpscalc.calc.CalcResult;
import com.duckblade.osrs.dpscalc.model.NpcStats;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.InteractingChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Singleton
public class DpsOverlay extends Overlay
{

	private final CalcManager calcManager;
	private final Client client;
	private final DpsCalcConfig config;
	private final ItemDataManager itemDataManager;
	private final NpcDataManager npcDataManager;

	private long lastAttackTimestamp = 0;
	private int lastNpcId = -1;
	private CalcResult lastDpsResult = null;
	
	private NpcStats currentTarget;
	private Map currentTarget;

	@Inject
	public DpsOverlay(CalcManager calcManager, Client client, DpsCalcConfig config, EventBus eventBus, ItemDataManager itemDataManager, NpcDataManager npcDataManager)
	{
		eventBus.register(this);
		this.client = client;
		this.config = config;
		this.itemDataManager = itemDataManager;
		this.npcDataManager = npcDataManager;

		setPosition(OverlayPosition.BOTTOM_LEFT);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!shouldDraw())
		{
			return null;
		}


		return null;
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged ev)
	{
		

		calculateDPS();
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied ev)
	{
		if (ev.getHitsplat().isMine() && ev.getActor() != client.getLocalPlayer())
		{
			lastAttackTimestamp = System.currentTimeMillis();
		}
	}

	private boolean shouldDraw()
	{
		if (!config.overlayEnabled() || lastDpsResult == null)
		{
			return false;
		}

		long secondsSinceLastAttack = (System.currentTimeMillis() - lastAttackTimestamp) / 1000;
		return secondsSinceLastAttack < config.overlayTimeoutSeconds();
	}
	
	private boolean calculateDPS()
	{
		CalcInput.builder()
			.activeHp(client.getBoostedSkillLevel(Skill.HITPOINTS));
		
		calcManager.calculateDPS()
	}

}
