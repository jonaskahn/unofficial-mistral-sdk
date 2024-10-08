package one.ifelse.tools.mistral;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import one.ifelse.tools.mistral.command.models.ModelCommand;
import one.ifelse.tools.mistral.http.HttpClient;

@Accessors(fluent = true)
@Getter
public final class MistralClient implements AutoCloseable {

  private final ModelCommand models;

  @Builder(builderMethodName = "with", buildMethodName = "instance")
  public MistralClient(HttpClient.Settings config) {
    var client = HttpClient.with()
        .baseUrl(config.getBaseUrl())
        .connectTimeout(config.getConnectTimeout())
        .waitTime(config.getWaitBeforeRetryTime())
        .maxRetries(config.getMaxRetries())
        .secret(config.getSecret())
        .instance();
    this.models = new ModelCommand(client);
  }


  public static MistralClient getClient(HttpClient.Settings config) {
    return MistralClient.with().config(config).instance();
  }

  public static MistralClient getClient(String secret) {
    return getClient(HttpClient.Settings.with().secret(secret).instance());
  }


  @Override
  public void close() throws Exception {
    this.models.close();
  }
}
