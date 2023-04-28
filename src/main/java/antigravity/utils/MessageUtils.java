package antigravity.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;


@Component
public class MessageUtils {
	private static MessageSourceAccessor messageSource = null;

	@Autowired
    public void setMessageSourceAccessor(@Qualifier("BusinessMessageSource")MessageSourceAccessor messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    public static String getMessage(String code) {
        return messageSource.getMessage(code, "", LocaleContextHolder.getLocale());
    }
}
