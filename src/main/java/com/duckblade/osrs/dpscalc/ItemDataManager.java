package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class ItemDataManager
{

	private static final Gson GSON = new Gson();

	private final Map<Integer, ItemStats> ALL_STATS;
	private final Map<String, ItemStats> ALL_STATS_BY_NAME;

	@Inject
	public ItemDataManager()
	{
		try (InputStream fileStream = getClass().getResourceAsStream("items-dps-calc.min.json"); InputStreamReader reader = new InputStreamReader(fileStream))
		{
			ALL_STATS = GSON.fromJson(reader, new TypeToken<HashMap<Integer, ItemStats>>()
			{
			}.getType());
		}
		catch (IOException e)
		{
			log.error("Failed to load item data", e);
			throw new IllegalStateException(e);
		}

		ALL_STATS_BY_NAME = new HashMap<>(ALL_STATS.size());
		for (Map.Entry<Integer, ItemStats> entry : ALL_STATS.entrySet())
		{
			ItemStats stats = entry.getValue();
			stats.setItemId(entry.getKey());
			if (stats.getName() != null && stats.getEquipStats() != null)
			{
				ALL_STATS_BY_NAME.put(entry.getValue().getName(), entry.getValue());
			}
		}
	}

	public String[] getAllItemNames(final int targetSlot)
	{
		return Stream.concat(
				Stream.of(""),
				ALL_STATS_BY_NAME.values()
						.stream()
						.filter(is -> is.getEquipStats().getSlot() == targetSlot)
						.map(ItemStats::getName)
						.sorted()
		).toArray(i -> new String[i + 1]);
	}

	public Optional<ItemStats> getItemStatsByName(String npcName)
	{
		return Optional.ofNullable(ALL_STATS_BY_NAME.get(npcName));
	}

	public Optional<ItemStats> getItemStatsById(int npcId)
	{
		return Optional.ofNullable(ALL_STATS.get(npcId));
	}

}
