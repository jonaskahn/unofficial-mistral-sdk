package one.ifelse.tools.mistral.api.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import kong.unirest.core.GenericType;
import lombok.RequiredArgsConstructor;
import one.ifelse.tools.mistral.http.HttpClient;

@RequiredArgsConstructor
public class ModelApi {

  private final HttpClient client;

  /**
   * Lists all available models.
   *
   * @return a list of models; never null
   */
  public List<Model> list() {
    return this.client.get("/models", new GenericType<ModelResponse>() {
        })
        .map(ModelResponse::getData)
        .orElseGet(List::of);
  }

  /**
   * Lists all available models, asynchronously.
   *
   * @return a list of models; never null
   */
  public CompletableFuture<List<Model>> asyncList() {
    final var responseType = new GenericType<ModelResponse>() {
    };
    return this.client.asyncGet("/models", responseType)
        .thenApply(response -> response.map(ModelResponse::getData).orElseGet(List::of));
  }

  /**
   * Retrieves a model by id.
   *
   * @param id the id of the model
   * @return the model if it exists, or an empty optional if it does not
   */
  public Optional<Model> retrieve(String id) {
    final var responseType = new GenericType<Model>() {
    };
    return this.client.get("/models/{id}", Map.of("id", id), responseType);
  }

  /**
   * Retrieves a model by id, asynchronously.
   *
   * @param id the id of the model
   * @return a future that completes with the model if it exists, or an empty optional if it does
   * not
   */
  public CompletableFuture<Optional<Model>> asyncRetrieve(String id) {
    final var responseType = new GenericType<Model>() {
    };
    return this.client.asyncGet("/models/{id}", Map.of("id", id), responseType);
  }
}
