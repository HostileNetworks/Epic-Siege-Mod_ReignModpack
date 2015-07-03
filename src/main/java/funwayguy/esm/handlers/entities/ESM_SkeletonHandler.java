package funwayguy.esm.handlers.entities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import funwayguy.esm.core.ESM_Settings;
import funwayguy.esm.core.ESM_Utils;

public class ESM_SkeletonHandler
{
	public static void onEntityJoinWorld(EntitySkeleton skeleton)
	{
		if(skeleton.getSkeletonType() == 0)
		{
			if(ESM_Settings.WitherSkeletons && (ESM_Settings.WitherSkeletonRarity <= 0 || skeleton.getRNG().nextInt(ESM_Settings.WitherSkeletonRarity) == 0))
			{
				skeleton.setDead();
				EntitySkeleton newSkeleton = new EntitySkeleton(skeleton.worldObj);
				newSkeleton.setLocationAndAngles(skeleton.posX, skeleton.posY, skeleton.posZ, 0.0F, 0.0F);
				newSkeleton.setSkeletonType(1);
				newSkeleton.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
				newSkeleton.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
				newSkeleton.setCombatTask();
				newSkeleton.getEntityData().setBoolean("ESM_MODIFIED", true);
				skeleton.worldObj.spawnEntityInWorld(newSkeleton);
				ESM_Utils.replaceAI(newSkeleton);
			} else
			{
				skeleton.getEntityData().setString("ESM_TASK_ID", skeleton.getUniqueID().toString() + ",NULL");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void onLivingUpdate(EntitySkeleton skeleton)
	{
		if(skeleton.getSkeletonType() == 0 && !skeleton.getEntityData().getString("ESM_TASK_ID").equals(skeleton.getUniqueID().toString() + "," + ESM_Settings.SkeletonDistance))
		{
			for(int i = 0; i < skeleton.tasks.taskEntries.size(); i++)
			{
				EntityAITaskEntry entry = (EntityAITaskEntry)skeleton.tasks.taskEntries.get(i);
				if(entry.action.getClass() == EntityAIArrowAttack.class)
				{
					EntityAITaskEntry replace = skeleton.tasks.new EntityAITaskEntry(entry.priority, new EntityAIArrowAttack(skeleton, 1.0D, 20, 60, (float)ESM_Settings.SkeletonDistance));
					skeleton.tasks.taskEntries.set(i, replace);
				}
			}
			
			skeleton.getEntityData().setString("ESM_TASK_ID", skeleton.getUniqueID().toString() + "," + ESM_Settings.SkeletonDistance);
		}
	}
}
