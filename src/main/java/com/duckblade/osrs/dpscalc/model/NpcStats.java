package com.duckblade.osrs.dpscalc.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // for npcstatspanel
@NoArgsConstructor // for gson
public class NpcStats
{

	String name;

	@SerializedName("hitpoints")
	int levelHp = 1;
	@SerializedName("att")
	int levelAttack = 1;
	@SerializedName("str")
	int levelStrength = 1;
	@SerializedName("def")
	int levelDefense = 1;
	@SerializedName("mage")
	int levelMagic = 1;
	@SerializedName("range")
	int levelRanged = 1;

	@SerializedName("dstab")
	int bonusDefenseStab = 0;
	@SerializedName("dslash")
	int bonusDefenseSlash = 0;
	@SerializedName("dcrush")
	int bonusDefenseCrush = 0;
	@SerializedName("dmagic")
	int bonusDefenseMagic = 0;
	@SerializedName("drange")
	int bonusDefenseRange = 0;

	@SerializedName("combat")
	int combatLevel = 0;

	boolean isDemon;
	boolean isDragon;
	boolean isKalphite;
	boolean isLeafy;
	boolean isUndead;
	boolean isVampyre;
	boolean isXerician;

}
