package com.duckblade.osrs.dpscalc.ui.prayer;

import com.duckblade.osrs.dpscalc.model.Prayer;
import com.duckblade.osrs.dpscalc.ui.util.CustomJCheckBox;
import com.duckblade.osrs.dpscalc.ui.util.CustomJComboBox;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

@Singleton
public class PrayerPanel extends JPanel
{

	private final CustomJComboBox<Prayer> offensePrayerSelect;
	private final Map<Prayer, CustomJCheckBox> otherPrayers;

	@Inject
	public PrayerPanel()
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		offensePrayerSelect = new CustomJComboBox<>(Prayer.OFFENSE, Prayer::getDisplayName, "Offensive Prayer");
		add(offensePrayerSelect);

		Box.createVerticalStrut(5);
		
		ImmutableMap.Builder<Prayer, CustomJCheckBox> builder = new ImmutableMap.Builder<>();
		Prayer.UTILITY.forEach(prayer ->
		{
			CustomJCheckBox check = new CustomJCheckBox(prayer.getDisplayName());
			add(check);
			add(Box.createVerticalStrut(5));
			builder.put(prayer, check);
		});
		otherPrayers = builder.build();
	}

	public Prayer getOffensive()
	{
		return offensePrayerSelect.getValue();
	}

	public void setOffensive(Prayer newValue)
	{
		offensePrayerSelect.setValue(newValue);
	}

	private List<Prayer> getOthers()
	{
		return otherPrayers.entrySet()
				.stream()
				.filter(e -> e.getValue().getValue())
				.map(Map.Entry::getKey)
				.collect(Collectors.toList());
	}

	public int getDrain()
	{
		return offensePrayerSelect.getValue().getDrainRate() +
				getOthers().stream()
						.mapToInt(Prayer::getDrainRate)
						.sum();
	}

	public String getSummary()
	{
		Prayer offense = getOffensive();
		List<Prayer> enabledOthers = getOthers();
		if (offense == null && enabledOthers.isEmpty())
			return "None";

		if (offense == null)
		{
			if (enabledOthers.size() == 1)
				return enabledOthers.get(0).getDisplayName();
			return enabledOthers.size() + " prayers";
		}
		else if (enabledOthers.isEmpty())
			return offense.getDisplayName();
		else
			return offense.getDisplayName() + " + " + otherPrayers + " others";
	}

}
