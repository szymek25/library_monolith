package pl.szymanski.springfrontend.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

	private static final String SCHEMA_REGISTRY_URL_KEY = "schema.registry.url";
	@Value("${library.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Value("${library.kafka.topics.user-updates}")
	private String userUpdatesTopic;

	@Value("${library.kafka.schema-registry-url-key}")
	private String schemaRegistryUrlKey;



	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic libraryUserUpdatesTopic() {
		return new NewTopic(userUpdatesTopic, 4, (short) 1);
	}

	@Bean
	public ProducerFactory<String, UpdateUserEvent> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				StringSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				KafkaAvroSerializer.class);
		configProps.put(SCHEMA_REGISTRY_URL_KEY, schemaRegistryUrlKey);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, UpdateUserEvent> kafkaTemplateUpdateUser() {
		return new KafkaTemplate<>(producerFactory());
	}
}
