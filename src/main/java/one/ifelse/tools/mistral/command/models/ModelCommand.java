package one.ifelse.tools.mistral.command.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import kong.unirest.core.GenericType;
import lombok.RequiredArgsConstructor;
import one.ifelse.tools.mistral.http.HttpClient;

@RequiredArgsConstructor
public class ModelCommand implements AutoCloseable {


  private final HttpClient client;

  @Override
  public void close() throws Exception {
    this.client.close();
  }

  public List<Model> list() {
    return this.client.get("/models", new GenericType<Response>() {
        })
        .map(Response::getData)
        .orElseGet(List::of);
  }

  public CompletableFuture<List<Model>> asyncList() {
    final var responseType = new GenericType<Response>() {
    };
    return this.client.asyncGet("/models", responseType)
        .thenApply(response -> response.map(Response::getData).orElseGet(List::of));
  }

  public Optional<Model> retrieve(String id) {
    final var responseType = new GenericType<Model>() {
    };
    return this.client.get("/models/{id}", Map.of("id", id), responseType);
  }

  public CompletableFuture<Optional<Model>> asyncRetrieve(String id) {
    final var responseType = new GenericType<Model>() {
    };
    return this.client.asyncGet("/models/{id}", Map.of("id", id), responseType);
  }
}
