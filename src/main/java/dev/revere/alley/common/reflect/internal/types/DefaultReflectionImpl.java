package dev.revere.alley.common.reflect.internal.types;

import dev.revere.alley.common.reflect.Reflection;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultReflectionImpl implements Reflection {
    /**
     * A single, shared, immutable instance of this reflect utility.
     * This prevents the creation of unnecessary objects.
     */
    public static final Reflection INSTANCE = new DefaultReflectionImpl();

    /**
     * Constructor for reflect-based instantiation by `ReflectionRepository`.
     * This constructor must be public for `ReflectionRepository` to successfully
     * create an instance using `getDeclaredConstructor().newInstance()`.
     * The `ReflectionRepository` will manage the lifecycle of this service.
     */
    public DefaultReflectionImpl() {
    }
}