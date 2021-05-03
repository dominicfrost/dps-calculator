package com.duckblade.osrs.dpscalc.ui.npc;

import com.duckblade.osrs.dpscalc.NpcDataManager;
import com.duckblade.osrs.dpscalc.model.NpcStats;
import com.duckblade.osrs.dpscalc.ui.skills.StatBox;
import com.duckblade.osrs.dpscalc.ui.skills.StatCategory;
import com.duckblade.osrs.dpscalc.ui.util.AutoCompletion;
import com.duckblade.osrs.dpscalc.ui.util.CustomJCheckBox;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.runelite.client.ui.PluginPanel;

@Singleton
public class NpcStatsPanel extends JPanel
{

	private final NpcDataManager npcDataManager;
	private final JCheckBox manualEntry;
	private final JComboBox<String> npcSelect;
	private final JComboBox<String> combatLevelSelect;
	private NpcStats lastSet;

	// levels
	private final StatBox hpBox;
	private final StatBox attackBox;
	private final StatBox strengthBox;
	private final StatBox defenseBox;
	private final StatBox magicBox;
	private final StatBox rangedBox;

	// defensive bonuses
	private final StatBox bonusDefStabBox;
	private final StatBox bonusDefSlashBox;
	private final StatBox bonusDefCrushBox;
	private final StatBox bonusDefMagicBox;
	private final StatBox bonusDefRangedBox;

	// enemy classes
	private final CustomJCheckBox isUndead;
	private final CustomJCheckBox isDemon;
	private final CustomJCheckBox isDragon;
	private final CustomJCheckBox isKalphite;
	private final CustomJCheckBox isLeafy;
	private final CustomJCheckBox isVampyre;
	private final CustomJCheckBox isXerician;

	private final ActionListener updateSelectActionListener;

