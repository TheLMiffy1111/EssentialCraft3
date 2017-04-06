package ec3.api;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import DummyCore.Utils.DummyData;
import ec3.common.mod.EssentialCraftCore;

public class GunRegistry {

	public static final List<GunMaterial> gunMaterials = new ArrayList<GunMaterial>();
	public static final List<LenseMaterial> lenseMaterials = new ArrayList<LenseMaterial>();
	public static final List<ScopeMaterial> scopeMaterials = new ArrayList<ScopeMaterial>();
	public static final List<ScopeMaterial> scopeMaterialsSniper = new ArrayList<ScopeMaterial>();
	
	public static ScopeMaterial getScopeFromID(String s) {
		for(int i = 0; i < GunRegistry.scopeMaterials.size(); ++i)
		{
			if(GunRegistry.scopeMaterials.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.scopeMaterials.get(i);
			}
		}
		return null;
	}
	
	public static ScopeMaterial getScopeSniperFromID(String s) {
		for(int i = 0; i < GunRegistry.scopeMaterialsSniper.size(); ++i)
		{
			if(GunRegistry.scopeMaterialsSniper.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.scopeMaterialsSniper.get(i);
			}
		}
		return null;
	}
	
	public static LenseMaterial getLenseFromID(String s) {
		for(int i = 0; i < GunRegistry.lenseMaterials.size(); ++i)
		{
			if(GunRegistry.lenseMaterials.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.lenseMaterials.get(i);
			}
		}
		return null;
	}
	
	public static GunMaterial getGunFromID(String s) {
		for(int i = 0; i < GunRegistry.gunMaterials.size(); ++i)
		{
			if(GunRegistry.gunMaterials.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.gunMaterials.get(i);
			}
		}
		return null;
	}
	
	public static class ScopeMaterial
	{
		public String id;
		public ItemStack recipe;
		public boolean sniper;
		public Hashtable<GunType,ArrayList<DummyData>> materialData = new Hashtable<GunType,ArrayList<DummyData>>();
		public Hashtable<String,String> textures = new Hashtable<String,String>();

		public ScopeMaterial(String s, boolean sniper)
		{
			id = s;
			this.sniper = sniper;
		}

		public ScopeMaterial setRecipe(ItemStack is)
		{
			recipe = is;
			return this;
		}
		
		public ScopeMaterial setTextures(String... rl) {
			if(rl.length == 3) {
				textures.put("pistol", rl[0]);
				textures.put("rifle", rl[1]);
				textures.put("sniper", rl[2]);
				
				for(int i = 0; i < 3; i++)
					EssentialCraftCore.proxy.registerTexture(new ResourceLocation(rl[i]));
			}
			return this;
		}
		
		public ScopeMaterial setTexture(String rl) {
			textures.put("sniper", rl);
			
			EssentialCraftCore.proxy.registerTexture(new ResourceLocation(rl));
			return this;
		}

		public ScopeMaterial appendData(String s, float value, GunType gun)
		{
			if(!materialData.containsKey(gun))
			{
				materialData.put(gun, new ArrayList<DummyData>());
			}
			ArrayList<DummyData> d = materialData.get(gun);
			d.add(new DummyData(s,value));
			materialData.put(gun, d);

			return this;
		}

		public ScopeMaterial appendData(String s, float value)
		{
			for(int i = 0; i < GunType.values().length; ++i)
			{
				GunType gun = GunType.values()[i];
				if(!materialData.containsKey(gun))
				{
					materialData.put(gun, new ArrayList<DummyData>());
				}
				ArrayList<DummyData> d = materialData.get(gun);
				d.add(new DummyData(s,value));
				materialData.put(gun, d);
			}
			return this;
		}
		
		public ScopeMaterial register() {
			if(!sniper)
				scopeMaterials.add(this);
			else
				scopeMaterialsSniper.add(this);
			return this;
		}
	}

	public static class LenseMaterial
	{
		public Hashtable<GunType,ArrayList<DummyData>> materialData = new Hashtable<GunType,ArrayList<DummyData>>();
		public String id;
		public ItemStack recipe;
		public Hashtable<String,String> textures = new Hashtable<String,String>();

		public LenseMaterial(String s)
		{
			id = s;
		}

		public LenseMaterial setRecipe(ItemStack is)
		{
			recipe = is;
			return this;
		}
		
