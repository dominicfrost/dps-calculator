package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.calc.EquipmentRequirement;
import com.duckblade.osrs.dpscalc.client.CalcInputProvider;
import com.duckblade.osrs.dpscalc.model.CombatMode;
import com.duckblade.osrs.dpscalc.model.DartType;
import com.duckblade.osrs.dpscalc.model.EquipmentStats;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.Spell;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import com.duckblade.osrs.dpscalc.model.WeaponType;
import com.duckblade.osrs.dpscalc.ui.util.CustomJCheckBox;
import com.duckblade.osrs.dpscalc.ui.util.CustomJComboBox;
import com.duckblade.osrs.dpscalc.ui.util.JTextFieldIntOnlyKeyAdapter;
import com.duckblade.osrs.dpscalc.ui.util.SelectAllFocusListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import net.runelite.api.Client;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

public class EquipmentPanel extends JPanel
{
	
	private static final DecimalFormat STAT_LABEL_FORMAT = new DecimalFormat(": #.#");

	private final CalcInputProvider calcInputProvider; // used for loadFromClient
	private final Client client;
	private final ClientThread clientThread;
	private final ItemManager rlItemManager;
	private final ItemDataManager itemDataManager;

	private final Map<EquipmentInventorySlot, EquipmentSlotPanel> slotPanels;
	private final CustomJCheckBox slayerCheck;
	private final EquipmentSlotPanel weaponSlot;

	private final CustomJComboBox<DartType> tbpDartSelectPanel;
	private final CustomJComboBox<WeaponMode> weaponModeSelect;
	private final CustomJComboBox<Spell> spellSelect;
	
	private final JPanel dharokPanel;
	private final JTextField dharokHpField;
	private final JTextField dharokMaxHpField;

	private final JPanel totalsPanel;

