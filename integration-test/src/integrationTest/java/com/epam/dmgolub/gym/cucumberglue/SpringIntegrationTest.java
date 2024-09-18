package com.epam.dmgolub.gym.cucumberglue;

import com.epam.dmgolub.gym.config.IntegrationTestConfig;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.activemq.ActiveMQContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static com.epam.dmgolub.gym.constant.Constants.GYM_BACKEND_MAIN;
import static com.epam.dmgolub.gym.constant.Constants.GYM_BACKEND_TRAINER_WORKLOAD;

@Import(IntegrationTestConfig.class)
@Testcontainers
@CucumberContextConfiguration
public class SpringIntegrationTest {

	public static final int MONGO_DB_PORT = 27017;
	public static final int MAIN_SERVICE_PORT = 8086;
	@Container
	@ServiceConnection
	public static final MongoDBContainer mongoDBContainer;
	@Container
	public static final GenericContainer<?> mainService;

	private static final String MONGO_DB_IMAGE = "mongo:6.0.10";
	private static final String POSTGRESQL_IMAGE = "pgvector/pgvector:pg16";
	private static final String ACTIVE_MQ_IMAGE = "apache/activemq-classic:5.18.3";
	private static final String MONGO_NETWORK_ALIAS = "mongo";
	private static final String POSTGRES_NETWORK_ALIAS = "postgres";
	private static final String ACTIVE_MQ_NETWORK_ALIAS = "activemq";
	private static final int POSTGRESQL_PORT = 5432;
	private static final int ACTIVE_MQ_PORT = 61616;
	private static final int TRAINER_WORKLOAD_SERVICE_PORT = 8088;
	private static final Network network = Network.newNetwork();

	@Container
	private static final PostgreSQLContainer<?> postgreSQLContainer;
	@Container
	private static final ActiveMQContainer activeMQContainer;
	@Container
	private static final GenericContainer<?> workloadService;

	static {
		mongoDBContainer = new MongoDBContainer(DockerImageName.parse(MONGO_DB_IMAGE))
			.withExposedPorts(MONGO_DB_PORT)
			.withNetwork(network)
			.withNetworkAliases(MONGO_NETWORK_ALIAS);
		mongoDBContainer.start();

		postgreSQLContainer = new PostgreSQLContainer<>(POSTGRESQL_IMAGE)
			.withExposedPorts(POSTGRESQL_PORT)
			.withNetwork(network)
			.withNetworkAliases(POSTGRES_NETWORK_ALIAS)
			.withDatabaseName("gym")
			.withUsername("developer")
			.withPassword("password");
		postgreSQLContainer.start();

		activeMQContainer = new ActiveMQContainer(DockerImageName.parse(ACTIVE_MQ_IMAGE))
			.withExposedPorts(ACTIVE_MQ_PORT)
			.withNetwork(network)
			.withNetworkAliases(ACTIVE_MQ_NETWORK_ALIAS);
		activeMQContainer.start();
		final String activeMQUrl = "tcp://" + ACTIVE_MQ_NETWORK_ALIAS + ":" + ACTIVE_MQ_PORT;

		mainService = new GenericContainer<>(DockerImageName.parse(GYM_BACKEND_MAIN))
			.withExposedPorts(MAIN_SERVICE_PORT)
			.withNetwork(network)
			.withEnv("SPRING_DATASOURCE_URL", "jdbc:postgresql://" + POSTGRES_NETWORK_ALIAS + "/gym")
			.withEnv("SPRING_JPA_HIBERNATE_DDL_AUTO", "create-drop")
			.withEnv("DUMMY_DATA_INITIALIZATION", "true")
			.withEnv("SPRING_ACTIVEMQ_BROKER_URL", activeMQUrl);
		mainService.start();

		workloadService = new GenericContainer<>(DockerImageName.parse(GYM_BACKEND_TRAINER_WORKLOAD))
			.withExposedPorts(TRAINER_WORKLOAD_SERVICE_PORT)
			.withNetwork(network)
			.withEnv("SPRING_DATA_MONGODB_HOST", MONGO_NETWORK_ALIAS)
			.withEnv("SPRING_DATA_MONGODB_PORT", String.valueOf(MONGO_DB_PORT))
			.withEnv("SPRING_ACTIVEMQ_BROKER_URL", activeMQUrl);
		workloadService.start();
	}
}