		public LenseMaterial setTextures(String... rl) {
			if(rl.length == 4) {
				textures.put("pistol", rl[0]);
				textures.put("rifle", rl[1]);
				textures.put("sniper", rl[2]);
				textures.put("gatling", rl[3]);
				
				for(int i = 0; i < 4; i++)
					EssentialCraftCore.proxy.registerTexture(new ResourceLocation(rl[i]));
			}
			return this;
		}

		public LenseMaterial appendData(String s, float value, GunType gun)
		{
			if(!materialData.containsKey(gun))
			{
				materialData.put(gun, new ArrayList<DummyData>());
			}
			ArrayList<DummyData> d = materialData.get(gun);
			d.add(new DummyData(s,value));
			materialData.put(gun, d);

			return this;
		}

		public LenseMaterial appendData(String s, float value)
		{
			for(int i = 0; i < GunType.values().length; ++i)
			{
				GunType gun = GunType.fromIndex(i);
				if(!materialData.containsKey(gun))
				{
					materialData.put(gun, new ArrayList<DummyData>());
				}
				ArrayList<DummyData> d = materialData.get(gun);
				d.add(new DummyData(s,value));
				materialData.put(gun, d);
			}
			return this;
		}
		
		public LenseMaterial register() {
			lenseMaterials.add(this);
			return this;
		}
	}

	public static class GunMaterial
	{
		public Hashtable<GunType,ArrayList<DummyData>> materialData = new Hashtable<GunType,ArrayList<DummyData>>();
		public String id;
		public ItemStack recipe;
		public Hashtable<String,String> baseTextures = new Hashtable<String,String>();
		public Hashtable<String,String> handleTextures = new Hashtable<String,String>();
		public Hashtable<String,String> deviceTextures = new Hashtable<String,String>();

		public GunMaterial(String s)
		{
			id = s;
		}

		public GunMaterial setRecipe(ItemStack is)
		{
			recipe = is;
			return this;
		}
		
		public GunMaterial setTextures(String... rl) {
			if(rl.length == 12) {
				baseTextures.put("pistol", rl[0]);
				baseTextures.put("rifle", rl[1]);
				baseTextures.put("sniper", rl[2]);
				baseTextures.put("gatling", rl[3]);
				
				handleTextures.put("pistol", rl[4]);
				handleTextures.put("rifle", rl[5]);
				handleTextures.put("sniper", rl[6]);
				handleTextures.put("gatling", rl[7]);
				
				deviceTextures.put("pistol", rl[8]);
				deviceTextures.put("rifle", rl[9]);
				deviceTextures.put("sniper", rl[10]);
				deviceTextures.put("gatling", rl[11]);

				for(int i = 0; i < 12; i++)
					EssentialCraftCore.proxy.registerTexture(new ResourceLocation(rl[i]));
			}
			return this;
		}

		public GunMaterial appendData(String s, float value, GunType gun)
		{
			if(!materialData.containsKey(gun))
			{
				materialData.put(gun, new ArrayList<DummyData>());
			}
			ArrayList<DummyData> d = materialData.get(gun);
			d.add(new DummyData(s,value));
			materialData.put(gun, d);

			return this;
		}

		public GunMaterial appendData(String s, float value)
		{
			for(int i = 0; i < GunType.values().length; ++i)
			{
				GunType gun = GunType.values()[i];
				if(!materialData.containsKey(gun))
				{
					materialData.put(gun, new ArrayList<DummyData>());
				}
				ArrayList<DummyData> d = materialData.get(gun);
				d.add(new DummyData(s,value));
				materialData.put(gun, d);
			}
			return this;
		}
		
		public GunMaterial register() {
			gunMaterials.add(this);
			return this;
		}
	}

	public static enum GunType implements IStringSerializable
	{
		PISTOL(0,"pistol"),
		RIFLE(1,"rifle"),
		SNIPER(2,"sniper"),
		GATLING(3,"gatling");

		private int index;
		private String name;

		private GunType(int i, String s) {
			index = i;
			name = s;
		}

		public String getName() {
			return name;
		}

		public String toString() {
			return name;
		}

		public int getIndex() {
			return index;
		}

		public static GunType fromIndex(int i) {
			return values()[i];
		}
	}
}
