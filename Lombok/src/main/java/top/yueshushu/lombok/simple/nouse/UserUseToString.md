@ToString  注解在类上, 会将相关的属性全部打印出来。

of属性，表示要展示的属性，  exclude 表示排除的属性。 通常只使用排除的属性。



@ToString 
public String toString() {
        return "UserUseToString(id=" + this.id + ", name=" + this.name + ", description=" + this.description + ", MIN_AGE=" + this.MIN_AGE + ")";
    }
    
    
    
@ToString(of = {"id","name"},exclude = {"description","MIN_AGE"})
  
   public String toString() {
          return "UserUseToString(id=" + this.id + ", name=" + this.name + ")";
      }  