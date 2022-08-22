package top.yueshushu.event.model;

import org.springframework.context.ApplicationEvent;

/**
 * 事件
 *
 * @author yuejianli
 * @date 2022-08-22
 */
public class OrderEvent extends ApplicationEvent {
	private String message;
	
	public OrderEvent(Object source, String message) {
		super(source);
		this.message = message;
	}
	
	public OrderEvent(Object source) {
		super(source);
	}
	
	public String getMessage() {
		return message;
	}
}
