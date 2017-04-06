package ec3.common.registry;

import net.minecraft.item.ItemStack;
import ec3.api.GunRegistry.GunMaterial;
import ec3.api.GunRegistry.LenseMaterial;
import ec3.api.GunRegistry.ScopeMaterial;
import ec3.common.item.ItemGenericEC3;
import ec3.common.item.ItemsCore;

public class GunInitialization {
	
	public static final String[] defaultGunMaterials = new String[] {"mru", "pale", "mithriline", "void", "demonic"};
	public static final String[] defaultSniperScopeMaterials = new String[] {"mru", "pale", "mithriline", "void", "demonic"};
	public static final String[] defaultLenseMaterials = new String[] {"chaos", "frozen", "pure", "shade"};
	public static final String[] defaultScopeMaterials = new String[] {"mru"};
	
	public static void register()
	{
		new GunMaterial("mru").setRecipe(ItemGenericEC3.getStkByName("magicalIngot"))
		.appendData("durability", 120)
		.appendData("damage", 8)
		.appendData("reload", 5)
		.appendData("knockback", 6)
		.appendData("spread", 3)
		.appendData("speed", 3)
		.appendData("shots", 2)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/base/mru",
				"essentialcraft:items/guns/rifle/base/mru",
				"essentialcraft:items/guns/sniper/base/mru",
				"essentialcraft:items/guns/gatling/base/mru",
				
				"essentialcraft:items/guns/pistol/handle/mru",
				"essentialcraft:items/guns/rifle/handle/mru",
				"essentialcraft:items/guns/sniper/handle/mru",
				"essentialcraft:items/guns/gatling/handle/mru",
				
				"essentialcraft:items/guns/pistol/device/mru",
				"essentialcraft:items/guns/rifle/device/mru",
				"essentialcraft:items/guns/sniper/device/mru",
				"essentialcraft:items/guns/gatling/device/mru"
		})
		.register()
		;
		
		new GunMaterial("pale").setRecipe(ItemGenericEC3.getStkByName("paleIngot"))
		.appendData("durability", 160)
		.appendData("damage", 11)
		.appendData("reload", 8)
		.appendData("knockback", 2)
		.appendData("spread", 6)
		.appendData("speed", 1)
		.appendData("shots", 2)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/base/pale",
				"essentialcraft:items/guns/rifle/base/pale",
				"essentialcraft:items/guns/sniper/base/pale",
				"essentialcraft:items/guns/gatling/base/pale",
				
				"essentialcraft:items/guns/pistol/handle/pale",
				"essentialcraft:items/guns/rifle/handle/pale",
				"essentialcraft:items/guns/sniper/handle/pale",
				"essentialcraft:items/guns/gatling/handle/pale",
				
				"essentialcraft:items/guns/pistol/device/pale",
				"essentialcraft:items/guns/rifle/device/pale",
				"essentialcraft:items/guns/sniper/device/pale",
				"essentialcraft:items/guns/gatling/device/pale"
		})
		.register()
		;
		
		new GunMaterial("mithriline").setRecipe(ItemGenericEC3.getStkByName("mithrilineIngot"))
		.appendData("durability", 300)
		.appendData("damage", 14)
		.appendData("reload", 12)
		.appendData("knockback", 12)
		.appendData("spread", 1)
		.appendData("speed", 6)
		.appendData("shots", 12)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/base/mithriline",
				"essentialcraft:items/guns/rifle/base/mithriline",
				"essentialcraft:items/guns/sniper/base/mithriline",
				"essentialcraft:items/guns/gatling/base/mithriline",
				
				"essentialcraft:items/guns/pistol/handle/mithriline",
				"essentialcraft:items/guns/rifle/handle/mithriline",
				"essentialcraft:items/guns/sniper/handle/mithriline",
				"essentialcraft:items/guns/gatling/handle/mithriline",
				
				"essentialcraft:items/guns/pistol/device/mithriline",
				"essentialcraft:items/guns/rifle/device/mithriline",
				"essentialcraft:items/guns/sniper/device/mithriline",
				"essentialcraft:items/guns/gatling/device/mithriline"
		})
		.register()
		;
		
		new GunMaterial("void").setRecipe(ItemGenericEC3.getStkByName("voidPlating"))
		.appendData("durability", 1200)
		.appendData("damage", 18)
		.appendData("reload", 4)
		.appendData("knockback", 2)
		.appendData("spread", 6)
		.appendData("speed", 2)
		.appendData("shots", 8)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/base/void",
				"essentialcraft:items/guns/rifle/base/void",
				"essentialcraft:items/guns/sniper/base/void",
				"essentialcraft:items/guns/gatling/base/void",
				
				"essentialcraft:items/guns/pistol/handle/void",
				"essentialcraft:items/guns/rifle/handle/void",
				"essentialcraft:items/guns/sniper/handle/void",
				"essentialcraft:items/guns/gatling/handle/void",
				
				"essentialcraft:items/guns/pistol/device/void",
				"essentialcraft:items/guns/rifle/device/void",
				"essentialcraft:items/guns/sniper/device/void",
				"essentialcraft:items/guns/gatling/device/void"
		})
		.register()
		;
		
		new GunMaterial("demonic").setRecipe(ItemGenericEC3.getStkByName("ackroniteIngot"))
		.appendData("durability", 3000)
		.appendData("damage", 26)
		.appendData("reload", 2)
		.appendData("knockback", 0)
		.appendData("spread", 1)
		.appendData("speed", 1)
		.appendData("shots", 20)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/base/demonic",
				"essentialcraft:items/guns/rifle/base/demonic",
				"essentialcraft:items/guns/sniper/base/demonic",
				"essentialcraft:items/guns/gatling/base/demonic",
				
				"essentialcraft:items/guns/pistol/handle/demonic",
				"essentialcraft:items/guns/rifle/handle/demonic",
				"essentialcraft:items/guns/sniper/handle/demonic",
				"essentialcraft:items/guns/gatling/handle/demonic",
				
				"essentialcraft:items/guns/pistol/device/demonic",
				"essentialcraft:items/guns/rifle/device/demonic",
				"essentialcraft:items/guns/sniper/device/demonic",
				"essentialcraft:items/guns/gatling/device/demonic"
		})
		.register()
		;
		
		
		new ScopeMaterial("mru",false).setRecipe(ItemGenericEC3.getStkByName("magicPurifyedGlassAlloy"))
		.appendData("scope.zoom", 1.2F)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/scope/mru",
				"essentialcraft:items/guns/rifle/scope/mru",
				"essentialcraft:items/guns/sniper/scope/mru"
		})
		.register();
		
		new ScopeMaterial("mru",true).setRecipe(ItemGenericEC3.getStkByName("magicalIngot"))
		.appendData("scope.zoom", 3F)
		.setTexture("essentialcraft:items/guns/sniper/scope/mru")
		.register();
		
		new ScopeMaterial("pale",true).setRecipe(ItemGenericEC3.getStkByName("paleIngot"))
		.appendData("scope.zoom", 5F)
		.setTexture("essentialcraft:items/guns/sniper/scope/pale")
		.register();
		
		new ScopeMaterial("mithriline",true).setRecipe(ItemGenericEC3.getStkByName("mithrilineIngot"))
		.appendData("scope.zoom", 7F)
		.setTexture("essentialcraft:items/guns/sniper/scope/mithriline")
		.register();
		
		new ScopeMaterial("void",true).setRecipe(ItemGenericEC3.getStkByName("voidPlating"))
		.appendData("scope.zoom", 12F)
		.setTexture("essentialcraft:items/guns/sniper/scope/void")
		.register();
		
		new ScopeMaterial("demonic",true).setRecipe(ItemGenericEC3.getStkByName("ackroniteIngot"))
		.appendData("scope.zoom", 16F)
		.setTexture("essentialcraft:items/guns/sniper/scope/demonic")
		.register();
		
		
		new LenseMaterial("chaos").setRecipe(new ItemStack(ItemsCore.matrixProj,1,1))
		.appendData("balance", 1F)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/lense/chaos",
				"essentialcraft:items/guns/rifle/lense/chaos",
				"essentialcraft:items/guns/sniper/lense/chaos",
				"essentialcraft:items/guns/gatling/lense/chaos"
		})
		.register();
		
		new LenseMaterial("frozen").setRecipe(new ItemStack(ItemsCore.matrixProj,1,2))
		.appendData("balance", 2F)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/lense/frozen",
				"essentialcraft:items/guns/rifle/lense/frozen",
				"essentialcraft:items/guns/sniper/lense/frozen",
				"essentialcraft:items/guns/gatling/lense/frozen"
		})
		.register();
		
		new LenseMaterial("pure").setRecipe(new ItemStack(ItemsCore.matrixProj,1,3))
		.appendData("balance", 3F)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/lense/pure",
				"essentialcraft:items/guns/rifle/lense/pure",
				"essentialcraft:items/guns/sniper/lense/pure",
				"essentialcraft:items/guns/gatling/lense/pure"
		})
		.register();
		
		new LenseMaterial("shade").setRecipe(new ItemStack(ItemsCore.matrixProj,1,4))
		.appendData("balance", 4F)
		.setTextures(new String[] {
				"essentialcraft:items/guns/pistol/lense/shade",
				"essentialcraft:items/guns/rifle/lense/shade",
				"essentialcraft:items/guns/sniper/lense/shade",
				"essentialcraft:items/guns/gatling/lense/shade"
		})
		.register();
	}
}
