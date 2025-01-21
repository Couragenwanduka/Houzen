package Houzen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoRepositories(basePackages = "Houzen.Repository")
public class MongoConfig {

    // Specify the database name here
    private static final String DATABASE_NAME = "Houzen"; // Replace with your actual database name

    @Bean
    public MongoClient mongoClient() {
        // Use the MongoDB Atlas connection string
        String connectionString = "mongodb+srv://courageobunike:yYA4XYqFn4OFIqfq@cluster0.bn5yw.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), DATABASE_NAME);
    }
}
