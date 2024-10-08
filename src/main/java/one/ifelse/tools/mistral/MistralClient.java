package one.ifelse.tools.mistral;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import one.ifelse.tools.mistral.api.chat.ChatApi;
import one.ifelse.tools.mistral.api.models.ModelApi;
import one.ifelse.tools.mistral.http.HttpClient;
import one.ifelse.tools.mistral.http.HttpClient.Proxy;

@Accessors(fluent = true)
@Getter
public final class MistralClient implements AutoCloseable {

  private final HttpClient client;
  private final ModelApi models;
  private final ChatApi chat;

  @Builder(builderMethodName = "with", buildMethodName = "instance")
  public MistralClient(Settings settings) {
    this.client = HttpClient.with()
        .baseUrl(settings.baseUrl)
        .connectTimeout(settings.connectTimeout)
        .waitTime(settings.waitBeforeRetryTime)
        .maxRetries(settings.maxRetries)
        .secret(settings.secret)
        .instance();
    this.models = new ModelApi(this.client);
    this.chat = new ChatApi(this.client);
  }


  public static MistralClient getClient(Settings settings) {
    return MistralClient.with().settings(settings).instance();
  }

  public static MistralClient getClient(String secret) {
    return getClient(Settings.with().secret(secret).instance());
  }

  public static MistralClient getClient() {
    final String secret = System.getenv().get("MISTRAL_API_KEY").trim();
    return getClient(secret);
  }


  @Override
  public void close() {
    this.client.close();
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @Builder(builderMethodName = "with", buildMethodName = "instance")
  @Getter
  @ToString
  @Accessors(fluent = true)
  public static class Settings {

    private final String baseUrl;
    @ToString.Exclude
    private final String secret;
    private final Integer connectTimeout;
    private final Long waitBeforeRetryTime;
    private final Integer maxRetries;
    private final Proxy proxy;
  }
}
