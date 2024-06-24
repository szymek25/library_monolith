package pl.szymanski.springfrontend.config;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebUtilsImpl implements WebUtils {

  @Autowired
  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  private HttpServletRequest request;

  @Override
  public String getClientIp() {
    String remoteAddr = "";

    if (request != null) {
      remoteAddr = request.getHeader("X-FORWARDED-FOR");
      if (remoteAddr == null || "".equals(remoteAddr)) {
        remoteAddr = request.getRemoteAddr();
      }
    }

    return remoteAddr;
  }
}
