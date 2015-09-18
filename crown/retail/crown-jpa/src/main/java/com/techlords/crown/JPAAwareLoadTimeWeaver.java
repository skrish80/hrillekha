package com.techlords.crown;

import org.springframework.instrument.classloading.SimpleLoadTimeWeaver;

public class JPAAwareLoadTimeWeaver extends SimpleLoadTimeWeaver {
	@Override
	public ClassLoader getInstrumentableClassLoader() {
		ClassLoader instrumentableClassLoader = super
				.getInstrumentableClassLoader();
		if (instrumentableClassLoader.getClass().getName()
				.endsWith("SimpleInstrumentableClassLoader")) {
			return instrumentableClassLoader.getParent();
		}
		return instrumentableClassLoader;
	}
}
