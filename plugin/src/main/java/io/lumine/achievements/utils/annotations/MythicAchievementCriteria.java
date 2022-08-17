package io.lumine.achievements.utils.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MythicAchievementCriteria {
	public String name() default "";
	public String[] aliases() default {};
	public String author() default "";
	public String description() default "";
	public String version() default "1.0";
	public boolean premium() default false;
}