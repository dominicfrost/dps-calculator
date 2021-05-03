package com.duckblade.osrs.dpscalc;

import com.duckblade.osrs.dpscalc.model.NpcStats;
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

@Singleton
@Slf4j
public class NpcDataManager
{

	private static final Gson GSON = new Gson();

	private final Map<Integer, NpcStats> ALL_STATS;
	private final Map<String, Map<Integer, NpcStats>> ALL_STATS_BY_NAME;

	@Inject
	public NpcDataManager()
	{
		try (InputStream fileStream = getClass().getResourceAsStream("npcs-dps-calc.min.json"); InputStreamReader reader = new InputStreamReader(fileStream))
		{
			ALL_STATS = GSON.fromJson(reader, new TypeToken<HashMap<Integer, NpcStats>>()
			{
			}.getType());
		}
		catch (IOException e)
		{
			log.error("Failed to load NPC data", e);
			throw new IllegalStateException(e);
		}

		ALL_STATS_BY_NAME = new HashMap<>(ALL_STATS.size());
		for (Map.Entry<Integer, NpcStats> entry : ALL_STATS.entrySet())
		{
			if (entry.getValue().getName() != null && entry.getValue().getCombatLevel() != 0)
			{
				ALL_STATS_BY_NAME.computeIfAbsent(entry.getValue().getName(), s -> new HashMap<>(10))
						.put(entry.getValue().getCombatLevel(), entry.getValue());
			}
		}
	}

	public String[] getAllNpcNames()
	{
		return Stream.concat(
				Stream.of(""),
				ALL_STATS_BY_NAME.keySet()
						.stream())
				.sorted()
				.toArray(String[]::new);
	}

	public Map<Integer, NpcStats> getNpcStatsByName(String npcName)
	{
		return ALL_STATS_BY_NAME.get(npcName);
	}

	public NpcStats getNpcStatsById(int npcId)
	{
		return ALL_STATS.get(npcId);
	}

}
