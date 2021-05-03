package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.ItemDataManager;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.ui.util.AutoCompletion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.runelite.api.EquipmentInventorySlot;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.AsyncBufferedImage;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.SwingUtil;

// todo convert to CustomJComboBox
public class EquipmentSlotPanel extends JPanel
{

	private static final ImageIcon CLEAR_ICON = new ImageIcon(ImageUtil.loadImageResource(EquipmentSlotPanel.class, "icon_clear.png"));

	private final ItemManager rlItemManager;
	private final ItemDataManager itemDataManager;
	private final Runnable callback;

	private final ImageIcon defaultIcon;
	private final JLabel imageLabel;
	private final JComboBox<String> itemSelect;

	private ItemStats lastSet = null;

	public EquipmentSlotPanel(ItemManager rlItemManager, ItemDataManager itemDataManager, EquipmentInventorySlot slot, Runnable callback)
	{
		this.rlItemManager = rlItemManager;
		this.itemDataManager = itemDataManager;
		this.callback = callback;

		setLayout(new BorderLayout());
		setMaximumSize(new Dimension(PluginPanel.PANEL_WIDTH, 25));

		defaultIcon = new ImageIcon(ImageUtil.resizeImage(ImageUtil.loadImageResource(getClass(), "slot_" + slot.getSlotIdx() + ".png"), 25, 25));
		imageLabel = new JLabel(defaultIcon);
		imageLabel.setMaximumSize(new Dimension(25, 25));
		imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
		add(imageLabel, BorderLayout.WEST);

		itemSelect = new JComboBox<>(itemDataManager.getAllItemNames(slot.getSlotIdx()));
		itemSelect.setPrototypeDisplayValue("");
		AutoCompletion.enable(itemSelect);
		itemSelect.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH - 70, 25));
		itemSelect.addActionListener(e -> setValue((String) itemSelect.getSelectedItem()));
		add(itemSelect, BorderLayout.CENTER);

		JButton clearButton = new JButton(CLEAR_ICON);
		clearButton.setPreferredSize(new Dimension(25, 25));
		SwingUtil.removeButtonDecorations(clearButton);
		clearButton.addActionListener(e -> setValue(null, false));
		add(clearButton, BorderLayout.EAST);
	}

	public ItemStats getValue()
	{
		return lastSet;
	}

	public void setValue(ItemStats newValue)
	{
		setValue(newValue, false);
	}

	private void setValue(String newValue)
	{
		ItemStats stats = itemDataManager.getItemStatsByName(newValue);
		setValue(stats, true);
	}

	private void setValue(ItemStats newValue, boolean fromItemSelect)
	{
		if (newValue == lastSet)
			return;

		if (newValue == null)
		{
			imageLabel.setIcon(defaultIcon);
			if (!fromItemSelect)
				itemSelect.setSelectedIndex(0);
		}
		else
		{
			if (rlItemManager != null)
			{
				AsyncBufferedImage icon = rlItemManager.getImage(newValue.getItemId());
				icon.addTo(imageLabel); // resize?
			}
			if (!fromItemSelect)
				itemSelect.setSelectedItem(newValue.getName());
		}

		lastSet = newValue;
		callback.run();
	}
}
