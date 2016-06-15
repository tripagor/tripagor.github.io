package com.tripagor;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.KeywordExporterSeleniumImpl;
import com.tripagor.locations.CityRepository;
import com.tripagor.locations.model.City;
import com.tripagor.locations.model.Keyword;
import com.tripagor.locations.model.KeywordResearchResults;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class HotelCitiesKeywordByCountryExport {
	private CityRepository cityRepository;
	private KeywordExporterSeleniumImpl exporter;;
	private final Map<Integer, String> propMap = new HashMap<Integer, String>();
	private final Map<String, String> propertyReplaceMap = new HashMap<String, String>();
	private TsvParser parser;
	private final Logger logger = LoggerFactory.getLogger(HotelCitiesKeywordByCountryExport.class);
	private String keywordPrefix = "hotels";

	@Before
	public void before() throws Exception {
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		parser = new TsvParser(settings);

		String mongoUri = "mongodb://localhost:27017/hotels";
		// String mongoUri =
		// "mongodb://admin:vsjl1801@db.tripagor.com/hotels?authMechanism=SCRAM-SHA-1&authSource=admin";

		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		cityRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(CityRepository.class);
		// exporter = new KeywordExporterSeleniumImpl("admin@pickito.de",
		// "sensor2016", "c:\\temp\\keywords");

	}

	@Test
	public void doExport() throws Exception {
		List<City> cities = cityRepository.findByCountryCode("de");

		List<String> keywords = new LinkedList<>();
		for (City city : cities) {
			keywords.add(keywordPrefix + " " + city.getName());
		}

		// List<File> files = exporter.doExport(keywords);
		File[] files = new File("C://temp/keywords").listFiles();

		for (File file : files) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>" + file.getName());
			doExtractKeywordResults(file);
		}

	}

	private void doExtractKeywordResults(File file) {
		List<String[]> rows = parser.parseAll(file, Charset.forName("UTF-16LE"));
		int failed = 0;
		int successful = 0;

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						String[] values = rows.get(i);

						Map<String, Object> valueMap = new HashMap<>();
						for (int j = 0; j < values.length; j++) {
							if (values[j] != null) {
								valueMap.put(propMap.get(j), toObject(getType(propMap.get(j)), values[j]));
							}
						}

						String keywordStr = (String) valueMap.get("Keyword");
						City city = cityRepository.findByName(keywordStr.substring((keywordPrefix + " ").length()));
						if (city != null) {
							List<Keyword> keywords = new LinkedList<>();
							if (city.getKeywordResearchResults() != null
									&& city.getKeywordResearchResults().getKeywords() != null) {
								for (Keyword current : city.getKeywordResearchResults().getKeywords()) {
									if (current.getName() != null && !keywordPrefix.equals(current.getName())) {
										keywords.add(current);
									}
								}
							}

							KeywordResearchResults keywordResearchResults = new KeywordResearchResults();

							Keyword keyword = new Keyword();
							keyword.setCompetition((Double) valueMap.get("Competition"));
							keyword.setMonthlySearches((Long) valueMap.get("Avg. Monthly Searches (exact match only)"));
							keyword.setSuggestedBid((Double) valueMap.get("Suggested bid"));
							keyword.setCurrencyCode((String) valueMap.get("Currency"));
							keyword.setName(keywordPrefix);
							keywords.add(keyword);

							keywordResearchResults.setKeywords(keywords);

							city.setKeywordResearchResults(keywordResearchResults);
							cityRepository.save(city);
							successful++;
						}
					} catch (Exception e) {
						failed++;
						continue;
					}
				} else {
					String[] propertyNames = rows.get(i);
					for (int j = 0; j < propertyNames.length; j++) {
						String propertyName = propertyNames[j];
						if (this.propertyReplaceMap.get(propertyName) != null) {
							propertyName = this.propertyReplaceMap.get(propertyName);
						}
						propMap.put(j, propertyName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		}

		logger.debug("successful:" + successful + " failed:" + failed);
	}

	@SuppressWarnings("rawtypes")
	private Class getType(String columnName) {
		Class clazz = String.class;
		if ("Avg. Monthly Searches (exact match only)".equals(columnName)) {
			return Double.class;
		} else if ("Competition".equals(columnName)) {
			return Double.class;
		} else if ("Suggested bid".equals(columnName)) {
			return Double.class;
		}
		return clazz;
	}

	private Object toObject(@SuppressWarnings("rawtypes") Class clazz, String value) {
		if (Boolean.class == clazz)
			return Boolean.parseBoolean(value);
		else if (Byte.class == clazz)
			return Byte.parseByte(value);
		else if (Short.class == clazz)
			return Short.parseShort(value);
		else if (Integer.class == clazz)
			return Integer.parseInt(value);
		else if (Long.class == clazz)
			return Long.parseLong(value);
		else if (Float.class == clazz)
			return Float.parseFloat(value);
		else if (Double.class == clazz) {
			try {
				Number number = NumberFormat.getNumberInstance(Locale.GERMAN).parse(value.replaceAll("\"", ""));
				return number;
			} catch (ParseException e) {
				throw new IllegalArgumentException(e);
			}
		} else if (BigDecimal.class == clazz)
			return new BigDecimal(value).doubleValue();
		else if (String.class == clazz)
			return value;
		return value;
	}

}
