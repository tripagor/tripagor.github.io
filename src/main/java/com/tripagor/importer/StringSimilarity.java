package com.tripagor.importer;

import org.simmetrics.StringMetric;
import org.simmetrics.builders.StringMetricBuilder;
import org.simmetrics.metrics.CosineSimilarity;
import org.simmetrics.metrics.Jaro;
import org.simmetrics.metrics.JaroWinkler;
import org.simmetrics.metrics.Levenshtein;
import org.simmetrics.simplifiers.Simplifiers;
import org.simmetrics.tokenizers.Tokenizers;

public class StringSimilarity {

	public float levenshteinCompare(String str1, String str2) {
		StringMetric metric = StringMetricBuilder.with(new Levenshtein()).simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase()).build();

		return metric.compare(str1, str2);
	}

	public float jaroWinklerCompare(String str1, String str2) {
		StringMetric metric = StringMetricBuilder.with(new JaroWinkler()).simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase()).build();

		return metric.compare(str1, str2);
	}
	public float jaroCompare(String str1, String str2) {
		StringMetric metric = StringMetricBuilder.with(new Jaro()).simplify(Simplifiers.removeDiacritics())
				.simplify(Simplifiers.toLowerCase()).build();

		return metric.compare(str1, str2);
	}
	public float cosineDistance(String str1, String str2) {
		StringMetric metric = 
				StringMetricBuilder.with(new CosineSimilarity<String>())
				.tokenize(Tokenizers.whitespace())
				.build();

		return metric.compare(str1, str2);
	}

}
