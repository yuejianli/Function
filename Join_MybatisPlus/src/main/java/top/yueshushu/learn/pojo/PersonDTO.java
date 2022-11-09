package top.yueshushu.learn.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * userDTO
 */
@Data
@ToString
public class PersonDTO {
    /**
     * user
     */
    private Integer id;
    /**
     * user
     */
    private String nameName;
    /**
     * user
     */
    private Integer sex;
    /**
     * user
     */
    private String headImg;
    /**
     * user
     */
    private String userHeadImg;//同 headImg 别名测试
    /**
     * user_address
     */
    private String tel;
    /**
     * user_address
     */
    private String address;
    /**
     * user_address
     */
    private String userAddress;
    /**
     * area
     */
    private String province;
    /**
     * area
     */
    private String city;
    /**
     * area
     */
    private String area;

    private List<AddressDO> addressList;
}
