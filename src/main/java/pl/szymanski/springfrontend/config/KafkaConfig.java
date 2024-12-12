package pl.szymanski.springfrontend.config;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.springfrontend.avro.UpdatePasswordEvent;
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

	@Value("${library.kafka.topics.user-removes}")
	private String userRemovesTopic;

	@Value("${library.kafka.schema-registry-url-key}")
	private String schemaRegistryUrlKey;

	@Value("${library.kafka.topics.update-password}")
	private String updatePasswordTopic;


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
	public NewTopic libraryUserRemovesTopic() {
		return new NewTopic(userRemovesTopic, 4, (short) 1);
	}

	@Bean
	public NewTopic updatePasswordTopic() {
		return new NewTopic(updatePasswordTopic, 4, (short) 1);
	}


	@Bean
	public ProducerFactory<Integer, UpdateUserEvent> updateUserProducerFactory() {
		Map<String, Object> configProps = avroConfigProps();
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<Integer, UpdateUserEvent> updateUserKafkaTemplate() {
		return new KafkaTemplate<>(updateUserProducerFactory());
	}

	@Bean
	public ProducerFactory<Integer, RemoveUserEvent> removeUserProducerFactory() {
		Map<String, Object> configProps = avroConfigProps();
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<Integer, RemoveUserEvent> removeUserKafkaTemplate() {
		return new KafkaTemplate<>(removeUserProducerFactory());
	}

	@Bean
	public ProducerFactory<Integer, UpdatePasswordEvent> updatePasswordProducerFactory() {
		Map<String, Object> configProps = avroConfigProps();
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<Integer, UpdatePasswordEvent> updatePasswordKafkaTemplate() {
		return new KafkaTemplate<>(updatePasswordProducerFactory());
	}

	private Map<String, Object> avroConfigProps() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
				bootstrapAddress);
		configProps.put(
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				IntegerSerializer.class);
		configProps.put(
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				KafkaAvroSerializer.class);
		configProps.put(SCHEMA_REGISTRY_URL_KEY, schemaRegistryUrlKey);
		return configProps;
	}
}
