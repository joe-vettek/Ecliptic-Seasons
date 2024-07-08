package xueluoanping.ecliptic.client;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;
import xueluoanping.ecliptic.Ecliptic;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SoundEventsRegistry {
    public final static SoundEvent CUP_BROKEN = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "block.cup_broken"));
    public final static SoundEvent RECORD_MOVING_UP = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.moving_up"));
    public final static SoundEvent RECORD_PICKING_TEA = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.picking_tea"));
    public final static SoundEvent RECORD_SPRING_FESTIVAL_OVERTURE = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.spring_festival_overture"));
    public final static SoundEvent RECORD_FLOWERS_AND_MOON = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.flowers_and_moon"));
    public final static SoundEvent RECORD_DANCING_GOLDEN_SNAKE = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.dancing_golden_snake"));
    public final static SoundEvent RECORD_JOYFUL = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.joyful"));
    public final static SoundEvent RECORD_GREEN_WILLOW = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.green_willow"));
    public final static SoundEvent RECORD_PURPLE_BAMBOO_MELODY = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.purple_bamboo_melody"));
    public final static SoundEvent RECORD_WELCOME_MARCH = SoundEvent.createVariableRangeEvent(new ResourceLocation(Ecliptic.MODID, "record.welcome_march"));

    @SubscribeEvent
    public static void blockRegister(RegisterEvent event) {
        // MultiPackResourceManager
        event.register(Registries.SOUND_EVENT, soundEventRegisterHelper -> {
            soundEventRegisterHelper.register(CUP_BROKEN.getLocation(), CUP_BROKEN);
            soundEventRegisterHelper.register(RECORD_MOVING_UP.getLocation(), RECORD_MOVING_UP);
            soundEventRegisterHelper.register(RECORD_PICKING_TEA.getLocation(), RECORD_PICKING_TEA);
            soundEventRegisterHelper.register(RECORD_SPRING_FESTIVAL_OVERTURE.getLocation(), RECORD_SPRING_FESTIVAL_OVERTURE);
            soundEventRegisterHelper.register(RECORD_FLOWERS_AND_MOON.getLocation(), RECORD_FLOWERS_AND_MOON);
            soundEventRegisterHelper.register(RECORD_DANCING_GOLDEN_SNAKE.getLocation(), RECORD_DANCING_GOLDEN_SNAKE);
            soundEventRegisterHelper.register(RECORD_JOYFUL.getLocation(), RECORD_JOYFUL);
            soundEventRegisterHelper.register(RECORD_GREEN_WILLOW.getLocation(), RECORD_GREEN_WILLOW);
            soundEventRegisterHelper.register(RECORD_PURPLE_BAMBOO_MELODY.getLocation(), RECORD_PURPLE_BAMBOO_MELODY);
            soundEventRegisterHelper.register(RECORD_WELCOME_MARCH.getLocation(), RECORD_WELCOME_MARCH);
        });
    }
}
