package pl.szymanski.springfrontend.controllers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.szymanski.springfrontend.facade.OrderFacade;

public abstract class AbstractPageController {

  public static final String ERROR_MESSAGE_HOLDER = "errorMessageHolder";
  public static final String CONF_MESSAGE_HOLDER = "confMessageHolder";
  protected static final String REDIRECT_PREFIX = "redirect:";
  private static final Logger LOG = LoggerFactory.getLogger(AbstractPageController.class);
  public static final String URL_PARAMS_TO_PASS = "urlParamsToPass";

  @Autowired
  private OrderFacade orderFacade;

  @ModelAttribute("newOrders")
  public int newOrdersCount() {
    return orderFacade.countNewOrders();
  }

  protected void addPaginationResult(final Integer currentPage, final String modelAttributeName,
      final Page pageableResult, final Model model) {
    model.addAttribute(modelAttributeName, pageableResult.getContent());
    model.addAttribute("pages", pageableResult.getTotalPages());
    model.addAttribute("currentPage", currentPage);
  }

  protected void addPaginationResult(final Integer currentPage, final String modelAttributeName,
      final Page pageableResult, final Model model, final Map<String, String> urlParamsToPass) {
    addPaginationResult(currentPage, modelAttributeName, pageableResult, model);
    if (MapUtils.isNotEmpty(urlParamsToPass)) {
      final StringBuilder formattedParams = new StringBuilder();
      final Iterator<Entry<String, String>> iterator = urlParamsToPass.entrySet().iterator();
      while (iterator.hasNext()) {
        final Entry<String, String> next = iterator.next();
        formattedParams.append(next.getKey());
        formattedParams.append("=");
        formattedParams.append(next.getValue());
        if (iterator.hasNext()) {
          formattedParams.append("&");
        }
      }
      model.addAttribute(URL_PARAMS_TO_PASS, formattedParams.toString());
    }

  }

  protected void addGlobalErrorMessage(final String messageCode, final Model model) {
    addMessage(ERROR_MESSAGE_HOLDER, messageCode, model);
  }

  protected void addGlobalConfMessage(final String messageCode, final Model model) {
    addMessage(CONF_MESSAGE_HOLDER, messageCode, model);
  }

  protected void addFlashConfMessage(final String messageCode,
      final RedirectAttributes redirectAttributes) {
    addFlashMessage(CONF_MESSAGE_HOLDER, messageCode, redirectAttributes);
  }

  protected void addFlashErrorMessage(final String messageCode,
      final RedirectAttributes redirectAttributes) {
    addFlashMessage(ERROR_MESSAGE_HOLDER, messageCode, redirectAttributes);
  }

  private void addMessage(final String messageHolder, final String messageCode, final Model model) {
    final List<String> newMessageList = new ArrayList<>();
    newMessageList.add(messageCode);
    if (model.containsAttribute(messageHolder)) {
      try {
        List<String> messages = (List) model.asMap().get(messageHolder);
        final List<String> copiedList = new ArrayList<>(messages);
        copiedList.add(messageCode);
        model.addAttribute(messageHolder, copiedList);

      } catch (ClassCastException e) {
        LOG.warn("{} message holder hadn`t list of messages, value will be overwritten",
            messageHolder);
        model.addAttribute(messageHolder, newMessageList);
      }
    } else {
      model.addAttribute(messageHolder, newMessageList);
    }
  }

  private void addFlashMessage(final String messageHolder, final String messageCode,
      final RedirectAttributes redirectAttributes) {
    final List<String> newMessageList = new ArrayList<>();
    newMessageList.add(messageCode);
    if (redirectAttributes.containsAttribute(messageHolder)) {
      try {
        List<String> messages = (List) redirectAttributes.asMap().get(messageHolder);
        final List<String> copiedList = new ArrayList<>(messages);
        copiedList.add(messageCode);
        redirectAttributes.addFlashAttribute(messageHolder, copiedList);

      } catch (ClassCastException e) {
        LOG.warn("{} message holder hadn`t list of messages, value will be overwritten",
            messageHolder);
        redirectAttributes.addFlashAttribute(messageHolder, newMessageList);
      }
    } else {
      redirectAttributes.addFlashAttribute(messageHolder, newMessageList);
    }
  }
}
