package com.teamtea.eclipticseasons.misc.teacon;

import com.teamtea.eclipticseasons.api.constant.solar.SolarTerm;

import java.time.LocalDate;

public class SolarTermCalculator {

    public static SolarTerm getNowTerm() {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();
        if (year == 2024) {
            if (currentMonth == 10) {
                if (currentDay < 11) return SolarTerm.SUMMER_SOLSTICE;
                if (currentDay < 21) return SolarTerm.LESSER_HEAT;
                return SolarTerm.GREATER_HEAT;
            }
            if (currentMonth == 11) {
                if (currentDay < 11) return SolarTerm.BEGINNING_OF_AUTUMN;
                if (currentDay < 20) return SolarTerm.END_OF_HEAT;
                return SolarTerm.WHITE_DEW;
            }
            if (currentMonth == 12) {
                if (currentDay < 11) return SolarTerm.AUTUMNAL_EQUINOX;
                if (currentDay < 21) return SolarTerm.COLD_DEW;
                return SolarTerm.FIRST_FROST;
            }

        } else if (year == 2025) {
            if (currentMonth == 1) {
                if (currentDay < 11) return SolarTerm.BEGINNING_OF_WINTER;
                if (currentDay < 21) return SolarTerm.LIGHT_SNOW;
                return SolarTerm.HEAVY_SNOW;
            }
            if (currentMonth == 2) {
                if (currentDay < 11) return SolarTerm.WINTER_SOLSTICE;
                if (currentDay < 20) return SolarTerm.LESSER_COLD;
                return SolarTerm.GREATER_COLD;
            }
            if (currentMonth == 3) {
                if (currentDay < 11) return SolarTerm.BEGINNING_OF_SPRING;
                if (currentDay < 21) return SolarTerm.RAIN_WATER;
                return SolarTerm.INSECTS_AWAKENING;
            }
        }
        return SolarTerm.SPRING_EQUINOX;
    }
}
