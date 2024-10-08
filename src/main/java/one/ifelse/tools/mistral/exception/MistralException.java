package one.ifelse.tools.mistral.exception;


public class MistralException extends RuntimeException {

  private final Integer code;

  public MistralException(Integer code, String message) {
    super(message);
    this.code = code;
  }

  public MistralException(Integer code, Exception exception) {
    super(exception);
    this.code = code;
  }

}
