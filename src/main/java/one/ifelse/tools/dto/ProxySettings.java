package one.ifelse.tools.dto;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
public class ProxySettings {

  private final String host;
  private final Integer port;
  @ToString.Exclude
  private final String username;
  private final String password;

  @Builder(builderMethodName = "config", buildMethodName = "instance")
  public ProxySettings(String host, Integer port, String username, String password) {
    this.host = host;
    this.port = port;
    this.username = username;
    this.password = password;
  }
}
