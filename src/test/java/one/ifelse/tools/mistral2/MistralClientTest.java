package one.ifelse.tools.mistral2;

import one.ifelse.tools.mistral.MistralClient;
import one.ifelse.tools.mistral.command.models.ModelCommand;
import org.junit.jupiter.api.Test;


public class MistralClientTest {

  @Test
  public void testClient() {
    ModelCommand a = new ModelCommand(null);
    Log4jConfigurator.configure();
    try (MistralClient client = MistralClient.getClient("hjtyvSP5rs6xPjBw89qiEIQ5dlztgMwL")) {

      client.models().asyncRetrieve("open-mistral-7b").join().ifPresent(System.out::println);
      System.out.println("DONE");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


  }
}
