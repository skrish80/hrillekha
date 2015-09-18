package com.techlords.crown.mvc.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class StringUtils {

	public static Collection<String> commaDelimitedStringAsCollection(
			String input) {
		final Set<String> output = new HashSet<String>();
		final String[] arr = input.split(",");
		for (final String val : arr) {
			output.add(val.trim());
		}
		return output;
	}

}
