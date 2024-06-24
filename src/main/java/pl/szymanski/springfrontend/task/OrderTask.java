package pl.szymanski.springfrontend.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.szymanski.springfrontend.service.OrderService;

@Component
public class OrderTask {

  @Autowired
  private OrderService orderService;

  @Scheduled(fixedDelay = 1000000)
  public void checkAllUnCollectedOrders() {
    orderService.checkAllUncollectedOrders();
  }

}
