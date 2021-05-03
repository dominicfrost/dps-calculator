package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.calc.EquipmentRequirement;
import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.Spell;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import com.duckblade.osrs.dpscalc.model.WeaponType;
import com.duckblade.osrs.dpscalc.ui.util.CustomJComboBox;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
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

	private final CustomJComboBox<ItemStats> tbpDartSelectPanel;
	private final CustomJComboBox<WeaponMode> weaponModeSelect;
	private final CustomJComboBox<Spell> spellSelect;

	private final JPanel totalsPanel;

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
		loadFromClientButton.setAlignmentX(CENTER_ALIGNMENT);
		add(loadFromClientButton);

		add(Box.createVerticalStrut(10));

		JPanel slotPanel = new JPanel();
		slotPanel.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 0));
		slotPanel.setLayout(new BoxLayout(slotPanel, BoxLayout.Y_AXIS));
		slotPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(slotPanel);

		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			EquipmentSlotPanel innerPanel = new EquipmentSlotPanel(this.rlItemManager, this.itemDataManager, slot, this::onEquipmentChanged);
			this.slotPanels.put(slot, innerPanel);
			slotPanel.add(innerPanel);
			slotPanel.add(Box.createRigidArea(new Dimension(1, 5)));
		}
		weaponSlot = slotPanels.get(EquipmentInventorySlot.WEAPON);

		add(Box.createVerticalStrut(5));

		tbpDartSelectPanel = new CustomJComboBox<>(itemDataManager.getAllDarts(), ItemStats::getName, "Blowpipe Darts");
		tbpDartSelectPanel.setCallback(this::onEquipmentChanged);
		tbpDartSelectPanel.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH - 25, 50));
		tbpDartSelectPanel.setAlignmentX(CENTER_ALIGNMENT);
		tbpDartSelectPanel.setVisible(false);
		tbpDartSelectPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		add(tbpDartSelectPanel);

		weaponModeSelect = new CustomJComboBox<>(WeaponType.UNARMED.getWeaponModes(), WeaponMode::getDisplayName, "Weapon Mode");
		weaponModeSelect.setCallback(this::onEquipmentChanged);
		weaponModeSelect.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH - 25, 50));
		weaponModeSelect.setAlignmentX(CENTER_ALIGNMENT);
		add(weaponModeSelect);

		add(Box.createVerticalStrut(10));

		spellSelect = new CustomJComboBox<>(Collections.emptyList(), Spell::getDisplayName, "Spell");
		spellSelect.setCallback(this::onEquipmentChanged);
		spellSelect.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH - 25, 50));
		spellSelect.setAlignmentX(CENTER_ALIGNMENT);
		spellSelect.setVisible(false);
		spellSelect.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		add(spellSelect);

		add(Box.createVerticalStrut(10));

		totalsPanel = new JPanel();
		totalsPanel.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 0));
		totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
		totalsPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(totalsPanel);

		rebuildTotals();
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

	public ItemStats getTbpDarts()
	{
		return tbpDartSelectPanel.getValue();
	}
	
	public Spell getSpell()
	{
		return spellSelect.getValue();
	}

	public void preload(Map<EquipmentInventorySlot, ItemStats> items)
	{
		items.forEach((key, value) -> slotPanels.get(key).setValue(value));
	}

	public void onEquipmentChanged()
	{
		SwingUtilities.invokeLater(() ->
		{
			ItemStats currentWeapon = weaponSlot.getValue();

			boolean dartSelectVisible = currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE;
			tbpDartSelectPanel.setVisible(dartSelectVisible);

			List<WeaponMode> modes = currentWeapon == null ? WeaponType.UNARMED.getWeaponModes() : currentWeapon.getWeaponType().getWeaponModes();
			weaponModeSelect.setItems(modes);

			WeaponMode weaponMode = getWeaponMode();
			if (weaponMode != null && weaponMode.getMode() == CombatMode.MAGE)
			{
				assert currentWeapon != null;
				Map<EquipmentInventorySlot, ItemStats> equipment = getEquipment();
				boolean ahrimsDamned = EquipmentRequirement.AHRIMS.isSatisfied(equipment) && EquipmentRequirement.AMULET_DAMNED.isSatisfied(equipment);
				List<Spell> availableSpells = Spell.forWeapon(currentWeapon.getItemId(), ahrimsDamned);
				spellSelect.setItems(availableSpells);
				if (availableSpells.size() == 1)
				{
					// don't prompt for a spell if there's only one available (powered staves/salamanders)
					spellSelect.setValue(availableSpells.get(0));
					spellSelect.setVisible(false);
				}
				else
				{
					spellSelect.setVisible(true);
				}
			}
			else
			{
				spellSelect.setVisible(false);
			}

			rebuildTotals();
			revalidate();
			repaint();
		});
	}

	private JLabel buildStatLabel(String statName, int stat)
	{
		JLabel label = new JLabel(statName + ": " + stat);
		label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private void rebuildTotals()
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
		totalsPanel.add(buildStatLabel("Ranged Strength", stats.getStrengthRanged()));
		totalsPanel.add(buildStatLabel("Magic Strength", stats.getStrengthMagic()));

		totalsPanel.add(Box.createVerticalStrut(10));

		totalsPanel.add(buildStatLabel("Weapon Speed", stats.getSpeed()));
		totalsPanel.add(buildStatLabel("Prayer", stats.getPrayer()));
	}

	public boolean isReady()
	{
		// ensure selected dart if using tbp
		ItemStats currentWeapon = weaponSlot.getValue();
		if (currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE && getTbpDarts() == null)
			return false;

		// ensure selected attack style (wearing nothing is fine)
		WeaponMode weaponMode = weaponModeSelect.getValue();
		if (weaponMode == null)
			return false;
		
		// ensure spell is selected if needed
		return weaponMode.getMode() != CombatMode.MAGE || spellSelect.getValue() != null;
	}

	public String getSummary()
	{
		if (!isReady())
			return "Not Set";

		String weaponName = weaponSlot.getValue() == null ? "Unarmed" : weaponSlot.getValue().getName();
		return weaponModeSelect.getValue().getMode() + " - " + weaponName;
	}
}
