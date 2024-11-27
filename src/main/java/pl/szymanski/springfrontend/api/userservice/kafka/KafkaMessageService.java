package pl.szymanski.springfrontend.api.userservice.kafka;


import pl.szymanski.springfrontend.avro.RemoveUserEvent;
import pl.szymanski.springfrontend.avro.UpdateUserEvent;

public interface KafkaMessageService {

	void sendUserUpdateMessage(UpdateUserEvent event);

	void sendUserDeleteMessage(RemoveUserEvent event);

}
