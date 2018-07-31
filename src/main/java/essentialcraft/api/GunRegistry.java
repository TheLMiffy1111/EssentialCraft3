package essentialcraft.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DummyCore.Utils.DummyData;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;

public class GunRegistry {

	public static final List<GunMaterial> GUN_MATERIALS = new ArrayList<GunMaterial>();
	public static final List<LenseMaterial> LENSE_MATERIALS = new ArrayList<LenseMaterial>();
	public static final List<ScopeMaterial> SCOPE_MATERIALS = new ArrayList<ScopeMaterial>();
	public static final List<ScopeMaterial> SCOPE_MATERIALS_SNIPER = new ArrayList<ScopeMaterial>();

	public static ScopeMaterial getScopeFromID(String s) {
		for(int i = 0; i < GunRegistry.SCOPE_MATERIALS.size(); ++i)
		{
			if(GunRegistry.SCOPE_MATERIALS.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.SCOPE_MATERIALS.get(i);
			}
		}
		return null;
	}

	public static ScopeMaterial getScopeSniperFromID(String s) {
		for(int i = 0; i < GunRegistry.SCOPE_MATERIALS_SNIPER.size(); ++i)
		{
			if(GunRegistry.SCOPE_MATERIALS_SNIPER.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.SCOPE_MATERIALS_SNIPER.get(i);
			}
		}
		return null;
	}

	public static LenseMaterial getLenseFromID(String s) {
		for(int i = 0; i < GunRegistry.LENSE_MATERIALS.size(); ++i)
		{
			if(GunRegistry.LENSE_MATERIALS.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.LENSE_MATERIALS.get(i);
			}
		}
		return null;
	}

	public static GunMaterial getGunFromID(String s) {
		for(int i = 0; i < GunRegistry.GUN_MATERIALS.size(); ++i)
		{
			if(GunRegistry.GUN_MATERIALS.get(i).id.equalsIgnoreCase(s))
			{
				return GunRegistry.GUN_MATERIALS.get(i);
			}
		}
		return null;
	}

	public static class ScopeMaterial
	{
		public String id;
		public ItemStack recipe = ItemStack.EMPTY;
		public boolean sniper;
		public HashMap<GunType,ArrayList<DummyData>> materialData = new HashMap<GunType,ArrayList<DummyData>>();
		public HashMap<String,String> textures = new HashMap<String,String>();

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
					ApiCore.registerTexture(new ResourceLocation(rl[i]));
			}
			return this;
		}

		public ScopeMaterial setTexture(String rl) {
			textures.put("sniper", rl);

			ApiCore.registerTexture(new ResourceLocation(rl));
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
				SCOPE_MATERIALS.add(this);
			else
				SCOPE_MATERIALS_SNIPER.add(this);
			return this;
		}
	}

	public static class LenseMaterial
	{
		public HashMap<GunType,ArrayList<DummyData>> materialData = new HashMap<GunType,ArrayList<DummyData>>();
		public String id;
		public ItemStack recipe = ItemStack.EMPTY;
		public HashMap<String,String> textures = new HashMap<String,String>();

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
					ApiCore.registerTexture(new ResourceLocation(rl[i]));
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
			LENSE_MATERIALS.add(this);
			return this;
		}
	}

	public static class GunMaterial
	{
		public HashMap<GunType,ArrayList<DummyData>> materialData = new HashMap<GunType,ArrayList<DummyData>>();
		public String id;
		public ItemStack recipe = ItemStack.EMPTY;
		public HashMap<String,String> baseTextures = new HashMap<String,String>();
		public HashMap<String,String> handleTextures = new HashMap<String,String>();
		public HashMap<String,String> deviceTextures = new HashMap<String,String>();

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
					ApiCore.registerTexture(new ResourceLocation(rl[i]));
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
			GUN_MATERIALS.add(this);
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

		@Override
		public String getName() {
			return name;
		}

		@Override
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
