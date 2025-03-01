package com.jetpacker06.CreateBrokenBad.item;

import com.jetpacker06.CreateBrokenBad.block.TrayBlock;
import com.jetpacker06.CreateBrokenBad.damage.ModDamageTypes;
import com.jetpacker06.CreateBrokenBad.register.CBBBlocks;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class MethItem extends Item {
    public MethItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        Block clickedBlock = pContext.getLevel().getBlockState(pContext.getClickedPos()).getBlock();
        Block newBlock = ((pContext.getItemInHand().getItem() instanceof MethItem.Blue) ? CBBBlocks.BLUE_METH_TRAY : CBBBlocks.WHITE_METH_TRAY).get();
        if (clickedBlock instanceof TrayBlock.Empty) {
            Direction direction = pContext.getLevel().getBlockState(pContext.getClickedPos()).getValue(TrayBlock.FACING);
            pContext.getLevel().setBlock(
                pContext.getClickedPos(),
                newBlock.defaultBlockState().setValue(TrayBlock.FACING, direction),
                3
            );
            pContext.getLevel().playSound(pContext.getPlayer(),pContext.getClickedPos(), SoundEvents.SAND_HIT, SoundSource.BLOCKS, 2f, 1f);
            pContext.getItemInHand().shrink(1);
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 32; // This is the standard duration for food items. (May need to change)
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT; // Displays the eating animation when the player uses the item
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            int duration = (this instanceof MethItem.White) ? 300 : 1200;
            int amplifier = (this instanceof MethItem.White) ? 2 : 4;
            if(!player.isCreative()) {
                CompoundTag playerData = player.getCustomData();
                if(player.hasEffect(MobEffects.MOVEMENT_SPEED)) {
                    int currentCount = playerData.getInt("MethEffectCount");
                    playerData.putInt("MethEffectCount", currentCount + 1);
                    if(currentCount + 1 >= 5) {
                        player.hurt(ModDamageTypes.overdoseDamage(player.level()), 250);
                    }
                } else {
                    playerData.putInt("MethEffectCount", 0);
                }
            }
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, duration, amplifier));
            if(!player.isCreative()) stack.shrink(1);
        }
        return super.finishUsingItem(stack, world, livingEntity);
    }

    public static class Blue extends MethItem {
        public Blue(Properties pProperties) {
            super(pProperties);
        }
    }

    public static class White extends MethItem {
        public White(Properties pProperties) {
            super(pProperties);
        }
    }
}
