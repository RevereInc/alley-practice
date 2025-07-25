package dev.revere.alley.tool.reflection.impl.instance;

import dev.revere.alley.tool.reflection.Reflection;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultReflectionImpl implements Reflection {
    /**
     * A single, shared, immutable instance of this reflection utility.
     * This prevents the creation of unnecessary objects.
     */
    public static final Reflection INSTANCE = new DefaultReflectionImpl();

    /**
     * Constructor for reflection-based instantiation by `ReflectionRepository`.
     * This constructor must be public for `ReflectionRepository` to successfully
     * create an instance using `getDeclaredConstructor().newInstance()`.
     * The `ReflectionRepository` will manage the lifecycle of this service.
     */
    public DefaultReflectionImpl() {}
}