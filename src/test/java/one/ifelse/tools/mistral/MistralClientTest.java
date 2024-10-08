package one.ifelse.tools.mistral;

import one.ifelse.tools.mistral.api.models.Model;
import org.junit.jupiter.api.Test;


public class MistralClientTest {

  @Test
  public void testClient() {
    one.ifelse.tools.mistral2.Log4jConfigurator.configure();
    try (MistralClient client = MistralClient.getClient("hjtyvSP5rs6xPjBw89qiEIQ5dlztgMwL")) {
      client.models().list().stream().map(Model::getId).forEach(System.out::println);
    }
  }
}
