
@Setter  @Getter  注解， 用于生成 Setting Getting 方法。

可以放置在类上， 表示类中的属性生成，
也可以放置在具体的属性上， 表示属性生成。 


其中， value 可以指定方法的 级别。 表示方法级别是 protected 
@Setter(value= AccessLevel.PROTECTED)

如果是 final 常量，则只会生成 getter 方法，不会生成 setting 方法。

	@Setter
	@Getter
	private final Integer MIN_AGE = 18;



1. package top.yueshushu.lombok.simple.nouse;

public class UserUseSetGet {
    private Integer id;
    private String name;
    private String description;

    public UserUseSetGet() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected String getName() {
        return this.name;
    }

    private void setDescription(String description) {
        this.description = description;
    }

    private String getDescription() {
        return this.description;
    }
}

2. 
 public Integer getMIN_AGE() {
        return this.MIN_AGE;
    }