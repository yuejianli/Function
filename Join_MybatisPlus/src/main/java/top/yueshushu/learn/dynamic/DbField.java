package top.yueshushu.learn.dynamic;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xx
 */
@Data
@EqualsAndHashCode
public final class DbField {
    private String className;
    private String classFieldName;
    private String tableName;
    private String tableFieldName;

    public DbField(String className, String classFieldName, String tableName, String tableFieldName) {
        this.className = className;
        this.classFieldName = classFieldName;
        this.tableName = tableName;
        this.tableFieldName = tableFieldName;
    }
}
