package ec3.utils.common;

import net.minecraft.client.renderer.GlStateManager;

public class NodeFinder {

	public void orientCamera(float flt) {
		GlStateManager.rotate(180F, 0, 0, 1);
	}
}
