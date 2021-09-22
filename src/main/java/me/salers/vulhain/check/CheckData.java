package me.salers.vulhain.check;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CheckData {

    public String name();

    public String type();

    public boolean experimental();

    public String category();
}
