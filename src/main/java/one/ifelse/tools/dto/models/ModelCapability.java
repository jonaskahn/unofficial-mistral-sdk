package one.ifelse.tools.dto.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModelCapability {

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