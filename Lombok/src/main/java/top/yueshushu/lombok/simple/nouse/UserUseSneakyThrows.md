
@SneakyThrows 注解，用于抛出异常，对异常进行处理。 

可以用来替代 在方法上 throws Exception 

// 指定具体的异常
	@SneakyThrows(UnsupportedEncodingException.class)
	public String utf8ToString(byte[] bytes) {
		return new String(bytes, "UTF-8");
	}
	
	// 抛出异常
	@SneakyThrows
	public void run() {
		throw new Throwable();
	}


1. 会生成: 

public String utf8ToString(byte[] bytes) {
        try {
            return new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            throw var3;
        }
    }

    public void run() {
        try {
            throw new Throwable();
        } catch (Throwable var2) {
            throw var2;
        }
    }