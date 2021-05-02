package com.duckblade.osrs.dpscalc.model;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor // for gson
public class ItemStats
{

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class EquipStats
	{
		@SerializedName("astab")
		private int accuracyStab;
		@SerializedName("aslash")
		private int accuracySlash;
		@SerializedName("acrush")
		private int accuracyCrush;
		@SerializedName("amagic")
		private int accuracyMagic;
		@SerializedName("arange")
		private int accuracyRanged;

		@SerializedName("str")
		private int strengthMelee;
		@SerializedName("rstr")
		private int strengthRanged;
		@SerializedName("mdmg")
		private int strengthMagic;

		private int prayer;
		private int slot;
		
		@SerializedName(value = "speed", alternate = {"aspeed"})
		private int speed;
	}

	private int itemId;
	private String name;
	private float weight;
	
	@SerializedName("equipment")
	private EquipStats equipStats;
	
	private WeaponType weaponType = WeaponType.UNARMED;

}
