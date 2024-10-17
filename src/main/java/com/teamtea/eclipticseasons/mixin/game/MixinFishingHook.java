package com.teamtea.eclipticseasons.mixin.game;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.eclipticseasons.api.util.WeatherUtil;
import com.teamtea.eclipticseasons.config.ServerConfig;
import com.teamtea.eclipticseasons.game.SeasonFishingHooks;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishingHook.class)
public class MixinFishingHook {


    @ModifyExpressionValue(
            method = "retrieve",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootTable;getRandomItems(Lnet/minecraft/world/level/storage/loot/LootParams;)Lit/unimi/dsi/fastutil/objects/ObjectArrayList;")
    )
    private ObjectArrayList<ItemStack> ecliptic$retrieve(ObjectArrayList<ItemStack> original, @Local LootParams lootparams) {
        return SeasonFishingHooks.modify(lootparams, original);
    }

}
