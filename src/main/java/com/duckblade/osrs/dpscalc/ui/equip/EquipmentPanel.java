package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

@Singleton
public class EquipmentPanel extends JPanel
{

	private final Client client;
	private final ItemManager rlItemManager;
	private final ItemDataManager itemDataManager;

	private final Map<EquipmentInventorySlot, EquipmentSlotPanel> slotPanels;
	private final EquipmentSlotPanel weaponSlot;
	private final WeaponModeSelectPanel weaponModeSelect;
	private final JPanel totalsPanel;

	private int lastWeapon;

	@Inject
	public EquipmentPanel(Client client, ItemManager rlItemManager, ItemDataManager itemDataManager)
	{
		this.client = client;
		this.rlItemManager = rlItemManager;
		this.itemDataManager = itemDataManager;
		this.slotPanels = new HashMap<>(EquipmentInventorySlot.values().length);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JButton loadFromClientButton = new JButton("Load From Client");
		loadFromClientButton.addActionListener(e -> loadFromClient());
		loadFromClientButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(loadFromClientButton);

		add(Box.createVerticalStrut(10));

		JPanel slotPanel = new JPanel();
		slotPanel.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 0));
		slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.Y_AXIS));
		slotPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(slotPanel);

		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			EquipmentSlotPanel innerPanel = new EquipmentSlotPanel(this.rlItemManager, this.itemDataManager, slot, this);
			this.slotPanels.put(slot, innerPanel);
			slotPanel.add(innerPanel);
			slotPanel.add(Box.createRigidArea(new Dimension(1, 5)));
		}
		weaponSlot = slotPanels.get(EquipmentInventorySlot.WEAPON);
		lastWeapon = -1;

		add(Box.createVerticalStrut(5));

		weaponModeSelect = new WeaponModeSelectPanel();
		weaponModeSelect.setWeapon(null);
		add(weaponModeSelect);

		add(Box.createVerticalStrut(5));

		totalsPanel = new JPanel();
		totalsPanel.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 0));
		totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
		totalsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(totalsPanel);

		rebuildTotals(false);
	}

	public void loadFromClient()
	{
		// TODO
	}

	public WeaponMode getWeaponMode()
	{
		return weaponModeSelect.getValue();
	}

	public Map<EquipmentInventorySlot, ItemStats> getEquipment()
	{
		HashMap<EquipmentInventorySlot, ItemStats> resultMap = new HashMap<>();
		slotPanels.forEach((key, value) -> resultMap.put(key, value.getValue()));
		return resultMap;
	}

	public void preload(Map<EquipmentInventorySlot, ItemStats> items)
	{
		items.forEach((key, value) -> slotPanels.get(key).setValue(value));
	}

	public void onEquipmentChanged()
	{
		weaponModeSelect.setWeapon(weaponSlot.getValue());
		rebuildTotals(true);
	}

	private JLabel buildStatLabel(String statName, Map<EquipmentInventorySlot, ItemStats> equipment, ToIntFunction<ItemStats.EquipStats> mapper)
	{
		int stat = equipment.values().stream().mapToInt(is -> is == null ? 0 : mapper.applyAsInt(is.getEquipStats())).sum();
		JLabel label = new JLabel(statName + ": " + stat);
		label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private void rebuildTotals(boolean redraw)
	{
		totalsPanel.removeAll();

		Map<EquipmentInventorySlot, ItemStats> equipment = getEquipment();
		totalsPanel.add(Box.createHorizontalGlue());

		totalsPanel.add(buildStatLabel("Stab Accuracy", equipment, ItemStats.EquipStats::getAccuracyStab));
		totalsPanel.add(buildStatLabel("Slash Accuracy", equipment, ItemStats.EquipStats::getAccuracySlash));
		totalsPanel.add(buildStatLabel("Crush Accuracy", equipment, ItemStats.EquipStats::getAccuracyCrush));
		totalsPanel.add(buildStatLabel("Magic Accuracy", equipment, ItemStats.EquipStats::getAccuracyMagic));
		totalsPanel.add(buildStatLabel("Ranged Accuracy", equipment, ItemStats.EquipStats::getAccuracyRanged));

		totalsPanel.add(Box.createVerticalStrut(10));

		totalsPanel.add(buildStatLabel("Melee Strength", equipment, ItemStats.EquipStats::getStrengthMelee));
		totalsPanel.add(buildStatLabel("Magic Strength", equipment, ItemStats.EquipStats::getStrengthMagic));
		totalsPanel.add(buildStatLabel("Ranged Strength", equipment, ItemStats.EquipStats::getStrengthRanged));

		if (redraw)
			SwingUtilities.invokeLater(() ->
			{
				revalidate();
				repaint();
			});
	}

	public boolean isReady()
	{
		return weaponModeSelect.getValue() != null;
	}

	public String getSummary()
	{
		if (!isReady())
			return "Not Set";

		String weaponName = weaponSlot.getValue() == null ? "Unarmed" : weaponSlot.getValue().getName();
		return weaponModeSelect.getValue().getMode() + " - " + weaponName;
	}
}
