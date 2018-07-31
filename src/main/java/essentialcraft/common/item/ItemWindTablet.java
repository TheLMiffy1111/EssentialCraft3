package essentialcraft.common.item;

import java.util.List;

import DummyCore.Client.IModelRegisterer;
import DummyCore.Utils.MathUtils;
import essentialcraft.common.block.BlocksCore;
import essentialcraft.utils.common.ECUtils;
import essentialcraft.utils.common.WindRelations;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWindTablet extends ItemMRUGeneric implements IModelRegisterer {

	//24
	public static String[] windMessages = {
			"A wind blow rotates around you...",//0
			"The wind howls...",//1
			"The wind ruffles your hair...",//2
			"The wind blows around your fingers...",//3
			"The wind says something...",//4
			"The wind makes you sneeze!",//5
			"You hear something similar to laugh...",//6
			"The wind rotates around your legs...",//7
			"The wind tickles you...",//8
			"The wind stops all other sounds...",//9
			"The wind whispers 'Owethanna'...",//10
			"The wind whispers your name...",//11
			"The wind brings a very nostalgic smell...",//12
			"The wind creates a miniature tornado...",//13
			"The wind thows some leaves around...",//14
			"The wind says 'Owethanna Else '...",//15
			"The wind pushes you upwards...",//16
			"The wind rotates very fast around you...",//17
			"The wind goes into your lungs...",//18
			"You feel very powerfull!",//19
			"You fly up using the wind!",//20
			"You start seeing other worlds!",//21
			"You and the wind laugh...",//22
			"The wind and you shout:"//23
	};

	public ItemWindTablet() {
		super();
		this.maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(ECUtils.playerUseMRU(player, player.getHeldItem(hand), 500)) {
			int currentWindRev = WindRelations.getPlayerWindRelations(player);
			int maxWindRev = 3500;
			String windName = "Owethanna Else Hugaida";
			int revPos = MathUtils.pixelatedTextureSize(currentWindRev, maxWindRev, windName.length());
			if(revPos >= 22) {
				if(!worldIn.isRemote) {
					Block b = worldIn.getBlockState(pos).getBlock();

					if(b == Blocks.PORTAL) {
						int i = 0;
						while(worldIn.getBlockState(pos.add(i, 0, 0)).getBlock() == Blocks.PORTAL) {
							int j = 0;
							while(worldIn.getBlockState(pos.add(i, j, 0)).getBlock() == Blocks.PORTAL) {
								int k = 0;
								while(worldIn.getBlockState(pos.add(i, j, k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(i, j, k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(i, j, k)));
									worldIn.setBlockState(pos.add(i, j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								k = 1;
								while(worldIn.getBlockState(pos.add(i, j, -k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(i, j, -k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(i, j, -k)));
									worldIn.setBlockState(pos.add(i, j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								++j;
							}
							j = 1;
							while(worldIn.getBlockState(pos.add(i, -j, 0)).getBlock() == Blocks.PORTAL) {
								int k = 0;
								while(worldIn.getBlockState(pos.add(i, -j, k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(i, -j, k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(i, -j, k)));
									worldIn.setBlockState(pos.add(i, -j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								k = 1;
								while(worldIn.getBlockState(pos.add(i, -j, -k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(i, -j, -k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(i, -j, -k)));
									worldIn.setBlockState(pos.add(i, -j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								++j;
							}
							++i;
						}
						i = 1;
						while(worldIn.getBlockState(pos.add(-i, 0, 0)).getBlock() == Blocks.PORTAL) {
							int j = 0;
							while(worldIn.getBlockState(pos.add(-i, j, 0)).getBlock() == Blocks.PORTAL) {
								int k = 0;
								while(worldIn.getBlockState(pos.add(-i, j, k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(-i, j, k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(-i, j, k)));
									worldIn.setBlockState(pos.add(-i, j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								k = 1;
								while(worldIn.getBlockState(pos.add(-i, j, -k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(-i, j, -k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(-i, j, -k)));
									worldIn.setBlockState(pos.add(-i, j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								++j;
							}
							j = 1;
							while(worldIn.getBlockState(pos.add(-i, -j, 0)).getBlock() == Blocks.PORTAL) {
								int k = 0;
								while(worldIn.getBlockState(pos.add(-i, -j, k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(-i, -j, k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(-i, -j, k)));
									worldIn.setBlockState(pos.add(-i, -j, k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								k = 1;
								while(worldIn.getBlockState(pos.add(-i, -j, -k)).getBlock() == Blocks.PORTAL) {
									int metadata = worldIn.getBlockState(pos.add(-i, -j, -k)).getBlock().getMetaFromState(worldIn.getBlockState(pos.add(-i, -j, -k)));
									worldIn.setBlockState(pos.add(-i, -j, -k), BlocksCore.portal.getStateFromMeta(metadata), 2);
									++k;
								}
								++j;
							}
							++i;
						}
					}
					worldIn.playSound(player, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 10, 0.01F);
				}
				return EnumActionResult.SUCCESS;
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World player, List<String> list, ITooltipFlag par4) {
		super.addInformation(stack, player, list, par4);
		int currentWindRev = WindRelations.getPlayerWindRelations(Minecraft.getMinecraft().player);
		int maxWindRev = 3500;
		String windName = "Owethanna Else Hugaida";
		String hidden = "??????????????????????";
		int revPos = MathUtils.pixelatedTextureSize(currentWindRev, maxWindRev, windName.length());
		list.add("Wind Name:");
		list.add(windName.substring(0, revPos)+hidden.substring(revPos));
	}

	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation("essentialcraft:item/windtablet", "inventory"));
	}
}
