@Synchronized  注解，用于生成同步锁方法


public class UserUseSynchronized {
	// 需要设置添加  readLock 属性名称不能改
	private final Object readLock = new Object();
	
	@Synchronized
	public static void hello() {
		System.out.println("world");
	}
	
	@Synchronized
	public int answerToLife() {
		return 42;
	}
	
	@Synchronized("readLock")
	public void foo() {
		System.out.println("bar");
	}
}



会生成:

public class UserUseSynchronized {
    private static final Object $LOCK = new Object[0];
    private final Object $lock = new Object[0];
    private final Object readLock = new Object();

    public UserUseSynchronized() {
    }

    public static void hello() {
        synchronized($LOCK) {
            System.out.println("world");
        }
    }

    public int answerToLife() {
        synchronized(this.$lock) {
            return 42;
        }
    }

    public void foo() {
        synchronized(this.readLock) {
            System.out.println("bar");
        }
    }
}
