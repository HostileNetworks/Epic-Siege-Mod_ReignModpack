package funwayguy.esm.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumDifficulty;
import funwayguy.esm.core.ESM_Settings;

public class ESM_EntityAIBreakDoor extends ESM_EntityAIDoorInteract
{
    private int breakingTime;
    private int field_75358_j = -1;

    public ESM_EntityAIBreakDoor(EntityLiving p_i1618_1_)
    {
        super(p_i1618_1_);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        boolean flag = !super.shouldExecute() ? false : (!this.theEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : true);
        
        if(!flag)
        {
        	return false;
        } else
        {
        	boolean b = this.isValidDoor();
        	return b;
        }
    }
    
    public boolean isValidDoor()
    {
    	if(this.field_151504_e instanceof BlockDoor && this.field_151504_e.getMaterial().isToolNotRequired())
    	{
    		return !((BlockDoor)this.field_151504_e).func_150015_f(this.theEntity.worldObj, this.entityPosX, this.entityPosY, this.entityPosZ);
    	} else if(this.field_151504_e instanceof BlockFenceGate && this.field_151504_e.getMaterial().isToolNotRequired())
    	{
    		return !BlockTrapDoor.func_150118_d(this.theEntity.worldObj.getBlockMetadata(this.entityPosX, this.entityPosY, this.entityPosZ));
    	} else if(this.field_151504_e == Blocks.fence_gate)
    	{
    		return !BlockFenceGate.isFenceGateOpen(this.theEntity.worldObj.getBlockMetadata(this.entityPosX, this.entityPosY, this.entityPosZ));
    	} else
    	{
    		return false;
    	}
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
        this.breakingTime = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        double d0 = this.theEntity.getDistanceSq((double)this.entityPosX, (double)this.entityPosY, (double)this.entityPosZ);
        return this.breakingTime <= 240 && this.isValidDoor() && d0 < 9.0D;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        super.resetTask();
        this.theEntity.worldObj.destroyBlockInWorldPartially(this.theEntity.getEntityId(), this.entityPosX, this.entityPosY, this.entityPosZ, -1);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        super.updateTask();
        
        if (this.theEntity.getRNG().nextInt(20) == 0)
        {
            this.theEntity.worldObj.playAuxSFX(1010, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
        }

        ++this.breakingTime;
        int i = (int)((float)this.breakingTime / 240.0F * 10.0F);

        if (i != this.field_75358_j)
        {
            this.theEntity.worldObj.destroyBlockInWorldPartially(this.theEntity.getEntityId(), this.entityPosX, this.entityPosY, this.entityPosZ, i);
            this.field_75358_j = i;
        }

        if (this.breakingTime == 240 && (this.theEntity.worldObj.difficultySetting == EnumDifficulty.HARD || ESM_Settings.ZombieDiggers))
        {
            this.theEntity.worldObj.setBlockToAir(this.entityPosX, this.entityPosY, this.entityPosZ);
            this.theEntity.worldObj.playAuxSFX(1012, this.entityPosX, this.entityPosY, this.entityPosZ, 0);
            this.theEntity.worldObj.playAuxSFX(2001, this.entityPosX, this.entityPosY, this.entityPosZ, Block.getIdFromBlock(this.field_151504_e));
        }
    }
}