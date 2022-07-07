Lombok 的 依赖:


 <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.16.20</version>
        </dependency>
        
 常用的注解是:
 
 @Data
 @Setter
 @Getter
 @ToString
 @Equals
 @Builder
 @NonNull
 @NoArgsConstructor
 @AllArgsConstructor
 @RequiredArgsConstructor
 @Slf4j
 @Cleanup
 @SneakyThrows
 @Synchronized
 
 
 工作原理:
会发现在Lombok使用的过程中，只需要添加相应的注解，无需再为此写任何代码。自动生成的代码到底是如何产生的呢？

核心之处就是对于注解的解析上。JDK5引入了注解的同时，也提供了两种解析方式。

运行时解析
运行时能够解析的注解，必须将@Retention设置为RUNTIME，这样就可以通过反射拿到该注解。java.lang,reflect反射包中提供了一个接口AnnotatedElement，该接口定义了获取注解信息的几个方法，Class、Constructor、Field、Method、Package等都实现了该接口，对反射熟悉的朋友应该都会很熟悉这种解析方式。

编译时解析
编译时解析有两种机制，分别简单描述下：

1）Annotation Processing Tool

apt自JDK5产生，JDK7已标记为过期，不推荐使用，JDK8中已彻底删除，自JDK6开始，可以使用Pluggable Annotation Processing API来替换它，apt被替换主要有2点原因：

api都在com.sun.mirror非标准包下
没有集成到javac中，需要额外运行
2）Pluggable Annotation Processing API

JSR 269自JDK6加入，作为apt的替代方案，它解决了apt的两个问题，javac在执行的时候会调用实现了该API的程序，这样我们就可以对编译器做一些增强，这时javac执行的过程如下：

![image](https://img-blog.csdn.net/20160908130644281)

Lombok本质上就是一个实现了“JSR 269 API”的程序。在使用javac的过程中，它产生作用的具体流程如下：

javac对源代码进行分析，生成了一棵抽象语法树（AST）
运行过程中调用实现了“JSR 269 API”的Lombok程序
此时Lombok就对第一步骤得到的AST进行处理，找到@Data注解所在类对应的语法树（AST），然后修改该语法树（AST），增加getter和setter方法定义的相应树节点
javac使用修改后的抽象语法树（AST）生成字节码文件，即给class增加新的节点（代码块）
拜读了Lombok源码，对应注解的实现都在HandleXXX中，比如@Getter注解的实现时HandleGetter.handle()。还有一些其它类库使用这种方式实现，比如Google Auto、Dagger等等。


 
 
 优点:
 
 Lombok能以简单的注解形式来简化java代码，提高开发人员的开发效率。
 例如开发中经常需要写的javabean，都需要花时间去添加相应的getter/setter，也许还要去写构造器、equals等方法，而且需要维护，
 当属性多时会出现大量的getter/setter方法，这些显得很冗长也没有太多技术含量，一旦修改属性，就容易出现忘记修改对应方法的失误。
 
 Lombok能通过注解的方式，在编译时自动为属性生成构造函数、getter/setter、equals、hashcode、toString等方法。
 奇妙之处在于源码中没有getter和setter方法，但是在编译生成的字节码文件中有getter和setter方法。
 这样就省去了手动重建这些代码的麻烦，使代码看起来更简洁明了。
 
 
 缺点:
 Lombok的优点显而易见，可以帮助我们省去很多冗余代码，实际上，从我个人角度来看，Java开发项目中，并不推荐使用Lombok，
 但潘老师还是介绍了它的使用方法，因为在一些公司存在这样的使用场景，下面我们来看一下潘老师为什么不推荐使用Lombok，它都有哪些缺点？
 1） 高侵入性，强迫队友
 
 Lombok插件的使用，要求开发者一定要在IDE中安装对应的插件。不仅自己要安装，任何和你协同开发的人都要安装。
 如果有谁未安装插件的话，使用IDE打开一个基于Lombok的项目的话会提示找不到方法等错误，导致项目编译失败。
 更重要的是，如果我们定义的一个jar包中使用了Lombok，那么就要求所有依赖这个jar包的所有应用都必须安装插件，这种侵入性是很高的。
 
 2）代码可调试性降低
 
 Lombok确实可以帮忙减少很多代码，因为Lombok会帮忙自动生成很多代码。但是，这些代码是要在编译阶段才会生成的，
 所以在开发的过程中，其实很多代码其实是缺失的。这就给代码调试带来一定的问题，我们想要知道某个类中的某个属性的getter方法都被哪些类引用的话，就没那么简单了。
 
 3） 影响版本升级
 
 Lombok对于代码有很强的侵入性，就可能带来一个比较大的问题，那就是会影响我们对JDK的升级。按照如今JDK的升级频率，
 每半年都会推出一个新的版本，但是Lombok作为一个第三方工具，并且是由开源团队维护的，那么他的迭代速度是无法保证的。
 所以，如果我们需要升级到某个新版本的JDK的时候，若其中的特性在Lombok中不支持的话就会受到影响。还有一个可能带来的问题，
 就是Lombok自身的升级也会受到限制。因为一个应用可能依赖了多个jar包，而每个jar包可能又要依赖不同版本的Lombok，这就导致在应用中需要做版本仲裁，
 而我们知道，jar包版本仲裁是没那么容易的，而且发生问题的概率也很高。
 
 4）注解使用有风险
 
 在使用Lombok过程中，如果对于各种注解的底层原理不理解的话，很容易产生意想不到的结果。举一个简单的例子：我们知道，
 当我们使用@Data定义一个类的时候，会自动帮我们生成equals()方法 。但是如果只使用了@Data，而不使用@EqualsAndHashCode(callSuper=true)的话，
 会默认是@EqualsAndHashCode(callSuper=false)，这时候生成的equals()方法只会比较子类的属性，不会考虑从父类继承的属性，无论父类属性访问权限是否开放，
 这就可能得到意想不到的结果。
 
 5）可能会破坏封装性
 
 使用过程中如果不小心，在一定程度上就会破坏代码的封装性
       