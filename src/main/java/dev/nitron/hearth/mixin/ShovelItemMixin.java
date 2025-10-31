package dev.nitron.hearth.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {
    @Inject(method = "useOn", at = @At(value = "HEAD"), cancellable = true)
    public void hearth$useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir){
        BlockPos pos = useOnContext.getClickedPos();
        Level level = useOnContext.getLevel();
        if (level.getBlockState(pos).getBlock() instanceof FarmBlock){
            level.addDestroyBlockEffect(pos, level.getBlockState(pos));
            if (!level.isClientSide()) {
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(useOnContext.getPlayer(), Blocks.DIRT.defaultBlockState()));
                if (useOnContext.getPlayer() != null) {
                    useOnContext.getItemInHand().hurtAndBreak(1, useOnContext.getPlayer(), useOnContext.getHand().asEquipmentSlot());
                }
            }
            level.playSound(useOnContext.getPlayer(), pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            cir.setReturnValue(InteractionResult.SUCCESS);
        }
    }
}