	@Inject
	public NpcStatsPanel(NpcDataManager npcDataManager)
	{
		this.npcDataManager = npcDataManager;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel manualEntryPanel = new JPanel();
		manualEntryPanel.setLayout(new BorderLayout());
		add(manualEntryPanel);

		manualEntry = new JCheckBox();
		manualEntry.addActionListener(e -> setManualMode(manualEntry.isSelected()));
		manualEntryPanel.add(manualEntry, BorderLayout.WEST);

		JLabel manualEntryLabel = new JLabel("Manual Entry Mode");
		manualEntryLabel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				manualEntry.setSelected(!manualEntry.isSelected());
				setManualMode(manualEntry.isSelected());
			}
		});
		manualEntryPanel.add(manualEntryLabel, BorderLayout.CENTER);
		
		add(Box.createRigidArea(new Dimension(1, 5)));

		npcSelect = new JComboBox<>(npcDataManager.getAllNpcNames());
		npcSelect.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH - 25, 25));
		AutoCompletion.enable(npcSelect);
		add(npcSelect);

		combatLevelSelect = new JComboBox<>();
		npcSelect.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 25, 25));
		combatLevelSelect.setVisible(false);
		add(combatLevelSelect);

		// need to preserve action listener so it can be removed before modifying the list
		updateSelectActionListener = e -> updateSelected(e.getSource() == combatLevelSelect);
		npcSelect.addActionListener(updateSelectActionListener);
		combatLevelSelect.addActionListener(updateSelectActionListener);

		add(Box.createRigidArea(new Dimension(1, 5)));

		hpBox = new StatBox("hitpoints");
		attackBox = new StatBox("att");
		strengthBox = new StatBox("str");
		defenseBox = new StatBox("def");
		magicBox = new StatBox("mage");
		rangedBox = new StatBox("range");

		bonusDefStabBox = new StatBox("dstab");
		bonusDefSlashBox = new StatBox("dslash");
		bonusDefCrushBox = new StatBox("dcrush");
		bonusDefMagicBox = new StatBox("dmagic");
		bonusDefRangedBox = new StatBox("drange");

		add(new StatCategory("Combat Stats", Arrays.asList(
				hpBox,
				attackBox,
				strengthBox,
				defenseBox,
				magicBox,
				rangedBox
		)));
		add(Box.createVerticalStrut(5));

		add(new StatCategory("Defensive Bonuses", Arrays.asList(
				bonusDefStabBox,
				bonusDefSlashBox,
				bonusDefCrushBox,
				bonusDefMagicBox,
				bonusDefRangedBox
		)));
		add(Box.createVerticalStrut(5));

		isDemon = new CustomJCheckBox("Demon");
		isDemon.setEditable(false);
		add(isDemon);

		isDragon = new CustomJCheckBox("Dragon");
		isDragon.setEditable(false);
		add(isDragon);

		isKalphite = new CustomJCheckBox("Kalphite");
		isKalphite.setEditable(false);
		add(isKalphite);

		isLeafy = new CustomJCheckBox("Leafy");
		isLeafy.setEditable(false);
		add(isLeafy);

		isUndead = new CustomJCheckBox("Undead");
		isUndead.setEditable(false);
		add(isUndead);

		isVampyre = new CustomJCheckBox("Vampyre");
		isVampyre.setEditable(false);
		add(isVampyre);

		isXerician = new CustomJCheckBox("Xerician");
		isXerician.setEditable(false);
		add(isXerician);

	}

	public void setManualMode(boolean manualMode)
	{
		if (!manualMode)
		{
			npcSelect.setSelectedIndex(0);
		}
		npcSelect.setVisible(!manualMode);
		combatLevelSelect.setVisible(!manualMode);
		setAllEditable(manualMode);
	}
	
	public boolean isReady()
	{
		return manualEntry.isSelected() || lastSet != null;
	}

	public NpcStats toNpcStats()
	{
		if (!manualEntry.isSelected())
			return lastSet;

		return new NpcStats(
				"Custom",

				// levels
				hpBox.getValue(),
				attackBox.getValue(),
				strengthBox.getValue(),
				defenseBox.getValue(),
				magicBox.getValue(),
				rangedBox.getValue(),

				// defensive bonuses
				bonusDefStabBox.getValue(),
				bonusDefSlashBox.getValue(),
				bonusDefCrushBox.getValue(),
				bonusDefMagicBox.getValue(),
				bonusDefRangedBox.getValue(),

				// combat level not applicable
				0,

				// flags
				isDemon.getValue(),
				isDragon.getValue(),
				isKalphite.getValue(),
				isLeafy.getValue(),
				isUndead.getValue(),
				isVampyre.getValue(),
				isXerician.getValue()
		);
	}
	
	public void loadNpcById(int npcId)
	{
		NpcStats npc = npcDataManager.getNpcStatsById(npcId);
		if (npc == null)
			return;
		
		loadNpcStats(npc);
	}

	public void loadNpcStats(NpcStats stats)
	{
		lastSet = stats;
		manualEntry.setSelected(false);

		// levels
		hpBox.setValue(stats.getLevelHp());
		attackBox.setValue(stats.getLevelAttack());
		strengthBox.setValue(stats.getLevelStrength());
		defenseBox.setValue(stats.getLevelDefense());
		magicBox.setValue(stats.getLevelMagic());
		rangedBox.setValue(stats.getLevelRanged());

		// defensive bonuses
		bonusDefStabBox.setValue(stats.getBonusDefenseStab());
		bonusDefSlashBox.setValue(stats.getBonusDefenseSlash());
		bonusDefCrushBox.setValue(stats.getBonusDefenseCrush());
		bonusDefMagicBox.setValue(stats.getBonusDefenseMagic());
		bonusDefRangedBox.setValue(stats.getBonusDefenseRange());

		isUndead.setValue(stats.isUndead());
		isDemon.setValue(stats.isDemon());
		isDragon.setValue(stats.isDragon());
		isKalphite.setValue(stats.isKalphite());
		isLeafy.setValue(stats.isLeafy());
		isVampyre.setValue(stats.isVampyre());
		isXerician.setValue(stats.isXerician());
	}

	private void updateSelected(boolean fromCombatSelector)
	{
		SwingUtilities.invokeLater(() ->
		{
			Object npcName = npcSelect.getSelectedItem();
			if (!(npcName instanceof String))
			{
				combatLevelSelect.setVisible(false);
				return;
			}

			Map<Integer, NpcStats> levelsMap = npcDataManager.getNpcStatsByName((String) npcName);
			if (levelsMap == null || levelsMap.size() == 0)
			{
				combatLevelSelect.setVisible(false);
				return;
			}
			combatLevelSelect.setVisible(true);

			if (!fromCombatSelector)
			{
				combatLevelSelect.removeActionListener(updateSelectActionListener);
				combatLevelSelect.removeAllItems();
				levelsMap.keySet()
						.stream()
						.sorted()
						.forEach(lvl -> combatLevelSelect.insertItemAt("Level " + lvl, 0));
				combatLevelSelect.setSelectedIndex(0);
				combatLevelSelect.addActionListener(updateSelectActionListener);
			}

			//noinspection ConstantConditions
			int selectedLevel = Integer.parseInt(((String) combatLevelSelect.getSelectedItem()).substring(6));
			NpcStats selectedStats = levelsMap.get(selectedLevel);
			if (selectedStats != null)
				loadNpcStats(selectedStats);

			revalidate();
			repaint();
		});
	}

	private void setAllEditable(boolean editable)
	{
		hpBox.setEditable(editable);
		attackBox.setEditable(editable);
		strengthBox.setEditable(editable);
		defenseBox.setEditable(editable);
		magicBox.setEditable(editable);
		rangedBox.setEditable(editable);

		bonusDefStabBox.setEditable(editable);
		bonusDefSlashBox.setEditable(editable);
		bonusDefCrushBox.setEditable(editable);
		bonusDefMagicBox.setEditable(editable);
		bonusDefRangedBox.setEditable(editable);

		isUndead.setEditable(editable);
		isDemon.setEditable(editable);
		isDragon.setEditable(editable);
		isKalphite.setEditable(editable);
		isLeafy.setEditable(editable);
		isVampyre.setEditable(editable);
		isXerician.setEditable(editable);
	}

	public String getSummary()
	{
		if (!isReady())
			return "Not Set";
		
		return manualEntry.isSelected() ? "Entered Manually" : lastSet.getName();
	}

}
