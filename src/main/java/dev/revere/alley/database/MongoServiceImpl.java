package dev.revere.alley.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 21:40
 */
@Getter
@Service(provides = MongoService.class, priority = 30)
public class MongoServiceImpl implements MongoService {
    private final ConfigService configService;

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * Constructor for DI. Receives the IConfigService from the AlleyContext.
     */
    public MongoServiceImpl(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        FileConfiguration dbConfig = configService.getDatabaseConfig();
        String uri = dbConfig.getString("mongo.uri");
        String databaseName = dbConfig.getString("mongo.database");

        if (uri == null || uri.isEmpty() || databaseName == null || databaseName.isEmpty()) {
            Logger.error("MongoDB URI or database name is not configured in database.yml.");
            throw new IllegalStateException("MongoDB configuration is missing.");
        }

        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .retryWrites(true)
                    .build();

            this.mongoClient = MongoClients.create(settings);
            this.mongoDatabase = this.mongoClient.getDatabase(databaseName);

            this.mongoDatabase.listCollectionNames().first();
        } catch (Exception e) {
            Logger.error("Failed to connect to MongoDB. Please check your credentials and network access.");
            throw new RuntimeException("MongoDB Connection Failure", e);
        }
    }

    @Override
    public void shutdown(AlleyContext context) {
        if (this.mongoClient != null) {
            this.mongoClient.close();
            Logger.info("MongoDB connection closed.");
        }
    }

    @Override
    public MongoDatabase getMongoDatabase() {
        if (this.mongoDatabase == null) {
            throw new IllegalStateException("MongoService has not been initialized or failed to connect.");
        }
        return this.mongoDatabase;
    }
}