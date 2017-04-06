package ec3.common.item;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import DummyCore.Client.IModelRegisterer;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import ec3.api.ApiCore;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public class ItemGenericEC3 extends Item implements IModelRegisterer {
	public static String[] names = new String[] {
			"combinedMagicalAlloys", //0
			"elementalCore", //1
			"enderScalePlating", //2
			"magicalEssence", //3
			"magicalGoldenOrb", //4
			"magicalIngot", //5
			"magicalWater", //6
			"magicFortifiedPlating", //7
			"magicPurifyedEnderScaleAlloy", //8
			"magicPurifyedGlassAlloy", //9
			"magicPurifyedGoldAlloy", //10
			"magicPurifyedRedstoneAlloy", //11
			"mruShard", //12
			"mruCrystal", //13
			"mruGem", //14
			"mruChunk", //15
			"mruLargeChunk", //16
			"inventoryUpgrade", //17
			"efficencyUpgrade", //18
			"diamondUpgrade", //19
			"crystalDust", //20
			"diamondPlate", //21
			"emeraldPlate", //22
			"eyeOfAbsorbtion", //23
			"fortifiedFrame", //24
			"heatingRod", //25
			"ironSupport", //26
			"magicalScreen", //27
			"matrixLink", //28
			"mruCatcher", //29
			"mruConversionMatrix", //30
			"obsidianPlate", //31
			"sunImbuedGlass", //32
			"worldInteractor", //33
			"magicPlate", //34
			"voidPlating", //35
			"voidCore", //36
			"voidMruReactor", //37
			"palePearl", //38
			"paleIngot", //39
			"gemPale", //40
			"palePlate", //41
			"paleCore", //42
			"mruMagnet", //43
			"mruResonatingCrystal", //44
			"lapisCore", //45
			"fadingDust", //46
			"fadingCrystal", //47
			"mithrilineCrystal", //48
			"mithrilinePlate", //49
			"mithrilineIngot", //50
			"mithrilineDust", //51
			"ackroniteIngot", //52
			"demonicCore", //53
			"demonicPlate", //54
			"windGem", //55
			"computerEngine", //56
			"forceFieldGenerator", //57
			"forceFieldPlating", //58
			"neuronicEnrichedPlating", //59
			"pressureStabiliser", //60
			"repulsionGenerator", //61
			"thrusterEngine", //62
			"visionLense", //63
			"speakerPlate", //64
			"forcefieldCore", //65
			"particleCatcher", //66
			"particleEmitter", //67
			"forceEmitter", //68
			"logicCore", //69
			"additionCore", //70
			"divisionCore", //71
			"multiplicationCore", //72
			"substructionCore", //73
			"forceAbsorber", //74
			"soundManager", //75
			"glitchyCore", //76
			"blazingUpgrade", //77
			"fortuneUpgrade", //78
			"magicPurifyedBlazeAlloy", //79
			"silkyUpgrade", //80
			"voidUpgrade", //81
			"unknown",//fallback
	};

	public ItemGenericEC3() {
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	public ItemStack onItemUseFinish(ItemStack p_77654_1_, World p_77654_2_, EntityLivingBase base) {
		if(base instanceof EntityPlayer) {
			if(!((EntityPlayer)base).capabilities.isCreativeMode)
				--p_77654_1_.stackSize;

			if(!p_77654_2_.isRemote && p_77654_1_.getItemDamage() == 6) {
				int addedEnergy = 0;
				IBaublesItemHandler b = BaublesApi.getBaublesHandler((EntityPlayer)base);
				if(b != null) {
					for(int i = 0; i < b.getSlots(); ++i) {
						ItemStack is = b.getStackInSlot(i);
						if(is != null && is.getItem() != null && is.getItem() instanceof BaublesModifier && is.getItemDamage() == 8)
							addedEnergy = 500;
					}
				}
				int current = ApiCore.getPlayerData(((EntityPlayer)base)).getPlayerUBMRU();
				ApiCore.getPlayerData(((EntityPlayer)base)).modifyUBMRU(current+addedEnergy);
			}
		}

		return p_77654_1_.stackSize <= 0 ? new ItemStack(Items.GLASS_BOTTLE) : p_77654_1_;
	}

	public int getMaxItemUseDuration(ItemStack p_77626_1_) {
		return 32;
	}

	public EnumAction getItemUseAction(ItemStack p_77661_1_) {
		if(p_77661_1_.getItemDamage() == 6)
			return EnumAction.DRINK;
		return super.getItemUseAction(p_77661_1_);
	}

	public static ItemStack getStkByName(String name) {
		List<String> lst = Arrays.asList(names);
		if(lst.contains(name)) {
			ItemStack stk = new ItemStack(ItemsCore.genericItem,1,lst.indexOf(name));
			return stk;
		}
		return null;
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if(itemStackIn.getItemDamage() == 6)
			playerIn.setActiveHand(hand);
		return new ActionResult(EnumActionResult.PASS, itemStackIn);
	}

	public String getUnlocalizedName(ItemStack p_77667_1_) {
		return getUnlocalizedName()+names[Math.min(p_77667_1_.getItemDamage(), names.length-1)];
	}

	public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List<ItemStack> p_150895_3_) {
		for(int i = 0; i < names.length-1; ++i) {
			p_150895_3_.add(new ItemStack(p_150895_1_,1,i));
		}
	}

	@Override
	public void registerModels() {
		for(int i = 0; i < names.length-1; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation("essentialcraft:item/genItem", "type=" + names[i].toLowerCase(Locale.ENGLISH)));
		}
	}
}
