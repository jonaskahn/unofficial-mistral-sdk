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
  public MistralClient(HttpClient.Settings settings) {
    this.client = HttpClient.with()
        .baseUrl(settings.getBaseUrl())
        .connectTimeout(settings.getConnectTimeout())
        .waitTime(settings.getWaitBeforeRetryTime())
        .maxRetries(settings.getMaxRetries())
        .secret(settings.getSecret())
        .instance();
    this.models = new ModelApi(this.client);
    this.chat = new ChatApi(this.client);
  }


  public static MistralClient getClient(HttpClient.Settings settings) {
    return MistralClient.with().settings(settings).instance();
  }

  public static MistralClient getClient(String secret) {
    return getClient(HttpClient.Settings.with().secret(secret).instance());
  }


  @Override
  public void close() {
    this.client.close();
  }
}