	@Inject
	public EquipmentPanel(CalcInputProvider calcInputProvider, @Nullable Client client, @Nullable ClientThread clientThread, @Nullable ItemManager rlItemManager, ItemDataManager itemDataManager)
	{
		this.calcInputProvider = calcInputProvider;
		this.client = client;
		this.clientThread = clientThread;
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

		slayerCheck = new CustomJCheckBox("On Slayer Task");
		slayerCheck.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		slayerCheck.setValue(false);
		slayerCheck.setEditable(true);
		slayerCheck.setVisible(false);

		for (EquipmentInventorySlot slot : EquipmentInventorySlot.values())
		{
			EquipmentSlotPanel innerPanel = new EquipmentSlotPanel(this.rlItemManager, this.itemDataManager, slot, this::onEquipmentChanged);
			this.slotPanels.put(slot, innerPanel);
			slotPanel.add(innerPanel);
			slotPanel.add(Box.createVerticalStrut(5));

			if (slot == EquipmentInventorySlot.HEAD)
				slotPanel.add(slayerCheck);
		}
		weaponSlot = slotPanels.get(EquipmentInventorySlot.WEAPON);

		add(Box.createVerticalStrut(5));

		dharokHpField = new JTextField("1", 3);
		dharokHpField.setAlignmentX(Component.CENTER_ALIGNMENT); // of component
		dharokHpField.setHorizontalAlignment(JTextField.CENTER); // of inner text
		dharokHpField.addFocusListener(new SelectAllFocusListener(dharokHpField));
		dharokHpField.addKeyListener(new JTextFieldIntOnlyKeyAdapter());

		dharokMaxHpField = new JTextField("99", 3);
		dharokMaxHpField.setAlignmentX(Component.CENTER_ALIGNMENT); // of component
		dharokMaxHpField.setHorizontalAlignment(JTextField.CENTER); // of inner text
		dharokMaxHpField.addFocusListener(new SelectAllFocusListener(dharokHpField));
		dharokMaxHpField.addKeyListener(new JTextFieldIntOnlyKeyAdapter());
		
		dharokPanel = new JPanel();
		dharokPanel.setMinimumSize(new Dimension(0, 70));
		dharokPanel.setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 70));
		dharokPanel.setLayout(new GridLayout(3, 2));
		dharokPanel.setVisible(false);
		add(dharokPanel);
		
		dharokPanel.add(new JLabel("Active HP"));
		dharokPanel.add(new JLabel("Max HP"));
		dharokPanel.add(dharokHpField);
		dharokPanel.add(dharokMaxHpField);
		dharokPanel.add(Box.createVerticalStrut(10));

		tbpDartSelectPanel = new CustomJComboBox<>(Arrays.asList(DartType.values()), DartType::getName, "Blowpipe Darts");
		tbpDartSelectPanel.setCallback(this::onEquipmentChanged);
		tbpDartSelectPanel.setAlignmentX(CENTER_ALIGNMENT);
		tbpDartSelectPanel.setVisible(false);
		add(tbpDartSelectPanel);

		weaponModeSelect = new CustomJComboBox<>(WeaponType.UNARMED.getWeaponModes(), WeaponMode::getDisplayName, "Weapon Mode");
		weaponModeSelect.setCallback(this::onEquipmentChanged);
		weaponModeSelect.setAlignmentX(CENTER_ALIGNMENT);
		add(weaponModeSelect);

		add(Box.createVerticalStrut(10));

		spellSelect = new CustomJComboBox<>(Collections.emptyList(), Spell::getDisplayName, "Spell");
		spellSelect.setCallback(this::onEquipmentChanged);
		spellSelect.setAlignmentX(CENTER_ALIGNMENT);
		spellSelect.setVisible(false);
		add(spellSelect);

		add(Box.createVerticalStrut(10));

		totalsPanel = new JPanel();
		totalsPanel.setMinimumSize(new Dimension(PluginPanel.PANEL_WIDTH, 0));
		totalsPanel.setLayout(new BoxLayout(totalsPanel, BoxLayout.Y_AXIS));
		totalsPanel.setAlignmentX(CENTER_ALIGNMENT);
		add(totalsPanel);

		rebuildTotals();
	}

	public WeaponMode getWeaponMode()
	{
		return weaponModeSelect.getValue();
	}

	public void setWeaponMode(WeaponMode newValue)
	{
		weaponModeSelect.setValue(newValue);
		onEquipmentChanged();
	}

	public Map<EquipmentInventorySlot, ItemStats> getEquipment()
	{
		HashMap<EquipmentInventorySlot, ItemStats> resultMap = new HashMap<>();
		slotPanels.forEach((key, value) -> resultMap.put(key, value.getValue()));
		return resultMap;
	}

	public void setEquipment(Map<EquipmentInventorySlot, ItemStats> newEquipment)
	{
		newEquipment.forEach((slot, item) -> slotPanels.get(slot).setValue(item));

		// don't defer this step to onEquipmentChanged, since it uses invokeLater, and a caller may set this value after setting a weapon
		ItemStats newWeapon = newEquipment.get(EquipmentInventorySlot.WEAPON);
		List<WeaponMode> modes = newWeapon == null ? WeaponType.UNARMED.getWeaponModes() : newWeapon.getWeaponType().getWeaponModes();
		weaponModeSelect.setItems(modes);

		onEquipmentChanged();
	}

	public boolean isOnSlayerTask()
	{
		return slayerCheck.getValue();
	}

	public void setOnSlayerTask(boolean newValue)
	{
		slayerCheck.setValue(newValue);
		onEquipmentChanged();
	}
	
	public int getActiveHp()
	{
		if (!dharokPanel.isVisible())
			return 1;
		
		String content = dharokHpField.getText();
		if (content.isEmpty())
			return 1;
		
		return Integer.parseInt(content);
	}
	
	public int getMaxHp()
	{
		if (!dharokPanel.isVisible())
			return 99;

		String content = dharokMaxHpField.getText();
		if (content.isEmpty())
			return 99;

		return Integer.parseInt(content);
	}

	public ItemStats getTbpDarts()
	{
		DartType selectedDarts = tbpDartSelectPanel.getValue();
		return selectedDarts == null ? null : selectedDarts.getItemStats();
	}

	public Spell getSpell()
	{
		return spellSelect.getValue();
	}

	public void loadFromClient()
	{
		if (client == null || clientThread == null || client.getGameState() != GameState.LOGGED_IN)
		{
			return; // ui test, or not init yet somehow
		}

		clientThread.invokeLater(() ->
		{
			Map<EquipmentInventorySlot, ItemStats> equipment = calcInputProvider.getPlayerEquipment();
			slotPanels.forEach((slot, panel) -> panel.setValue(equipment.get(slot)));

			ItemStats currentWeapon = weaponSlot.getValue();
			updateWeaponModeComboBox(currentWeapon); // this is also done in onEquipmentChanged, but we trigger early so we can set value
			weaponModeSelect.setValue(calcInputProvider.getWeaponMode());

			updateSpellComboBox(currentWeapon, equipment);
			spellSelect.setValue(calcInputProvider.getSpell());
			onEquipmentChanged();
		});
	}
	
	private void updateWeaponModeComboBox(ItemStats currentWeapon)
	{
		List<WeaponMode> modes = currentWeapon == null ? WeaponType.UNARMED.getWeaponModes() : currentWeapon.getWeaponType().getWeaponModes();
		weaponModeSelect.setItems(modes);
	}
	
	private List<Spell> updateSpellComboBox(ItemStats currentWeapon, Map<EquipmentInventorySlot, ItemStats> equipment)
	{
		assert currentWeapon != null;
		boolean ahrimsDamned = EquipmentRequirement.AHRIMS.isSatisfied(equipment) && EquipmentRequirement.AMULET_DAMNED.isSatisfied(equipment);
		List<Spell> availableSpells = Spell.forWeapon(currentWeapon.getItemId(), ahrimsDamned);
		spellSelect.setItems(availableSpells);
		return availableSpells;
	}

	public void onEquipmentChanged()
	{
		SwingUtilities.invokeLater(() ->
		{
			Map<EquipmentInventorySlot, ItemStats> equipment = getEquipment();
			slayerCheck.setVisible(EquipmentRequirement.BLACK_MASK_MELEE.isSatisfied(equipment));

			ItemStats currentWeapon = weaponSlot.getValue();

			boolean dartSelectVisible = currentWeapon != null && currentWeapon.getItemId() == ItemID.TOXIC_BLOWPIPE;
			tbpDartSelectPanel.setVisible(dartSelectVisible);
			
			boolean hpVisible = EquipmentRequirement.DHAROKS.isSatisfied(equipment);
			dharokPanel.setVisible(hpVisible);

			updateWeaponModeComboBox(currentWeapon);

			WeaponMode weaponMode = getWeaponMode();
			if (weaponMode != null && weaponMode.getMode() == CombatMode.MAGE)
			{
				assert currentWeapon != null;
				List<Spell> availableSpells = updateSpellComboBox(currentWeapon, equipment);
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
	
	private JLabel buildStatLabel(String text)
	{
		JLabel label = new JLabel(text);
		label.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
		label.setAlignmentX(Component.LEFT_ALIGNMENT);
		return label;
	}

	private JLabel buildStatLabel(String statName, int stat)
	{
		return buildStatLabel(statName + ": " + stat);
	}

	private JLabel buildStatLabel(String statName, float stat)
	{
		return buildStatLabel(statName + STAT_LABEL_FORMAT.format(stat));
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
		totalsPanel.add(buildStatLabel("Magic Damage Bonus", stats.getStrengthMagic()));

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
