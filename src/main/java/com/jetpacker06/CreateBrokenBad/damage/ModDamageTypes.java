package com.jetpacker06.CreateBrokenBad.damage;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> OVERDOSE_DAMAGE_TYPE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("createbb", "overdose_damage"));
    
     public static DamageSource of(Level level, ResourceKey<DamageType> key) {
         DamageType damageType = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).get(key);
         return new DamageSource(Holder.direct(damageType));
     }
}
