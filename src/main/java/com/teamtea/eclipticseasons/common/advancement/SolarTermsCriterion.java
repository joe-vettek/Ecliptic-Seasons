package com.teamtea.eclipticseasons.common.advancement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamtea.eclipticseasons.EclipticSeasonsMod;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class SolarTermsCriterion extends SimpleCriterionTrigger<SolarTermsCriterion.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return SolarTermsCriterion.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (triggerInstance -> triggerInstance.test()));
    }

    public record TriggerInstance(
            Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance
    {
        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                builder -> builder.group(
                                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(SolarTermsCriterion.TriggerInstance::player))
                        .apply(builder, SolarTermsCriterion.TriggerInstance::new)
        );

        public static Criterion<TriggerInstance> simple() {
            return EclipticSeasonsMod.ModContents.SOLAR_TERMS.get().createCriterion(
                    new SolarTermsCriterion.TriggerInstance(Optional.empty())
            );
        }

        public boolean test() {
            return true;
        }
    }
}
