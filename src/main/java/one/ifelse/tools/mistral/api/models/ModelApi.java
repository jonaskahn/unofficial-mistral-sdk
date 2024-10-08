package one.ifelse.tools.mistral.api.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

}
