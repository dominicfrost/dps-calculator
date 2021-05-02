package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.ItemID;
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

	private final JPanel tbpDartSelectPanel;
	private final JComboBox<String> tbpDartSelect;
	
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
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 1000));

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

		tbpDartSelectPanel = new JPanel();
		tbpDartSelectPanel.setLayout(new BorderLayout());
//		tbpDartSelectPanel.setVisible(false);
		add(tbpDartSelectPanel);
		
		tbpDartSelectPanel.add(new JLabel("Blowpipe Darts"), BorderLayout.NORTH);
				
		String[] dartList = new String[ItemDataManager.DART_NAMES.length + 1];
		dartList[0] = "";
		System.arraycopy(ItemDataManager.DART_NAMES, 0, dartList, 1, ItemDataManager.DART_NAMES.length);
		
		tbpDartSelect = new JComboBox<>(dartList);
		tbpDartSelect.addActionListener(e -> onEquipmentChanged());
		tbpDartSelectPanel.add(tbpDartSelect, BorderLayout.CENTER);

		add(Box.createVerticalStrut(5));

		weaponModeSelect = new WeaponModeSelectPanel();
		weaponModeSelect.setWeapon(null);
		add(weaponModeSelect);

		add(Box.createVerticalStrut(10));

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
	
	public void enter()
	{
		ItemStats currentWeapon = weaponSlot.getValue();
		tbpDartSelectPanel.setVisible(currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE);
		// todo magic spell visibility
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

	public ItemStats getTbpDarts()
	{
		int dartIx = tbpDartSelect.getSelectedIndex();
		if (dartIx == 0)
			return null;
		else
			return itemDataManager.getItemStatsById(ItemDataManager.DART_IDS[dartIx - 1]);
	}

	public void preload(Map<EquipmentInventorySlot, ItemStats> items)
	{
		items.forEach((key, value) -> slotPanels.get(key).setValue(value));
	}

	public void onEquipmentChanged()
	{
		ItemStats currentWeapon = weaponSlot.getValue();
		weaponModeSelect.setWeapon(currentWeapon);
		tbpDartSelectPanel.setVisible(currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE);
		rebuildTotals(true);
	}

	private JLabel buildStatLabel(String statName, int stat)
	{
		JLabel label = new JLabel(statName + ": " + stat);
		label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private void rebuildTotals(boolean redraw)
	{
		totalsPanel.removeAll();

		EquipmentStats stats = EquipmentStats.fromMap(getEquipment(), getWeaponMode(), getTbpDarts());
		totalsPanel.add(Box.createHorizontalGlue());

		totalsPanel.add(buildStatLabel("Stab Accuracy", stats.getAccuracyStab()));
		totalsPanel.add(buildStatLabel("Slash Accuracy", stats.getAccuracySlash()));
		totalsPanel.add(buildStatLabel("Crush Accuracy", stats.getAccuracyCrush()));
		totalsPanel.add(buildStatLabel("Magic Accuracy", stats.getAccuracyMagic()));
		totalsPanel.add(buildStatLabel("Ranged Accuracy", stats.getAccuracyRanged()));

		totalsPanel.add(Box.createVerticalStrut(10));

		totalsPanel.add(buildStatLabel("Melee Strength", stats.getStrengthMelee()));
		totalsPanel.add(buildStatLabel("Melee Strength", stats.getStrengthRanged()));
		totalsPanel.add(buildStatLabel("Melee Strength", stats.getStrengthMagic()));

		totalsPanel.add(Box.createVerticalStrut(10));

		totalsPanel.add(buildStatLabel("Weapon Speed", stats.getSpeed()));
		totalsPanel.add(buildStatLabel("Prayer", stats.getPrayer()));

		if (redraw)
			SwingUtilities.invokeLater(() ->
			{
				revalidate();
				repaint();
			});
	}

	public boolean isReady()
	{
		// ensure selected dart if using tbp
		ItemStats currentWeapon = weaponSlot.getValue();
		if (currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE && tbpDartSelect.getSelectedIndex() == 0)
			return false;

		// ensure selected attack style (wearing nothing is fine)
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
