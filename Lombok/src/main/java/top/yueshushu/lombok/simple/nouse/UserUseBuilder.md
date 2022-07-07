
@Builder 注解，会生成建造者。

创建构造时:

UserUseBuilder userUseBuilder = UserUseBuilder.builder()
										.id(1)
										.name("builder 建造者")
										.description("描述")
										.build();
										



package top.yueshushu.lombok.simple.nouse;

public class UserUseBuilder {
    private Integer id;
    private String name;
    private String description;

    UserUseBuilder(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // 调用  builder 方法 
    public static UserUseBuilder.UserUseBuilderBuilder builder() {
        return new UserUseBuilder.UserUseBuilderBuilder();
    }

    // setting getting, hashcode equals 

    public String toString() {
        return "UserUseBuilder(id=" + this.getId() + ", name=" + this.getName() + ", description=" + this.getDescription() + ")";
    }

    public static class UserUseBuilderBuilder {
        private Integer id;
        private String name;
        private String description;

        UserUseBuilderBuilder() {
        }

        public UserUseBuilder.UserUseBuilderBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public UserUseBuilder.UserUseBuilderBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserUseBuilder.UserUseBuilderBuilder description(String description) {
            this.description = description;
            return this;
        }

        // 后面调用  build 方法（） 调用的是构造方法。 
        public UserUseBuilder build() {
            return new UserUseBuilder(this.id, this.name, this.description);
        }

        public String toString() {
            return "UserUseBuilder.UserUseBuilderBuilder(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ")";
        }
    }
}