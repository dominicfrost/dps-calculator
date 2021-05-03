package com.duckblade.osrs.dpscalc.ui.skills;

import java.awt.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.runelite.api.Client;
import net.runelite.api.Skill;

import static net.runelite.api.Skill.*;

@Singleton
public class SkillsPanel extends JPanel
{

	private final Client client;

	private final StatBox attack;
	private final StatBox strength;
	private final StatBox defense;
	private final StatBox magic;
	private final StatBox ranged;
	private final StatBox prayer;

	private final StatBox boostAttack;
	private final StatBox boostStrength;
	private final StatBox boostDefense;
	private final StatBox boostMagic;
	private final StatBox boostRanged;
	private final StatBox boostPrayer;

	@Inject
	public SkillsPanel(Client client)
	{
		this.client = client;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton loadFromClientButton = new JButton("Load From Client");
		loadFromClientButton.addActionListener(e -> loadFromClient());
		loadFromClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(loadFromClientButton);

		add(Box.createVerticalStrut(10));

		attack = new StatBox("att", true);
		strength = new StatBox("str", true);
		defense = new StatBox("def", true);
		magic = new StatBox("mage", true);
		ranged = new StatBox("range", true);
		prayer = new StatBox("prayer", true);

		add(new StatCategory("Player Stats", Arrays.asList(
				attack,
				strength,
				defense,
				magic,
				ranged,
				prayer
		)));

		add(Box.createVerticalStrut(10));

		boostAttack = new StatBox("att", true);
		boostStrength = new StatBox("str", true);
		boostDefense = new StatBox("def", true);
		boostMagic = new StatBox("mage", true);
		boostRanged = new StatBox("range", true);
		boostPrayer = new StatBox("prayer", true);

		add(new StatCategory("Boosts", Arrays.asList(
				boostAttack,
				boostStrength,
				boostDefense,
				boostMagic,
				boostRanged,
				boostPrayer
		)));
	}

	public void loadFromClient()
	{
		// TODO
	}

	public boolean isReady()
	{
		return Stream.of(attack, strength, defense, magic, ranged, prayer).noneMatch(sb -> sb.getValue() == 0);
	}
	
	public String getSummary()
	{
		if (isReady())
			return "Set";

		return "Not Set";
	}

	private static Map<Skill, Integer> buildMap(int a, int s, int d, int m, int r, int p)
	{
		HashMap<Skill, Integer> skills = new HashMap<>(6);
		skills.put(ATTACK, a);
		skills.put(STRENGTH, s);
		skills.put(DEFENCE, d);
		skills.put(MAGIC, m);
		skills.put(RANGED, r);
		skills.put(PRAYER, p);
		return skills;
	}

	public Map<Skill, Integer> getSkills()
	{
		return buildMap(
				attack.getValue(),
				strength.getValue(),
				defense.getValue(),
				magic.getValue(),
				ranged.getValue(),
				prayer.getValue()
		);
	}

	public Map<Skill, Integer> getBoosts()
	{
		return buildMap(
				boostAttack.getValue(),
				boostStrength.getValue(),
				boostDefense.getValue(),
				boostMagic.getValue(),
				boostRanged.getValue(),
				boostPrayer.getValue()
		);
	}
}
