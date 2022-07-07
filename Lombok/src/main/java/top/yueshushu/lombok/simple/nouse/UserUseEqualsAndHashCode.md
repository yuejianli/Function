@EqualsAndHashCode 注解，放置在类上。

可以通过  of 值 指定 使用哪些属性， exclude 排除某些属性。
通常，使用 of 即可
@EqualsAndHashCode(of = {"name"},exclude = {"id","description"})	

1. 

public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof UserUseEqualsAndHashCode)) {
            return false;
        } else {
            UserUseEqualsAndHashCode other = (UserUseEqualsAndHashCode)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$id = this.id;
                    Object other$id = other.id;
                    if (this$id == null) {
                        if (other$id == null) {
                            break label47;
                        }
                    } else if (this$id.equals(other$id)) {
                        break label47;
                    }

                    return false;
                }

                Object this$name = this.name;
                Object other$name = other.name;
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$description = this.description;
                Object other$description = other.description;
                if (this$description == null) {
                    if (other$description != null) {
                        return false;
                    }
                } else if (!this$description.equals(other$description)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof UserUseEqualsAndHashCode;
    }

    public int hashCode() {
        int PRIME = true;
        int result = 1;
        Object $id = this.id;
        int result = result * 59 + ($id == null ? 43 : $id.hashCode());
        Object $name = this.name;
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $description = this.description;
        result = result * 59 + ($description == null ? 43 : $description.hashCode());
        return result;
    }
    
 2. 
 
 
 public boolean equals(Object o) {
         if (o == this) {
             return true;
         } else if (!(o instanceof UserUseEqualsAndHashCode)) {
             return false;
         } else {
             UserUseEqualsAndHashCode other = (UserUseEqualsAndHashCode)o;
             if (!other.canEqual(this)) {
                 return false;
             } else {
                 Object this$name = this.name;
                 Object other$name = other.name;
                 if (this$name == null) {
                     if (other$name != null) {
                         return false;
                     }
                 } else if (!this$name.equals(other$name)) {
                     return false;
                 }
 
                 return true;
             }
         }
     }
 
     protected boolean canEqual(Object other) {
         return other instanceof UserUseEqualsAndHashCode;
     }
 
     public int hashCode() {
         int PRIME = true;
         int result = 1;
         Object $name = this.name;
         int result = result * 59 + ($name == null ? 43 : $name.hashCode());
         return result;
     }   