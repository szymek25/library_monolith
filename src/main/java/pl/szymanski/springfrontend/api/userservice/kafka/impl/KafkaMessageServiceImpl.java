package pl.szymanski.springfrontend.api.userservice.kafka.impl;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import pl.szymanski.springfrontend.api.userservice.kafka.KafkaMessageService;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

import javax.annotation.Nullable;

@Service
public class KafkaMessageServiceImpl implements KafkaMessageService {

	private static final Logger LOG = LoggerFactory.getLogger(KafkaMessageServiceImpl.class);
	@Autowired
	private KafkaTemplate<String, UpdateUserEvent> kafkaTemplate;

	@Value("${library.kafka.topics.user-updates}")
	private String userUpdatesTopic;

	@Override
	public void sendUserUpdateMessage(UpdateUserEvent event) {
		ListenableFuture<SendResult<String, UpdateUserEvent>> send = kafkaTemplate.send(userUpdatesTopic, event.getId(), event);
		send.addCallback(result -> LOG.debug("Sent message: {}", result), ex -> LOG.error("Failed to send message", ex));
	}
}
