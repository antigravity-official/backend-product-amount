package antigravity.exception;

public class NotFoundResourceException extends RuntimeException {

	public NotFoundResourceException(Class<?> resource) {
		super(String.format("%s 해당 리소스를 찾을 수 없습니다.", resource.getName()));
	}
}
