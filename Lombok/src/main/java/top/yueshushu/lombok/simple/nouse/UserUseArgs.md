构建方法

@NoArgsConstructor 生成无参的构造方法

@AllArgsConstructor 生成全部参数的构造方法， 会按照定义属性的顺序当成构造方法的顺序处理。 


@RequiredArgsConstructor  对 @NonNull 注解的属性和 final 常量的属性进行构建。 


如果定义的是:
	@NonNull
	private String name;
	private String description;
	private final Integer MIN_AGE = 18;
	 
那么会生成: 	 
	  
1. 


 public UserUseArgs(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UserUseArgs() {
    }
    
 2. 
 
 public UserUseArgs(Integer id, @NonNull String name, String description) {
         if (name == null) {
             throw new NullPointerException("name");
         } else {
             this.id = id;
             this.name = name;
             this.description = description;
         }
     }
 
     public UserUseArgs() {
     }
 
     public UserUseArgs(@NonNull String name) {
         if (name == null) {
             throw new NullPointerException("name");
         } else {
             this.name = name;
         }
     }   