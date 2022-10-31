package top.yueshushu.common;

import com.github.pagehelper.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 修改自com.github.pagehelper.PageInfo
 * <p>
 * 对Page<E>结果进行包装
 * <p/>
 * 新增分页的多项属性，主要参考:http://bbs.csdn.net/topics/360010907
 *
 * @author liuzh/abel533/isea533
 * @version 3.3.0
 * @since 3.2.2
 * 项目地址 : http://git.oschina.net/free/Mybatis_PageHelper
 */
@Data
public class BasePage<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalElements;


    private List<T> list;

    public BasePage(List<T> list, int totalElements) {
        this.totalElements = totalElements;
        this.list = list;
    }

    public BasePage(List<T> list, Page<Object> page) {
        if (list != null) {
            this.totalElements = page.getTotal();
            this.list = list;
        }
    }

    public BasePage(com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page) {
        this.totalElements = page.getTotal();
        this.list = page.getRecords();
    }
}