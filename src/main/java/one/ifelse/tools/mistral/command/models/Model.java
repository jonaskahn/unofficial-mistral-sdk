package one.ifelse.tools.mistral.command.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model {

  @JsonProperty("id")
  private String id;
  @JsonProperty("object")
  private String object;
  @JsonProperty("created")
  private long created;
  @JsonProperty("owned_by")
  private String ownedBy;
  @JsonProperty("name")
  private String name;
  @JsonProperty("description")
  private String description;
  @JsonProperty("max_context_length")
  private int maxContextLength;
  @JsonProperty("aliases")
  private List<String> aliases;
  @JsonProperty("deprecation")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private ZonedDateTime deprecation;
  @JsonProperty("capabilities")
  private Capability capabilities;
  @JsonProperty("type")
  private String type;


  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public static class Capability {

    @JsonProperty("completion_chat")
    private boolean completionChat;

    @JsonProperty("completion_fim")
    private boolean completionFim;

    @JsonProperty("function_calling")
    private boolean functionCalling;

    @JsonProperty("fine_tuning")
    private boolean fineTuning;

    @JsonProperty("vision")
    private boolean vision;
  }
}