package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.model.DartType;
import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ItemDataManager
{

	private static final Gson GSON = new Gson();

	private final Map<Integer, ItemStats> ITEMS_BY_ID;

	@Inject
	public ItemDataManager()
	{
		try (InputStream fileStream = getClass().getResourceAsStream("items-dps-calc.min.json"); InputStreamReader reader = new InputStreamReader(fileStream))
		{
			ITEMS_BY_ID = GSON.fromJson(reader, new TypeToken<HashMap<Integer, ItemStats>>()
			{
			}.getType());

			ITEMS_BY_ID.forEach((id, stats) -> stats.setItemId(id));
		}
		catch (IOException e)
		{
			log.error("Failed to load item data", e);
			throw new IllegalStateException(e);
		}

		// for tbp selection
		Arrays.stream(DartType.values())
			.filter(dt -> dt != DartType.NONE)
			.forEach(dt -> dt.setItemStats(ITEMS_BY_ID.get(dt.getItemId())));
	}

	public List<ItemStats> getBySlot(final int targetSlot)
	{
		return ITEMS_BY_ID.values()
			.stream()
			.filter(is -> is.getSlot() == targetSlot)
			.sorted(Comparator.comparing(ItemStats::getName))
			.distinct()
			.collect(Collectors.toList());
	}

	public ItemStats getItemStatsById(int npcId)
	{
		return ITEMS_BY_ID.get(npcId);
	}

}
