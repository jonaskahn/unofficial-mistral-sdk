package one.ifelse.tools.http;


import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
import lombok.Builder;
import lombok.Getter;
import one.ifelse.tools.dto.ProxySettings;
import one.ifelse.tools.exception.MistralException;
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
      ProxySettings proxySettings,
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
    if (proxySettings != null) {
      this.instance.config()
          .proxy(
              proxySettings.getHost(),
              proxySettings.getPort(),
              proxySettings.getUsername(),
              proxySettings.getPassword()
          );
    }
    if (baseUrl != null) {
      this.instance.config().defaultBaseUrl(baseUrl);
    }

    this.instance.config().followRedirects(true);
    if (Objects.isNull(secret) || secret.trim().isEmpty()) {
      throw new MistralException("Secret API Key must be provided");
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
        log.debug("Request:\n{}", response.getRequestSummary().asString());
        log.debug("Response:\n  - Status: {}\n  - Body: {}",
            response.getStatus(),
            response.getBody()
        );
        int status = response.getStatus();
        if (status <= 400) {
          Interceptor.super.onResponse(response, request, config);
        } else if (status == 401) {
          throw new MistralException("Unauthorized");
        } else {
          throw new MistralException("Request failed with status " + status);
        }
      }

      @Override
      public HttpResponse<?> onFail(Exception e, HttpRequestSummary request, Config config)
          throws UnirestException {
        log.warn(e.getLocalizedMessage());
        return Interceptor.super.onFail(e, request, config);
      }
    });
  }


  /**
   * Make a GET request to the given path, relative to the configured base URL.
   *
   * @param path the path to request
   * @return the response body, deserialized to a type of T
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
   * Make a GET request to the given path, relative to the configured base URL, with query
   * parameters.
   *
   * @param path   the path to request
   * @param params query parameters to include in the request
   * @return the response body, deserialized to a type of T
   */
  public <T> Optional<T> get(String path, Map<String, Object> params, GenericType<T> type) {
    if (Objects.isNull(params) || params.isEmpty()) {
      return this.get(path, type);
    }
    return Optional.ofNullable(
        this.getInstance()
            .get("/" + path)
            .queryString(params)
            .asObject(type)
            .getBody()
    );
  }

  /**
   * Make a GET request to the given path, relative to the configured base URL, asynchronously.
   *
   * @param path the path to request
   * @return a future that completes with the response body, deserialized to a type of T
   */
  public <T> CompletableFuture<T> asyncGet(String path) {
    return this.getInstance()
        .get("/" + path)
        .asObjectAsync(new GenericType<T>() {
        })
        .thenApply(HttpResponse::getBody);
  }


  /**
   * Make a GET request to the given path, relative to the configured base URL, with query
   * parameters, asynchronously.
   *
   * @param path   the path to request
   * @param params query parameters to include in the request
   * @return a future that completes with the response body, deserialized to a type of T
   */
  public <T> CompletableFuture<T> asyncGet(String path, Map<String, Object> params) {
    if (Objects.isNull(params) || params.isEmpty()) {
      return this.asyncGet(path);
    }
    return this.getInstance()
        .get(path)
        .queryString(params)
        .asObjectAsync(new GenericType<T>() {
        })
        .thenApply(HttpResponse::getBody);
  }


  /**
   * Make a POST request to the given path, relative to the configured base URL, with the given
   * body.
   *
   * @param path the path to request
   * @param body the body to send with the request
   * @return the response body, deserialized to a type of R
   */
  public <T, R> R post(String path, T body) {
    return this.getInstance()
        .post(path)
        .body(body)
        .asObject(new GenericType<R>() {
        })
        .getBody();
  }

  /**
   * Make a POST request to the given path, relative to the configured base URL, with the given
   * body, asynchronously.
   *
   * @param path the path to request
   * @param body the body to send with the request
   * @return a future that completes with the response body, deserialized to a type of R
   */
  public <T, R> CompletableFuture<R> asyncPost(String path, T body) {
    return this.getInstance()
        .post(path)
        .body(body)
        .asObjectAsync(new GenericType<R>() {
        })
        .thenApply(HttpResponse::getBody);
  }

  @Override
  public void close() {
    instance.close();
  }
}


