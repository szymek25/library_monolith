package pl.szymanski.springfrontend.api.userservice.kafka;

import pl.szymanski.springfrontend.api.userservice.events.UpdateUserEvent;

public interface KafkaMessageService {

	void sendUserUpdateMessage(UpdateUserEvent event);
}
