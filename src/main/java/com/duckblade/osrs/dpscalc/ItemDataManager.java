package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.model.ItemStats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import static net.runelite.api.ItemID.*;

@Slf4j
@Singleton
public class ItemDataManager
{

	private static final Gson GSON = new Gson();

	private final Map<Integer, ItemStats> ITEMS_BY_ID;
	private final Map<String, ItemStats> ITEMS_BY_NAME; // precompute since used in dropdowns

	private final List<ItemStats> DARTS;

	@Inject
	public ItemDataManager()
	{
		try (InputStream fileStream = getClass().getResourceAsStream("items-dps-calc.min.json"); InputStreamReader reader = new InputStreamReader(fileStream))
		{
			ITEMS_BY_ID = GSON.fromJson(reader, new TypeToken<HashMap<Integer, ItemStats>>()
			{
			}.getType());
		}
		catch (IOException e)
		{
			log.error("Failed to load item data", e);
			throw new IllegalStateException(e);
		}

		ITEMS_BY_NAME = new HashMap<>(ITEMS_BY_ID.size());
		for (Map.Entry<Integer, ItemStats> entry : ITEMS_BY_ID.entrySet())
		{
			ItemStats stats = entry.getValue();
			stats.setItemId(entry.getKey());
			if (stats.getName() != null && stats.getEquipStats() != null)
			{
				ITEMS_BY_NAME.put(entry.getValue().getName(), entry.getValue());
			}
		}
		
		// for tbp selection
		DARTS = IntStream.of(DRAGON_DART, RUNE_DART, ADAMANT_DART, MITHRIL_DART, BLACK_DART, STEEL_DART, IRON_DART, BRONZE_DART)
				.mapToObj(ITEMS_BY_ID::get)
				.collect(Collectors.toList());
	}
	
	public List<ItemStats> getAllDarts()
	{
		return Collections.unmodifiableList(DARTS);
	}
	
	public String[] getAllItemNames(final int targetSlot)
	{
		return Stream.concat(
				Stream.of(""),
				ITEMS_BY_NAME.values()
						.stream()
						.filter(is -> is.getEquipStats().getSlot() == targetSlot)
						.map(ItemStats::getName)
						.sorted()
		).toArray(i -> new String[i + 1]);
	}

	public ItemStats getItemStatsByName(String npcName)
	{
		return ITEMS_BY_NAME.get(npcName);
	}

	public ItemStats getItemStatsById(int npcId)
	{
		return ITEMS_BY_ID.get(npcId);
	}

}
