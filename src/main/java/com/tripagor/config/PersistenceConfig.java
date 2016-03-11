package com.tripagor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class PersistenceConfig extends AbstractMongoConfiguration {

	private @Value("${db.uri}") String dbUri;
	private @Value("${db.name}") String dbName;
	private @Value("${db.username}") String dbUsername;
	private @Value("${db.password}") String dbPassword;

	@Override
	public Mongo mongo() throws Exception {
		return new MongoClient(new MongoClientURI(this.dbUri));
	}

	@Override
	protected String getDatabaseName() {
		return this.dbName;
	}

}
