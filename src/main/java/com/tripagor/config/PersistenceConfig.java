package com.tripagor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
@EnableMongoAuditing
public class PersistenceConfig extends AbstractMongoConfiguration {

	private @Value("${db.uri}") String dbUri;
	private @Value("${db.name}") String dbName;

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(new MongoClientURI(this.dbUri));
	}

	@Override
	protected String getDatabaseName() {
		return dbName;
	}

}
