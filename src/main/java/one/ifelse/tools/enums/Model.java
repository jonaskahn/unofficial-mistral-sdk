package one.ifelse.tools.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Model {

  MISTRAL_LARGE("mistral-large-latest", 128_000, ModelType.PREMIER),
  MISTRAL_SMALL("mistral-small-latest", 64_000, ModelType.PREMIER),
  CODESTRAL("codestral-latest", 32_000, ModelType.PREMIER),
  MISTAL_EMBED("mistral-embed-latest", 8000, ModelType.PREMIER),

  PIXTRAL("pixtral-12b-2409", 128_000, ModelType.FREE),
  MISTRAL_NEMO("mistral-nemo-latest", 128_000, ModelType.FREE),
  CODESTRAL_MAMBA("open-codestral-mamba", 256_000, ModelType.FREE),
  MATHSTRAL("open-mathstral", 32_000, ModelType.FREE),

  MISTRAL_7B("open-mistral-7b", 32_000, ModelType.LEGACY),
  MISTRAL_8x7B("open-mixtral-8x7b", 32_000, ModelType.LEGACY),
  MISTRAL_22x7B("open-mixtral-8x22b", 64_000, ModelType.LEGACY);

  private final String code;
  private final Integer maxTokens;
  private final ModelType type;

}

