package pl.szymanski.springfrontend.service.impl;

import java.util.Locale;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.service.MessageService;

@Component
public class MessageServiceImpl implements MessageService {

  @Autowired
  private MessageSource messageSource;

  private MessageSourceAccessor accessor;

  @PostConstruct
  private void init() {
    accessor = new MessageSourceAccessor(messageSource, Locale.getDefault());
  }

  @Override
  public String getMessageForCurrentLocale(final String messageCode) {
    return accessor.getMessage(messageCode);
  }
}
