package one.ifelse.tools.mistral.http;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import kong.unirest.core.Config;
import kong.unirest.core.GenericType;
import kong.unirest.core.HttpRequest;
import kong.unirest.core.HttpRequestSummary;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Interceptor;
import kong.unirest.core.RetryStrategy;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestException;
import kong.unirest.core.UnirestInstance;
import kong.unirest.modules.jackson.JacksonObjectMapper;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import one.ifelse.tools.mistral.exception.MistralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient is a class that provides a simple way to make HTTP requests. It implements the
 * AutoCloseable interface to ensure that the underlying Unirest instance is properly closed when it
 * is no longer needed.
 */
@Getter
public final class HttpClient implements AutoCloseable {

  private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
  private final UnirestInstance instance;

  @Builder(builderMethodName = "with", buildMethodName = "instance")
  private HttpClient(
      Proxy proxy,
      String baseUrl,
      String secret,
      Integer connectTimeout,
      Long waitTime,
      Integer maxRetries) {
    this.instance = Unirest.spawnInstance();
    this.instance.config().connectTimeout(Objects.isNull(connectTimeout) ? 5000 : connectTimeout);
    this.instance.config().retryAfter(new RetryStrategy() {
      @Override
      public boolean isRetryable(HttpResponse<?> httpResponse) {
        return true;
      }

      @Override
      public long getWaitTime(HttpResponse<?> httpResponse) {
        return Objects.isNull(waitTime) ? 1000L : waitTime;
      }

      @Override
      public int getMaxAttempts() {
        return Objects.isNull(maxRetries) ? 3 : maxRetries;
      }
    });
    this.instance.config().defaultBaseUrl("https://api.mistral.ai/v1").retryAfter(true, 3);
    if (proxy != null) {
      this.instance.config()
          .proxy(
              proxy.getHost(),
              proxy.getPort(),
              proxy.getUsername(),
              proxy.getPassword()
          );
    }
    if (baseUrl != null) {
      this.instance.config().defaultBaseUrl(baseUrl);
    }

    this.instance.config().followRedirects(true);
    if (Objects.isNull(secret) || secret.trim().isEmpty()) {
      throw new MistralException(400, "Secret API Key must be provided");
    }
    this.instance.config().addDefaultHeader("User-Agent", "MistralClient-Java-0.1.0");
    this.instance.config().addDefaultHeader("Authorization", "Bearer " + secret);
    this.instance.config().interceptor(new Interceptor() {
      @Override
      public void onRequest(HttpRequest<?> request, Config config) {
        Interceptor.super.onRequest(request, config);
      }

      @Override
      public void onResponse(HttpResponse<?> response, HttpRequestSummary request, Config config) {
        response.ifFailure(String.class, e -> {
          log.debug(
              "\n===================================\nRequest:\n{}\nResponse: \n{}",
              e.getRequestSummary().asString(), e.getBody());
          throw new MistralException(e.getStatus(), e.getBody());
        });
        Interceptor.super.onResponse(response, request, config);
      }

      @Override
      public HttpResponse<?> onFail(Exception e, HttpRequestSummary request, Config config)
          throws UnirestException {
        if (e instanceof MistralException) {
          throw (MistralException) e;
        }
        throw new MistralException(400, e);
      }
    });
    this.instance.config().setObjectMapper(new JacksonObjectMapper(
        JsonMapper.builder()
            .findAndAddModules()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .enable(SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE)
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .build())
    );
  }


  /**
   * Performs a GET request with the given path.
   *
   * @param path the path to query
   * @param type the type of the response
   * @return an optional with the response body, or empty if the response was a 404
   */
  public <T> Optional<T> get(String path, GenericType<T> type) {
    String refinedPath = path.startsWith("/") ? path : "/" + path;
    return Optional.ofNullable(
        this.getInstance()
            .get(refinedPath)
            .asObject(type)
            .getBody()
    );
  }


  /**
   * Performs a GET request and returns the response body as an object of type {@code T}.
   *
   * @param path   the path to send the request to
   * @param params the route parameters to pass to the request
   * @param type   the type to read the response body as
   * @return the response body as an object of type {@code T}, if the request was successful; an
   * empty optional if the request failed
   */
  public <T> Optional<T> get(String path, Map<String, Object> params, GenericType<T> type) {
    String refinedPath = path.startsWith("/") ? path : "/" + path;
    if (Objects.isNull(params) || params.isEmpty()) {
      return this.get(refinedPath, type);
    }
    return Optional.ofNullable(
        this.getInstance()
            .get(refinedPath)
            .routeParam(params)
            .asObject(type)
            .getBody()
    );
  }


  /**
   * Sends a POST request with the given body to the given path.
   *
   * @param path the path to send the request to
   * @param body the body to send in the request
   * @return the response body as an object of type {@code R}
   */
  public <T, R> R post(String path, T body) {
    final var type = new GenericType<R>() {
    };
    String refinedPath = path.startsWith("/") ? path : "/" + path;
    return this.getInstance()
        .post(refinedPath)
        .body(body)
        .asObject(type)
        .getBody();
  }

  @Override
  public void close() {
    instance.close();
  }

  @Data
  public static class Proxy {

    private final String host;
    private final Integer port;
    @ToString.Exclude
    private final String username;
    private final String password;

    @Builder(builderMethodName = "config", buildMethodName = "instance")
    public Proxy(String host, Integer port, String username, String password) {
      this.host = host;
      this.port = port;
      this.username = username;
      this.password = password;
    }
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  @Builder(builderMethodName = "with", buildMethodName = "instance")
  @Data
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


