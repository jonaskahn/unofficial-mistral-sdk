package one.ifelse.tools.mistral;

import one.ifelse.tools.mistral.api.models.ModelApi;
import org.junit.jupiter.api.Test;


public class MistralClientTest {

  @Test
  public void testClient() {
    ModelApi a = new ModelApi(null);
    one.ifelse.tools.mistral2.Log4jConfigurator.configure();
    try (MistralClient client = MistralClient.getClient("hjtyvSP5rs6xPjBw89qiEIQ5dlztgMwL")) {
      client.models().asyncRetrieve("open-mistral-7b2").join();
    }
  }
}
