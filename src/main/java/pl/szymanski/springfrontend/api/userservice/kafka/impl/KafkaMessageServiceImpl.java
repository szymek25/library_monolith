package pl.szymanski.springfrontend.api.userservice.kafka.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import pl.szymanski.springfrontend.api.userservice.kafka.KafkaMessageService;
import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.springfrontend.avro.UpdatePasswordEvent;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

@Service
public class KafkaMessageServiceImpl implements KafkaMessageService {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageServiceImpl.class);
	@Autowired
	private KafkaTemplate<String, UpdateUserEvent> kafkaTemplateUpdateUser;

	@Autowired
	private KafkaTemplate<String, RemoveUserEvent> kafkaTemplateRemoveUser;

	@Autowired
	private KafkaTemplate<String, UpdatePasswordEvent> kafkaTemplateUpdatePassword;

	@Value("${library.kafka.topics.user-updates}")
	private String userUpdatesTopic;

	@Value("${library.kafka.topics.user-removes}")
	private String userRemovesTopic;

	@Value("${library.kafka.topics.update-password}")
	private String updatePasswordTopic;

	@Override
	public void sendUserUpdateMessage(UpdateUserEvent event) {
		ListenableFuture<SendResult<String, UpdateUserEvent>> send = kafkaTemplateUpdateUser.send(userUpdatesTopic, event.getId(), event);
		send.addCallback(result -> LOG.debug("Sent message: {}", result), ex -> LOG.error("Failed to send message", ex));
	}

	@Override
	public void sendUserDeleteMessage(RemoveUserEvent event) {
		ListenableFuture<SendResult<String, RemoveUserEvent>> send = kafkaTemplateRemoveUser.send(userRemovesTopic, event.getId(), event);
		send.addCallback(result -> LOG.debug("Sent message: {}", result), ex -> LOG.error("Failed to send message", ex));
	}

	@Override
	public void sendUpdatePasswordMessage(UpdatePasswordEvent event) {
		ListenableFuture<SendResult<String, UpdatePasswordEvent>> send = kafkaTemplateUpdatePassword.send(updatePasswordTopic, event.getId(), event);
		send.addCallback(result -> LOG.debug("Sent message: {}", result), ex -> LOG.error("Failed to send message", ex));
	}
}
