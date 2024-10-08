package one.ifelse.tools.mistral;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import one.ifelse.tools.mistral.api.chat.ChatApi;
import one.ifelse.tools.mistral.api.models.ModelApi;
import one.ifelse.tools.mistral.http.HttpClient;

@Accessors(fluent = true)
@Getter
public final class MistralClient implements AutoCloseable {

  private final HttpClient client;
  private final ModelApi models;
  private final ChatApi chat;

  @Builder(builderMethodName = "with", buildMethodName = "instance")
  public MistralClient(HttpClient.Settings config) {
    this.client = HttpClient.with()
        .baseUrl(config.getBaseUrl())
        .connectTimeout(config.getConnectTimeout())
        .waitTime(config.getWaitBeforeRetryTime())
        .maxRetries(config.getMaxRetries())
        .secret(config.getSecret())
        .instance();
    this.models = new ModelApi(this.client);
    this.chat = new ChatApi(this.client);
  }


  public static MistralClient getClient(HttpClient.Settings config) {
    return MistralClient.with().config(config).instance();
  }

  public static MistralClient getClient(String secret) {
    return getClient(HttpClient.Settings.with().secret(secret).instance());
  }


  @Override
  public void close() {
    this.client.close();
  }
}
