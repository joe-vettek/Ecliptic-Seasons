package com.teamtea.eclipticseasons.api;


import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Mapc<T, R> {

    @NotNull R apply(@NotNull T var1);
}
