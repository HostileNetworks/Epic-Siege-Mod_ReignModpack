package funwayguy.esm.ai.interop;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class InteropPathfinderTweaks implements InteropPathfinderTweaksInterface {

	@Override
	public boolean isTallBlock(Entity entity, Block block, int posX, int posY, int posZ) {
		return com.cosmicdan.pathfindertweaks.Main.isTallBlock(entity, block, posX, posY, posZ);
	}

}
