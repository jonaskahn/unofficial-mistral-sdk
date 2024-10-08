package one.ifelse.tools;

import java.util.List;
import kong.unirest.core.GenericType;
import lombok.Builder;
import one.ifelse.tools.dto.Configuration;
import one.ifelse.tools.dto.models.ModelData;
import one.ifelse.tools.http.HttpClient;


public final class MistralClient implements AutoCloseable {

  private final Configuration config;
  private final HttpClient client;


  @Builder(builderMethodName = "with", buildMethodName = "instance")
  public MistralClient(Configuration config) {
    this.config = config;
    this.client = HttpClient.with()
        .baseUrl(config.getBaseUrl())
        .connectTimeout(config.getConnectTimeout())
        .waitTime(config.getWaitBeforeRetryTime())
        .maxRetries(config.getMaxRetries())
        .secret(config.getSecret())
        .instance();
  }


  public static MistralClient getClient(Configuration config) {
    return MistralClient.with().config(config).instance();
  }

  public static MistralClient getClient(String secret) {
    return getClient(Configuration.with().secret(secret).instance());
  }

  public Object getModels() {
    var data = client.get("models", new GenericType<List<ModelData>>() {
    });
    return data;
  }

  @Override
  public void close() throws Exception {
    client.close();
  }
}
