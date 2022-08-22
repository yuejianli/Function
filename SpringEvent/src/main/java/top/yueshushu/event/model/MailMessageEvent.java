package top.yueshushu.event.model;

import org.springframework.context.ApplicationEvent;

/**
 * 用途描述
 *
 * @author yuejianli
 * @date 2022-08-22
 */

public class MailMessageEvent extends ApplicationEvent {
	private String message;
	
	public MailMessageEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
