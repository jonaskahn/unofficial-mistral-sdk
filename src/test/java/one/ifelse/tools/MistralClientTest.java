package one.ifelse.tools;

import org.junit.jupiter.api.Test;


public class MistralClientTest {

  @Test
  public void testClient() {
    Log4jConfigurator.configure();
    MistralClient client = MistralClient.getClient("hjtyvSP5rs6xPjBw89qiEIQ5dlztgMwL");
    client.getModels();
  }
}
