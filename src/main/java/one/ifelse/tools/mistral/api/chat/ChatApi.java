package one.ifelse.tools.mistral.api.chat;

import one.ifelse.tools.mistral.http.HttpClient;

public class ChatApi {

  private final HttpClient client;

  public ChatApi(HttpClient client) {
    this.client = client;
  }
}
