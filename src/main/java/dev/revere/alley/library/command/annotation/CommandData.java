package dev.revere.alley.library.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Command Framework - Command <br>
 * The command annotation used to designate methods as commands. All methods
 * should have a single CommandArgs argument
 *
 * @author minnymin3
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandData {

    String name();

    String permission() default "";

    String[] aliases() default {};

    String description();

    String usage() default "";

    boolean inGameOnly() default true;

    boolean isAdminOnly() default false;

    int cooldown() default 0;
}