package one.ifelse.tools.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(builderMethodName = "with", buildMethodName = "instance")
@Data
public class Configuration {

  private final String baseUrl;
  private final String secret;
  private final Integer connectTimeout;
  private final Long waitBeforeRetryTime;
  private final Integer maxRetries;
  private final ProxySettings proxySettings;
}
