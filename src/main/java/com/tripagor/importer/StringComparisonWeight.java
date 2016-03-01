package com.tripagor.importer;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class StringComparisonWeight {

	public double getWeightLevenshtein(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final int distance = StringUtils.getLevenshteinDistance(str1, str2);
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

	public double getWeightFuzzy(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final int distance = StringUtils.getFuzzyDistance(str1, str2, new Locale("en", "US"));
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

	public double getWeightJaroWinkler(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final double distance = StringUtils.getJaroWinklerDistance(str1, str2);
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

}
