package one.ifelse.tools.mistral;

import org.junit.jupiter.api.Test;


public class MistralClientTest {

  @Test
  public void testClient() {
    one.ifelse.tools.mistral2.Log4jConfigurator.configure();
    try (MistralClient client = MistralClient.getClient("hjtyvSP5rs6xPjBw89qiEIQ5dlztgMwL")) {
      client.models().retrieve("open-mistral-7b2");
    }
  }
}
