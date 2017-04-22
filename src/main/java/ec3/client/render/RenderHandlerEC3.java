package ec3.client.render;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.lwjgl.opengl.GL11;

import DummyCore.Client.AdvancedModelLoader;
import DummyCore.Client.IModelCustom;
import DummyCore.Client.TextureUtils;
import DummyCore.Utils.Coord3D;
import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.MathUtils;
import DummyCore.Utils.MiscUtils;
import DummyCore.Utils.TessellatorWrapper;
import baubles.api.BaublesApi;
import ec3.api.WindImbueRecipe;
import ec3.client.model.ModelFancyMimic;
import ec3.client.model.ModelGunHandler;
import ec3.client.model.ModelMimic;
import ec3.client.model.ModelRightClicker;
import ec3.client.render.item.ArmorRenderer;
import ec3.client.render.item.RenderColdDistillatorAsItem;
import ec3.client.render.item.RenderCrystalControllerAsItem;
import ec3.client.render.item.RenderCrystalExtractorAsItem;
import ec3.client.render.item.RenderDarknessObeliskAsItem;
import ec3.client.render.item.RenderElementalCrystalAsItem;
import ec3.client.render.item.RenderEnderGeneratorAsItem;
import ec3.client.render.item.RenderMagicalRepairerAsItem;
import ec3.client.render.item.RenderMithrilineCrystalAsItem;
import ec3.common.block.BlockFancy.FancyBlockType;
import ec3.common.block.BlockWindRune;
import ec3.common.block.BlocksCore;
import ec3.common.item.ItemBoundGem;
import ec3.common.item.ItemComputerArmor;
import ec3.common.item.ItemComputerBoard;
import ec3.common.item.ItemGun;
import ec3.common.item.ItemInventoryGem;
import ec3.common.item.ItemMagicalBuilder;
import ec3.common.item.ItemOrbitalRemote;
import ec3.common.item.ItemsCore;
import ec3.common.mod.EssentialCraftCore;
import ec3.common.registry.PotionRegistry;
import ec3.common.registry.SoundRegistry;
import ec3.common.tile.TileWindRune;
import ec3.proxy.ClientProxy;
import ec3.utils.common.ECUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityBeaconRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RenderHandlerEC3 {

	public static final ResourceLocation iconsEC = new ResourceLocation("essentialcraft","textures/special/icons.png");
	public static final ResourceLocation whitebox = new ResourceLocation("essentialcraft","textures/special/whitebox.png");
	public static final Minecraft mc = Minecraft.getMinecraft();

	public static boolean isParadoxActive;
	public static int currentParadoxTicks;
	public static int paradoxID;

	public static Coord3D explosion;

	public static IRenderHandler skyRenderer;

	public static boolean isMouseInverted;
	public static boolean isSprintKeyDown;

	public static double renderPartialTicksCheck = 0;

	public static boolean isNightVisionKeyDown;
	public static boolean isNightVisionActive;

	public static final IModelCustom board = AdvancedModelLoader.loadModel(new ResourceLocation("essentialcraft","models/item/board.obj"));
	public static final ResourceLocation boardTextures = new ResourceLocation("essentialcraft","textures/models/board.png");

	public static Hashtable<IInventory, Hashtable<Integer,List<EnumFacing>>> slotsTable = new Hashtable<IInventory, Hashtable<Integer, List<EnumFacing>>>();

	public void renderParadox()
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int k = scaledresolution.getScaledWidth();
		int l = scaledresolution.getScaledHeight();
		if(currentParadoxTicks >= 190)
		{
			renderImage(whitebox, k, l, 1,0,0,0);

		}
		if(paradoxID == 0)
		{
			if(currentParadoxTicks == 199)
			{
				mc.player.getEntityWorld().playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, 100, 0.01F, false);
			}
			if(currentParadoxTicks == 199)
			{
				MiscUtils.setShaders(5);
			}
			if(currentParadoxTicks <= 10)
			{
				MiscUtils.setShaders(-1);
				renderImage(whitebox, k, l, 1,1,1,1);
			}
			if(currentParadoxTicks >= 10)
				for(int i = 0; i < 20; ++i)
				{
					mc.world.spawnParticle(EnumParticleTypes.REDSTONE, mc.player.posX+MathUtils.randomDouble(mc.world.rand)*16, mc.player.posY+MathUtils.randomDouble(mc.world.rand)*16, mc.player.posZ+MathUtils.randomDouble(mc.world.rand)*16, -1, 0, 0);
				}
		}
		if(paradoxID == 1)
		{
			if(currentParadoxTicks >= 190)
			{
				renderImage(whitebox, k, l, 1,0,0,0);
			}
			if(currentParadoxTicks == 190)
			{
				MiscUtils.setShaders(16);
				World w = mc.world;
				WorldProvider prov = w.provider;
				if(!(prov.getSkyRenderer() instanceof RenderSkyParadox_1))
				{
					skyRenderer = prov.getSkyRenderer();
					prov.setSkyRenderer(new RenderSkyParadox_1());
				}
			}
			if(currentParadoxTicks <= 10)
			{
				MiscUtils.setShaders(-1);
				renderImage(whitebox, k, l, 1,1,1,1);
				World w = mc.world;
				WorldProvider prov = w.provider;
				prov.setSkyRenderer(skyRenderer);
			}
		}
		if(paradoxID == 2)
		{
			if(currentParadoxTicks >= 190)
			{
				renderImage(whitebox, k, l, 1,0,0,0);
			}
			if(currentParadoxTicks == 190)
			{
				MiscUtils.setShaders(12);
			}
			if(currentParadoxTicks < 190 && currentParadoxTicks > 10)
			{
				if(explosion == null)
				{
					explosion = new Coord3D(mc.player.posX+MathUtils.randomDouble(mc.world.rand)*32, mc.player.posY+32, mc.player.posZ+MathUtils.randomDouble(mc.world.rand)*32);

				}
				else
				{
					mc.world.playSound(explosion.x, explosion.y, explosion.z, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 3, 0.1F, true);
					mc.world.createExplosion(null, explosion.x, explosion.y, explosion.z, 5, false);
					explosion.y -= 0.5F;
					if(explosion.y < mc.player.posY-10)
					{
						explosion = null;
					}
				}
			}
			if(currentParadoxTicks <= 10)
			{
				MiscUtils.setShaders(-1);
				renderImage(whitebox, k, l, 1,1,1,1);
			}
		}
		if(paradoxID == 3)
		{
			if(currentParadoxTicks >= 190)
			{
				renderImage(whitebox, k, l, 1,0,0,0);
			}
			if(currentParadoxTicks == 190)
			{
				MiscUtils.setShaders(8);
			}
			mc.player.motionY += 0.04F;
			if(currentParadoxTicks <= 10)
			{
				MiscUtils.setShaders(-1);
				renderImage(whitebox, k, l, 1,1,1,1);
			}
		}
	}

	public static IInventory getInventoryFromContainer(GuiContainer gc)
	{
		for(int i = 0; i < gc.inventorySlots.inventorySlots.size(); ++i) {
			Slot slt = (Slot)gc.inventorySlots.inventorySlots.get(i);
			if(slt != null)
				return slt.inventory;
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderLivingSpecials(RenderLivingEvent.Specials.Post event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer p = (EntityPlayer)event.getEntity();
			if(BaublesApi.getBaublesHandler(p) != null &&
					BaublesApi.getBaublesHandler(p).getStackInSlot(3) != null &&
					BaublesApi.getBaublesHandler(p).getStackInSlot(3).getItem() instanceof ItemComputerBoard &&
					p.capabilities.isFlying) {

				GlStateManager.pushMatrix();

				GlStateManager.rotate(180, 1, 0, 0);
				GlStateManager.rotate(p.rotationYaw-90, 0, 1, 0);

				GlStateManager.translate(0, 0, 0);

				GlStateManager.scale(0.8F, 0.8F, 0.8F);

				Minecraft.getMinecraft().renderEngine.bindTexture(boardTextures);
				board.renderAll();

				GlStateManager.popMatrix();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderFog(EntityViewRenderEvent.RenderFogEvent evt) {
		EntityPlayerSP p = mc.player;
		ItemStack is = p.getHeldItemMainhand();
		if(is != null) {
			if(is.getItem() instanceof ItemOrbitalRemote) {
				float f = 1;
				double d0 = p.prevPosX + (p.posX - p.prevPosX) * (double)f;
				double d1 = p.prevPosY + (p.posY - p.prevPosY) * (double)f + (double)p.getEyeHeight();
				double d2 = p.prevPosZ + (p.posZ - p.prevPosZ) * (double)f;

				Vec3d lookVec = new Vec3d(d0, d1, d2);
				float f1 = p.prevRotationPitch + (p.rotationPitch - p.prevRotationPitch);
				float f2 = p.prevRotationYaw + (p.rotationYaw - p.prevRotationYaw);
				float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
				float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
				float f5 = -MathHelper.cos(-f1 * 0.017453292F);
				float f6 = MathHelper.sin(-f1 * 0.017453292F);
				float f7 = f4 * f5;
				float f8 = f3 * f5;
				double d3 = 32.0D;
				Vec3d distanced = lookVec.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
				RayTraceResult mop = p.getEntityWorld().rayTraceBlocks(lookVec, distanced, true, false, false);

				if(mop != null && mop.typeOfHit == Type.BLOCK) {
					GlStateManager.pushMatrix();
					GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
					GlStateManager.disableFog();
					DrawUtils.bindTexture("minecraft", "textures/entity/beacon_beam.png");
					GlStateManager.translate(mop.getBlockPos().getX()-TileEntityRendererDispatcher.staticPlayerX,mop.getBlockPos().getY()+1-TileEntityRendererDispatcher.staticPlayerY, mop.getBlockPos().getZ()-TileEntityRendererDispatcher.staticPlayerZ);
					TileEntityBeaconRenderer.renderBeamSegment(0, 0, 0, evt.getRenderPartialTicks(), 1, p.getEntityWorld().getTotalWorldTime(), 0, 255-mop.getBlockPos().getY(), new float[] {1,0,1}, 0.2D, 0.5D);
					GlStateManager.enableFog();
					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderBlockOverlays(RenderWorldLastEvent evt) {
		EntityPlayerSP p = mc.player;
		double doubleX = p.lastTickPosX + (p.posX - p.lastTickPosX) * evt.getPartialTicks();
		double doubleY = p.lastTickPosY + (p.posY - p.lastTickPosY) * evt.getPartialTicks();
		double doubleZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * evt.getPartialTicks();

		if(ItemInventoryGem.currentlyClicked != null) {
			Coord3D c = ItemInventoryGem.currentlyClicked;
			float mx = c.x;
			float my = c.y;
			float mz = c.z;

			double dist = p.getDistance(mx+0.5D, my+0.5D, mz+0.5D);

			if(dist > 24)
				return;

			GlStateManager.pushMatrix();

			boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
			GlStateManager.disableDepth();
			boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glColor4d(1, 0, 1, (double)ItemInventoryGem.clickTicks/100D);
			GlStateManager.glLineWidth(3);
			GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

			GlStateManager.glBegin(GL11.GL_LINES);

			GlStateManager.glVertex3f(mx, my, mz);
			GlStateManager.glVertex3f(mx+1, my, mz);
			GlStateManager.glVertex3f(mx, my, mz);
			GlStateManager.glVertex3f(mx, my, mz+1);
			GlStateManager.glVertex3f(mx+1, my, mz+1);
			GlStateManager.glVertex3f(mx, my, mz+1);
			GlStateManager.glVertex3f(mx+1, my, mz+1);
			GlStateManager.glVertex3f(mx+1, my, mz);

			GlStateManager.glVertex3f(mx, my, mz);
			GlStateManager.glVertex3f(mx, my+1, mz);
			GlStateManager.glVertex3f(mx+1, my, mz+1);
			GlStateManager.glVertex3f(mx+1, my+1, mz+1);
			GlStateManager.glVertex3f(mx, my, mz+1);
			GlStateManager.glVertex3f(mx, my+1, mz+1);
			GlStateManager.glVertex3f(mx+1, my, mz);
			GlStateManager.glVertex3f(mx+1, my+1, mz);

			GlStateManager.glVertex3f(mx, my+1, mz);
			GlStateManager.glVertex3f(mx+1, my+1, mz);
			GlStateManager.glVertex3f(mx, my+1, mz);
			GlStateManager.glVertex3f(mx, my+1, mz+1);
			GlStateManager.glVertex3f(mx+1, my+1, mz+1);
			GlStateManager.glVertex3f(mx, my+1, mz+1);
			GlStateManager.glVertex3f(mx+1, my+1, mz+1);
			GlStateManager.glVertex3f(mx+1, my+1, mz);

			GlStateManager.glEnd();


			if(depth)  {
				GlStateManager.enableDepth();
			}
			if(texture) {
				GlStateManager.enableTexture2D();
			}

			GlStateManager.disableBlend();
			GlStateManager.enableAlpha();

			GlStateManager.popMatrix();
		}

		ItemStack is = p.getHeldItemMainhand();
		if(is != null) {
			if(is.getTagCompound() != null && is.getItem() instanceof ItemBoundGem && is.getTagCompound().hasKey("pos") && is.getTagCompound().getInteger("dim") == p.dimension) {
				int[] coords = ItemBoundGem.getCoords(is);
				Coord3D c = new Coord3D(coords[0],coords[1],coords[2]);
				float mx = c.x;
				float my = c.y;
				float mz = c.z;

				double dist = p.getDistance(mx+0.5D, my+0.5D, mz+0.5D);

				if(dist > 24)
					return;

				if(!p.getEntityWorld().isBlockLoaded(new BlockPos((int)mx, (int)my, (int)mz)))
					return;

				AxisAlignedBB aabb = p.getEntityWorld().getBlockState(new BlockPos((int)mx, (int)my, (int)mz)).getBlock().getSelectedBoundingBox(p.getEntityWorld().getBlockState(new BlockPos((int)mx, (int)my, (int)mz)),p.getEntityWorld(), new BlockPos((int)mx, (int)my, (int)mz));

				GlStateManager.pushMatrix();

				boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
				GlStateManager.disableDepth();
				boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
				GlStateManager.disableTexture2D();
				GlStateManager.disableAlpha();
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				//e6c297
				GlStateManager.color(0.8984375F, 0.7578125F, 0.58984375F);
				GlStateManager.glLineWidth(1);
				GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

				GlStateManager.glBegin(GL11.GL_LINES);

				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);

				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
				GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

				GlStateManager.glEnd();


				if (depth) 
				{
					GlStateManager.enableDepth();
				}
				if (texture) 
				{
					GlStateManager.enableTexture2D();
				}

				GlStateManager.disableBlend();
				GlStateManager.enableAlpha();

				GlStateManager.popMatrix();
			}

			if(is.getItem() instanceof ItemMagicalBuilder)
			{
				ItemMagicalBuilder builder = ItemMagicalBuilder.class.cast(is.getItem());
				if(builder.hasFirstPoint(is) && !builder.hasSecondPoint(is))
				{
					Coord3D c = builder.getFirstPoint(is);

					AxisAlignedBB aabb = new AxisAlignedBB(c.x, c.y, c.z, c.x+1, c.y+1, c.z+1);

					GlStateManager.pushMatrix();

					boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
					GlStateManager.disableDepth();
					boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
					GlStateManager.disableTexture2D();
					GlStateManager.disableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					GlStateManager.color(1, 0, 1);
					GlStateManager.glLineWidth(2);
					GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

					GlStateManager.glBegin(GL11.GL_LINES);

					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);

					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

					GlStateManager.glEnd();


					if (depth) 
					{
						GlStateManager.enableDepth();
					}
					if (texture) 
					{
						GlStateManager.enableTexture2D();
					}

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();

					GlStateManager.popMatrix();

				}
				else if(builder.hasFirstPoint(is) && builder.hasSecondPoint(is))
				{
					Coord3D c = builder.getFirstPoint(is);
					Coord3D c1 = builder.getSecondPoint(is);

					AxisAlignedBB aabb = new AxisAlignedBB(c1.x < c.x ? c.x+1 : c.x, c1.y < c.y ? c.y+1 : c.y, c1.z < c.z ? c.z+1 : c.z, c1.x < c.x ? c1.x : c1.x+1, c1.y < c.y ? c1.y : c1.y+1, c1.z < c.z ? c1.z : c1.z+1);

					GlStateManager.pushMatrix();

					boolean depth = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
					GlStateManager.disableDepth();
					boolean texture = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
					GlStateManager.disableTexture2D();
					GlStateManager.disableAlpha();
					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

					GlStateManager.color(1, 0, 1);
					GlStateManager.glLineWidth(2);
					GlStateManager.translate(-doubleX, -doubleY, -doubleZ);

					GlStateManager.glBegin(GL11.GL_LINES);

					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);

					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.minY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.minY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.minZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.minX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.maxZ);
					GL11.glVertex3d(aabb.maxX, aabb.maxY, aabb.minZ);

					GlStateManager.glEnd();


					if (depth) 
					{
						GlStateManager.enableDepth();
					}
					if (texture) 
					{
						GlStateManager.enableTexture2D();
					}

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();

					GlStateManager.popMatrix();
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void clientGUIRenderTickEvent(RenderTickEvent event) {
		EntityPlayer player = EssentialCraftCore.proxy.getClientPlayer();
		if(player != null) {
			World w = player.getEntityWorld();
			if(w != null) {
				GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
				if(currentScreen != null && currentScreen instanceof GuiContainer && GuiScreen.isCtrlKeyDown()) {
					GuiContainer gc = (GuiContainer)currentScreen;
					try {
						Class<GuiContainer> gcClass = GuiContainer.class;
						Field FguiLeft = gcClass.getDeclaredFields()[4];
						Field FguiTop = gcClass.getDeclaredFields()[5];
						FguiLeft.setAccessible(true);
						FguiTop.setAccessible(true);
						int guiLeft = FguiLeft.getInt(gc);
						int guiTop = FguiTop.getInt(gc);
						int k = guiLeft;
						int l = guiTop;

						IInventory inv = getInventoryFromContainer(gc);
						if(inv != null && inv instanceof ISidedInventory) {
							ISidedInventory sided = (ISidedInventory) inv;
							if(RenderHandlerEC3.slotsTable.isEmpty() || !RenderHandlerEC3.slotsTable.containsKey(inv)) {
								RenderHandlerEC3.slotsTable.clear();
								Hashtable<Integer, List<EnumFacing>> accessibleSlots = new Hashtable<Integer, List<EnumFacing>>();
								for(int j = 0; j < 6; ++j) {
									EnumFacing d = EnumFacing.getFront(j);
									int[] slots = sided.getSlotsForFace(d);
									if(slots != null) {
										for(int i1 = 0; i1 < slots.length; ++i1) {
											int slotN = slots[i1];
											if(accessibleSlots.containsKey(slotN)) {
												List<EnumFacing> lst = accessibleSlots.get(slotN);
												if(!lst.contains(d))
													lst.add(d);
												accessibleSlots.put(slotN, lst);
											}
											else {
												List<EnumFacing> lst = new ArrayList<EnumFacing>();
												lst.add(d);
												accessibleSlots.put(slotN, lst);
											}
										}
									}
								}
								RenderHandlerEC3.slotsTable.put(inv, accessibleSlots);
							}
						}

						for(int i = 0; i < gc.inventorySlots.inventorySlots.size(); ++i) {
							Slot slt = (Slot)gc.inventorySlots.inventorySlots.get(i);
							if((slt.inventory instanceof TileEntity) || (slt.inventory instanceof InventoryBasic)) {

								GlStateManager.pushMatrix();
								GlStateManager.scale(0.5F, 0.5F, 0.5F);
								Minecraft.getMinecraft().fontRendererObj.drawString(""+slt.slotNumber, (k+slt.xPos)*2, (l+slt.yPos)*2, 0x000000);
								GlStateManager.popMatrix();
								if(slt.inventory instanceof ISidedInventory) {
									if(RenderHandlerEC3.slotsTable.containsKey(inv)) {
										Hashtable<Integer, List<EnumFacing>> accessibleSlots = RenderHandlerEC3.slotsTable.get(inv);
										if(accessibleSlots.containsKey(slt.slotNumber)) {
											List<EnumFacing> lst = accessibleSlots.get(slt.slotNumber);
											if(lst != null && !lst.isEmpty()) {
												EnumFacing d = lst.get((int) (w.getWorldTime()/20%lst.size()));
												GlStateManager.pushMatrix();
												GlStateManager.scale(0.5F, 0.5F, 0.5F);
												Minecraft.getMinecraft().fontRendererObj.drawString(""+d, (k+slt.xPos)*2, (l+slt.yPos+12)*2, 0x000000);
												GlStateManager.popMatrix();
											}
										}
									}
								}
							}
							GlStateManager.color(1, 1, 1);
						}
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if(event.phase == Phase.START) {
			if(ItemInventoryGem.clickTicks > 0)
				--ItemInventoryGem.clickTicks;
			if(ItemInventoryGem.clickTicks == 0)
				ItemInventoryGem.currentlyClicked = null;
		}
		for(int i = 0; i < ClientProxy.playingMusic.size(); ++i) {
			ISound snd = ClientProxy.playingMusic.get(i).getRight();
			if(!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(snd))
				ClientProxy.playingMusic.remove(i);
		}
		if(event.player.getEntityWorld().isRemote && event.phase == Phase.START)
			if(event.player instanceof EntityPlayerSP) {
				EntityPlayerSP player = (EntityPlayerSP)event.player;

				if(PotionRegistry.paradox != null && mc.player.getActivePotionEffect(PotionRegistry.paradox) != null) {
					int duration = mc.player.getActivePotionEffect(PotionRegistry.paradox).getDuration();
					if(duration > 100) {
						if(duration % 100 == 0)
							mc.world.playSound(mc.player.posX, mc.player.posY, mc.player.posZ, SoundRegistry.potionHeartbeat, SoundCategory.PLAYERS, 100, 1, true);
						if(currentParadoxTicks > 0)
							--currentParadoxTicks;
						if(currentParadoxTicks == 0 && duration < 1600) {
							paradoxID = -1;
							isParadoxActive = false;
							MiscUtils.setShaders(-1);
						}
						if(duration < 1700 && !isParadoxActive && player.getEntityWorld().rand.nextFloat() < 0.005F) {
							//paradoxID = player.getEntityWorld().rand.nextInt(6);
							paradoxID = mc.world.rand.nextInt(4);
							currentParadoxTicks = 200;
							isParadoxActive = true;
						}
					}
					else {
						currentParadoxTicks = 0;
						paradoxID = -1;
						isParadoxActive = false;
					}
				}

				if(renderPartialTicksCheck > 0) {
					--renderPartialTicksCheck;
					if(renderPartialTicksCheck <= 0)
						Minecraft.getMinecraft().world.playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1F, 2F, false);
				}

				if(!ClientProxy.kbArmorVision.isKeyDown() && isNightVisionKeyDown) {
					isNightVisionKeyDown = false;
				}

				if(isNightVisionActive && (player.inventory.armorInventory[3]==null || !(player.inventory.armorInventory[3].getItem() instanceof ItemComputerArmor))) {
					isNightVisionActive = false;
					Minecraft.getMinecraft().player.removePotionEffect(MobEffects.NIGHT_VISION);
				}

				if(ClientProxy.kbArmorVision.isKeyDown() && !isNightVisionKeyDown && (player.inventory.armorInventory[3]!=null && (player.inventory.armorInventory[3].getItem() instanceof ItemComputerArmor))) {
					isNightVisionKeyDown = true;
					isNightVisionActive = !isNightVisionActive;
					if(isNightVisionActive) {
						PotionEffect effect = new PotionEffect(MobEffects.NIGHT_VISION,Integer.MAX_VALUE,0,false,false);
						effect.setPotionDurationMax(true);
						Minecraft.getMinecraft().player.addPotionEffect(effect);
					}
					else {
						Minecraft.getMinecraft().player.removePotionEffect(MobEffects.NIGHT_VISION);
					}

				}

				if(!ClientProxy.kbArmorBoost.isKeyDown() && isSprintKeyDown) {
					isSprintKeyDown = false;
				}

				if(ClientProxy.kbArmorBoost.isKeyDown() && !isSprintKeyDown) {
					isSprintKeyDown = true;
					if(((ItemComputerArmor)ItemsCore.computer_helmet).hasFullset(player) && renderPartialTicksCheck <= 0) {
						Vec3d lookVec = player.getLookVec();
						player.motionX += lookVec.xCoord*3;
						player.motionY += lookVec.yCoord*3;
						player.motionZ += lookVec.zCoord*3;
						for(int i = 0; i < 10; ++i)
							player.getEntityWorld().playSound(player.posX, player.posY, player.posZ, SoundEvents.ENTITY_FIREWORK_LARGE_BLAST, SoundCategory.PLAYERS, 1, 0.01F+player.getEntityWorld().rand.nextFloat(), false);
						renderPartialTicksCheck = 20;
					}
				}

				if(!player.capabilities.isFlying && player.isInWater() && ItemComputerArmor.class.cast(ItemsCore.computer_helmet).hasFullset(player) && Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
					player.motionX *= 1.2D;
					if(player.motionY > 0)
						player.motionY *= 1.2D;
					player.motionZ *= 1.2D;

					double d8, d9, d2, d4;
					d2 = Math.cos((double)player.rotationYaw * Math.PI / 180.0D);
					d4 = Math.sin((double)player.rotationYaw * Math.PI / 180.0D);
					double d5 = (double)(player.getEntityWorld().rand.nextFloat() * 2.0F - 1.0F);
					double d6 = (double)(player.getEntityWorld().rand.nextInt(2) * 2 - 1) * 0.7D;

					d8 = player.posX - d2 * d5 * 0.8D + d4 * d6;
					d9 = player.posZ - d4 * d5 * 0.8D - d2 * d6;
					for(int i = 0; i < 10; ++i)
						player.getEntityWorld().spawnParticle(EnumParticleTypes.WATER_BUBBLE, d8, player.posY - 0.125D, d9, player.motionX, player.motionY, player.motionZ);
				}
			}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderWindRuneOverlay(DrawBlockHighlightEvent event) {

	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void pre(RenderGameOverlayEvent.Pre event) {
		try {
			if(event.getType() == ElementType.CROSSHAIRS) {
				if(ItemComputerArmor.hasFullset(Minecraft.getMinecraft().player)) {
					ScaledResolution res = new ScaledResolution(mc);
					float r = 10;
					float k = res.getScaledWidth()/2 + 105;
					float h = 11;

					int circle_points = 100;
					float angle = 2.0f * 3.1416f / circle_points;

					GlStateManager.pushMatrix();

					GlStateManager.enableDepth();
					GlStateManager.disableTexture2D();
					GlStateManager.disableLighting();

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableAlpha();

					GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN); 

					float renderTime = Minecraft.getMinecraft().player.ticksExisted % 40;
					if(renderTime > 20)
						renderTime = 40 - renderTime;

					GlStateManager.color(0, 1, 1, MathHelper.clamp(renderTime/20,0.1F,0.8F));

					GL11.glVertex2f(k, h);

					for(angle=2.0f;angle<8.3F-renderPartialTicksCheck/20*8.3F;angle+=0.01) {
						float x2 = (float) (k+Math.sin(angle)*r);
						float y2 = (float) (h+Math.cos(angle)*r);

						GL11.glVertex2f(x2,y2);
					}

					GlStateManager.color(1, 1, 1);

					GlStateManager.glEnd(); 

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();

					GlStateManager.enableTexture2D();

					GlStateManager.popMatrix();

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableAlpha();

					GlStateManager.color(1, 1, 1, MathHelper.clamp(renderTime/20,0.1F,0.8F));

					DrawUtils.drawTexture_Items((int)k-8, (int)h-8, TextureUtils.fromItem(Items.FIREWORKS), 16, 16, 100);

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();
				}

				if(Minecraft.getMinecraft().player.inventory.armorInventory[3] != null && Minecraft.getMinecraft().player.inventory.armorInventory[3].getItem() instanceof ItemComputerArmor) {
					ScaledResolution res = new ScaledResolution(mc);
					float r = 10;
					float k = res.getScaledWidth()/2-108;
					float h = 11;

					int circle_points = 100;
					float angle = 2.0f * 3.1416f / circle_points;

					GlStateManager.pushMatrix();

					GL11.glEnable(2929);
					GL11.glEnable(3042);
					GlStateManager.blendFunc(770, 771);
					GL11.glDisable(3553);
					GL11.glDisable(2896);

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableAlpha();

					GlStateManager.glBegin(GL11.GL_TRIANGLE_FAN); 

					float renderTime = Minecraft.getMinecraft().player.ticksExisted % 40;
					if(renderTime > 20)
						renderTime = 40 - renderTime;

					float cB = 0.2F;

					if(isNightVisionActive)
						cB = 1F;

					GlStateManager.color(0, 0, cB, MathHelper.clamp(renderTime/20,0.1F,0.8F));

					GL11.glVertex2f(k, h);

					for(angle=2.0f;angle<8.3F;angle+=0.01) {
						float x2 = (float) (k+Math.sin(angle)*r);
						float y2 = (float) (h+Math.cos(angle)*r);

						GL11.glVertex2f(x2,y2);
					}

					GlStateManager.color(1, 1, 1);

					GlStateManager.glEnd(); 

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();

					GL11.glEnable(3553);

					GlStateManager.popMatrix();

					GlStateManager.enableBlend();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GlStateManager.disableAlpha();

					GlStateManager.color(1, 1, 1, MathHelper.clamp(renderTime/20,0.1F,0.8F));

					DrawUtils.drawTexture_Items((int)k-8, (int)h-8, TextureUtils.fromItem(ItemsCore.computer_helmet), 16, 16, 100);

					GlStateManager.disableBlend();
					GlStateManager.enableAlpha();
				}

			}

			if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
				if(Minecraft.getMinecraft().player.getActivePotionEffect(PotionRegistry.mruCorruptionPotion)!=null) {
					Minecraft.getMinecraft().renderEngine.bindTexture(iconsEC);
				}
			}
			if(event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
				EntityPlayer p = Minecraft.getMinecraft().player;
				RayTraceResult target = p.rayTrace(mc.playerController.getBlockReachDistance(), event.getPartialTicks());

				if(target != null && target.typeOfHit == RayTraceResult.Type.BLOCK) {
					int x = target.getBlockPos().getX();
					int y = target.getBlockPos().getY();
					int z = target.getBlockPos().getZ();
					Block b = p.getEntityWorld().getBlockState(target.getBlockPos()).getBlock();
					if(b != null && b instanceof BlockWindRune) {
						TileWindRune rune = (TileWindRune)p.getEntityWorld().getTileEntity(target.getBlockPos());
						if(rune != null) {
							if(p.getHeldItemMainhand() != null) {
								WindImbueRecipe rec = WindImbueRecipe.findRecipeByComponent(p.getHeldItemMainhand());
								if(rec != null) {
									int energyReq = rec.enderEnergy;
									int energy = rune.energy;

									int color = 0xffffff;
									boolean creative = p.capabilities.isCreativeMode;

									if(energy < energyReq && !creative)
										color = 0xff0000;
									else
										color = 0x00ff66;

									ScaledResolution res = new ScaledResolution(mc);
									String displayed = energyReq+" ESPE";
									if(creative)
										displayed += " [Creative]";
									Minecraft.getMinecraft().fontRendererObj.drawString(displayed, res.getScaledWidth()/2-displayed.length()*3, res.getScaledHeight()/2+5, color);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			return;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void post(RenderGameOverlayEvent.Post event) {
		try {
			if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
				Minecraft mc = Minecraft.getMinecraft();
				ScaledResolution scaledresolution = new ScaledResolution(mc);
				int k = scaledresolution.getScaledWidth();
				int l = scaledresolution.getScaledHeight();

				if(mc.player.getActivePotionEffect(PotionRegistry.chaosInfluence) != null) {
					GlStateManager.disableDepth();
					GlStateManager.depthMask(false);
					GlStateManager.enableBlend();
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 0.3F);
					TextureAtlasSprite iicon = TextureUtils.fromBlock(BlocksCore.lightCorruption[0], 7);
					mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					float f1 = (float)iicon.getMinU();
					float f2 = (float)iicon.getMinV();
					float f3 = (float)iicon.getMaxU();
					float f4 = (float)iicon.getMaxV();
					TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0.0D, (double)l, -90.0D, (double)f1, (double)f4);
					tessellator.addVertexWithUV((double)k, (double)l, -90.0D, (double)f3, (double)f4);
					tessellator.addVertexWithUV((double)k, 0.0D, -90.0D, (double)f3, (double)f2);
					tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f1, (double)f2);
					tessellator.draw();
					GlStateManager.depthMask(true);
					GlStateManager.enableDepth();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				}

				if(mc.player.getActivePotionEffect(PotionRegistry.frozenMind) != null) {
					GlStateManager.disableDepth();
					GlStateManager.depthMask(false);
					GlStateManager.enableBlend();
					OpenGlHelper.glBlendFunc(770, 771, 1, 0);
					GlStateManager.color(1.0F, 1.0F, 1.0F, 0.3F);
					TextureAtlasSprite iicon = TextureUtils.fromBlock(BlocksCore.lightCorruption[1], 7);
					mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					float f1 = (float)iicon.getMinU();
					float f2 = (float)iicon.getMinV();
					float f3 = (float)iicon.getMaxU();
					float f4 = (float)iicon.getMaxV();
					TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0.0D, (double)l, -90.0D, (double)f1, (double)f4);
					tessellator.addVertexWithUV((double)k, (double)l, -90.0D, (double)f3, (double)f4);
					tessellator.addVertexWithUV((double)k, 0.0D, -90.0D, (double)f3, (double)f2);
					tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, (double)f1, (double)f2);
					tessellator.draw();
					GlStateManager.depthMask(true);
					GlStateManager.enableDepth();
					GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				}

				if(mc.player.getActivePotionEffect(PotionRegistry.paradox) != null) {
					/*
			    	int duration = mc.player.getActivePotionEffect(PotionRegistry.paradox).getDuration();
			    	renderImage(whitebox, k, l, (float)(duration-1800)/200,1,1,1);
			    	if(duration == 1700)MiscUtils.setShaders(-1);
			    	if(duration <= 1600)
			    	{
			    		int rDur = duration % 200;
			    		if((rDur < 18 && rDur > 15) || (rDur > 20 && rDur < 25))
			    			renderImage(whitebox, k, l, 1,0,0,0);
			    	}
			    	renderParadox();
			    	if(duration == 1)
			    	{
			    		MiscUtils.setShaders(-1);
						World w = mc.world;
						WorldProvider prov = w.provider;
						if(prov.getSkyRenderer() instanceof RenderSkyParadox_1)
							prov.setSkyRenderer(skyRenderer);
			    	}*/
				}
			}
			if(event.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
				if(Minecraft.getMinecraft().player.getActivePotionEffect(PotionRegistry.mruCorruptionPotion)!=null) {
					Minecraft.getMinecraft().renderEngine.bindTexture(Gui.ICONS);
				}
			}
		} catch (Exception e) {
			return;
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onModelBake(ModelBakeEvent event) {
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:rightClicker", "normal"), new ModelRightClicker());
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:mimic", "normal"), new ModelMimic());
		for(int i = 0; i < 16; i++) {
			event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:fancyBlock/mimic", "internal=" + FancyBlockType.fromIndex(i).toString()), new ModelFancyMimic(FancyBlockType.fromIndex(i).toString()));
		}

		ModelGunHandler.blankItem = event.getModelRegistry().getObject(new ModelResourceLocation("essentialcraft:item/blank", "inventory"));

		IBakedModel pistolModel = event.getModelRegistry().getObject(new ModelResourceLocation("essentialcraft:item/gun.pistol", "inventory"));
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:item/gun.pistol", "internal"), new ModelGunHandler(pistolModel));
		IBakedModel rifleModel = event.getModelRegistry().getObject(new ModelResourceLocation("essentialcraft:item/gun.rifle", "inventory"));
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:item/gun.rifle", "internal"), new ModelGunHandler(rifleModel));
		IBakedModel sniperModel = event.getModelRegistry().getObject(new ModelResourceLocation("essentialcraft:item/gun.sniper", "inventory"));
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:item/gun.sniper", "internal"), new ModelGunHandler(sniperModel));
		IBakedModel gatlingModel = event.getModelRegistry().getObject(new ModelResourceLocation("essentialcraft:item/gun.gatling", "inventory"));
		event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:item/gun.gatling", "internal"), new ModelGunHandler(gatlingModel));

		//CCL made GL rendering, why should I make one as well? Why don't I just use theirs?
		if(Loader.isModLoaded("codechickenlib") || Loader.isModLoaded("CodeChickenLib")) {
			event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:elementalCrystal", "inventory"), new RenderElementalCrystalAsItem());
			event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:mithrilineCrystal", "inventory"), new RenderMithrilineCrystalAsItem());
			event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:armor", "inventory"), new ArmorRenderer());
			{
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:coldDistillatorTemp", "inventory"), new RenderColdDistillatorAsItem());
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:crystalControllerTemp", "inventory"), new RenderCrystalControllerAsItem());
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:crystalExtractorTemp", "inventory"), new RenderCrystalExtractorAsItem());
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:darknessObeliskTemp", "inventory"), new RenderDarknessObeliskAsItem());
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:enderGeneratorTemp", "inventory"), new RenderEnderGeneratorAsItem());
				event.getModelRegistry().putObject(new ModelResourceLocation("essentialcraft:magicalRepairerTemp", "inventory"), new RenderMagicalRepairerAsItem());
			}
		}
	}
	
	ResourceLocation loc = new ResourceLocation("essentialcraft","textures/hud/sniper_scope.png");

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientRenderTick(RenderGameOverlayEvent.Pre event)
	{
		if(event.getType() != ElementType.ALL)
		{
			EntityPlayer p = Minecraft.getMinecraft().player;
			if(p.getHeldItemMainhand() != null && p.getHeldItemMainhand().getItem() instanceof ItemGun && p.isSneaking() && p.getHeldItemMainhand().getTagCompound() != null && p.getHeldItemMainhand().getTagCompound().hasKey("scope") && ItemGun.class.cast(p.getHeldItemMainhand().getItem()).gunType.equalsIgnoreCase("sniper"))
			{
				if(event.getType() == ElementType.CROSSHAIRS)
				{
					Minecraft mc = Minecraft.getMinecraft();
					ScaledResolution scaledresolution = new ScaledResolution(mc);
					int k = scaledresolution.getScaledWidth();
					int l = scaledresolution.getScaledHeight();
					if(k < l) k = l;
					if(k > l) k = l;

					Minecraft.getMinecraft().getTextureManager().bindTexture(loc);
					GlStateManager.depthMask(false);

					TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();
					Minecraft.getMinecraft().getTextureManager().bindTexture(RenderHandlerEC3.whitebox);

					GlStateManager.color(0, 0, 0);

					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(0, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV(0, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, (double)l, -90.0D, 0.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth(), (double)l, -90.0D, 1.0D, 1.0D);
					tessellator.addVertexWithUV(scaledresolution.getScaledWidth(), 0.0D, -90.0D, 1.0D, 0.0D);
					tessellator.addVertexWithUV((double)k+scaledresolution.getScaledWidth()/2-scaledresolution.getScaledWidth()/4, 0.0D, -90.0D, 0.0D, 0.0D);
					tessellator.draw();

					GlStateManager.color(1, 1, 1);

					GlStateManager.depthMask(true);

				}
				event.setCanceled(true);
			}
		}
	}

	public static void renderImage(ResourceLocation image, int scaledResX, int scaledResY, float opacity, float r, float g, float b) {
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.color(r, g, b, opacity);
		GlStateManager.disableAlpha();
		mc.getTextureManager().bindTexture(image);
		TessellatorWrapper tessellator = TessellatorWrapper.getInstance();
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0.0D, (double)scaledResY, -90.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV((double)scaledResX, (double)scaledResY, -90.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV((double)scaledResX, 0.0D, -90.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
