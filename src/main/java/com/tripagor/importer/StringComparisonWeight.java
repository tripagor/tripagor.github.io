package com.tripagor.importer;

import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.metrics.StringMetrics;
import org.simmetrics.simplifiers.Simplifiers;

public class StringComparisonWeight {

	public float getWeightLevenshtein(String str1, String str2) {
		StringMetric metric = StringMetricBuilder.with(new Levenshtein()).simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase()).build();

		return metric.compare(str1, str2);
	}

	public float getWeightJaroWinkler(String str1, String str2) {
		StringMetric metric = StringMetrics.jaroWinkler();

		return metric.compare(str1, str2);
	}

}
