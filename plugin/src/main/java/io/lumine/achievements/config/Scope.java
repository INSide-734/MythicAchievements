package io.lumine.achievements.config;

import io.lumine.mythic.bukkit.utils.config.properties.PropertyScope;

public enum Scope implements PropertyScope {
    
    NONE(""),
    CONFIG("config"),
    CATEGORIES("categories"),
	;
    
	private final String scope;
	
	private Scope(String scope)	{
	    this.scope = scope;
	}  
	
	@Override
	public String get() {
	    return scope;
	}

	@Override
	public String toString()	{
	    return get();
	}
}