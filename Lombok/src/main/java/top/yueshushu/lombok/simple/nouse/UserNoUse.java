package top.yueshushu.lombok.simple.nouse;

import java.io.Serializable;
import java.util.Objects;

/**
 * 不使用 lombok 时
 *
 * @author yuejianli
 * @date 2022-07-07
 */

public class UserNoUse implements Serializable {
	// 属性
	private Integer id;
	private String name;
	private String description;
	
	public UserNoUse() {
	
	}
	
	public UserNoUse(Integer id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserNoUse that = (UserNoUse) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(name, that.name);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "UserNoUse{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
