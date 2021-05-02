package com.duckblade.osrs.dpscalc.ui.equip;

import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.duckblade.osrs.dpscalc.model.WeaponMode;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WeaponModeSelectPanel extends JPanel
{

	private final JComboBox<String> comboBox;
	private ItemStats currentWeapon;

	public WeaponModeSelectPanel()
	{
		setLayout(new BorderLayout());
		
		add(new JLabel("Weapon Mode"), BorderLayout.NORTH);
		
		comboBox = new JComboBox<>(new String[] {""});
		add(comboBox, BorderLayout.CENTER);
	}

	public void setWeapon(ItemStats newWeapon)
	{
		if (newWeapon != currentWeapon)
		{
			currentWeapon = newWeapon;
			updateComboBox();
		}
	}

	private void updateComboBox()
	{
		comboBox.removeAllItems();
		comboBox.addItem("");

		if (currentWeapon == null)
			return;
		
		currentWeapon.getWeaponType()
				.getWeaponModes()
				.forEach(wm -> comboBox.addItem(wm.getDisplayName()));
	}

	public WeaponMode getValue()
	{
		int ix = comboBox.getSelectedIndex();
		if (ix <= 0)
			return null;

		return currentWeapon.getWeaponType()
				.getWeaponModes()
				.get(ix - 1);
	}

}
