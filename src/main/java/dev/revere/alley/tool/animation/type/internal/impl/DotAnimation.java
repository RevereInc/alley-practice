package dev.revere.alley.tool.animation.type.internal.impl;

import dev.revere.alley.tool.animation.type.internal.Animation;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class DotAnimation extends Animation {

    public DotAnimation() {
        super(Arrays.asList(".", "..", "..."), 500);
    }
}