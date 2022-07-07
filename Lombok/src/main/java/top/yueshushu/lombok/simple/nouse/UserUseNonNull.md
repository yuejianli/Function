@NonNull 注解，不为空

name 属性不能为空， 会变成构造函数 里面的参数。 

	@NonNull
	private String name;
	
	UserUseNonNull userUseNonNull = new UserUseNonNull("名称");	

会进行编辑,生成

    @NonNull
    private String name;	
	
	

