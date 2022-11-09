package top.yueshushu.learn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.learn.mapper.PersonMapper;
import top.yueshushu.learn.pojo.AddressDO;
import top.yueshushu.learn.pojo.AreaDO;
import top.yueshushu.learn.pojo.PersonDO;
import top.yueshushu.learn.pojo.PersonDTO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 连表测试类
 * <p>
 * 支持mybatis-plus 查询枚举字段
 * 支持mybatis-plus typeHandle功能
 * <p>
 * 移除了mybatis-plus 逻辑删除支持，逻辑删除需要在连表查询时自己添加对应的条件
 */
@SpringBootTest
@Log4j2
class JoinTest {
    @Resource
    private PersonMapper personMapper;


    /**
     * 简单的关联查询 String
     * <p>
     * <p>
     * SELECT t.id,t.name,t.sex,t.head_img,t.address_id,addr.person_id,addr.area_id,addr.tel,addr.address,a.province,a.city,
     * a.area,a.postcode,a.del FROM `person` t
     * LEFT JOIN Person_address addr on t.id = addr.Person_id
     * LEFT JOIN area a on a.id = addr.area_id
     * WHERE t.del=0 AND (t.id = ?)
     */
    @Test
    void joinSqlTest() {
        MPJQueryWrapper<PersonDO> wrapper = MPJWrappers.<PersonDO>queryJoin()
                // 查询字段， 且对表起别名
                .selectAll(PersonDO.class)
                .selectAll(AddressDO.class, "addr")
                .selectAll(AreaDO.class, "a")
                // 忽略字段
                .selectIgnore("addr.id", "a.id", "t.del", "addr.del")
                // 连接信息，连接字段是编写的。
                .leftJoin("Person_address addr on t.id = addr.Person_id")
                .leftJoin("area a on a.id = addr.area_id")
                .eq("t.id", 1);
        List<PersonDO> list = personMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }


    /**
     * 简单的分页关联查询 lambda
     * <p>
     * 表的别名是自定义的， 出现的先后顺序依次是 t  t1  t2  t3 t4
     * <p>
     * SELECT COUNT(*) AS total FROM `person` t WHERE t.del = 0
     * <p>
     * SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,t1.address,t2.province FROM `person` t
     * LEFT JOIN person_address t1 ON (t1.person_id = t.id)
     * LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=0 LIMIT 10
     */
    @Test
    void joinTest() {
        // 转换成 DTO 信息
        IPage<PersonDTO> iPage = personMapper.selectJoinPage(new Page<>(1, 10), PersonDTO.class,
                MPJWrappers.<PersonDO>lambdaJoin()
                        .selectAll(PersonDO.class)
                        // 查询 其余的字段
                        .select(AddressDO::getAddress)
                        .select(AreaDO::getProvince)
                        // left join 时 进行控制
                        .leftJoin(AddressDO.class, AddressDO::getPersonId, PersonDO::getId)
                        .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId));
        iPage.getRecords().forEach(System.out::println);
    }


    /**
     * 简单的分页关联查询 lambda
     * ON语句多条件
     * <p>
     * SELECT COUNT(*) AS total FROM `person` t LEFT JOIN person_address t1
     * ON (t.id = t1.person_id AND t.id = t1.person_id)
     * WHERE t.del = 0
     * AND (t.id = 1 AND (t.head_img = ‘er’ OR t1.person_id = 1) AND t.id = 1
     */
    @Test
    void joinMoreOnTest() {
        IPage<PersonDTO> page = personMapper.selectJoinPage(new Page<>(1, 10), PersonDTO.class,
                MPJWrappers.<PersonDO>lambdaJoin()
                        .selectAll(PersonDO.class)
                        .select(AddressDO::getAddress)
                        // left join 时 多个条件
                        .leftJoin(AddressDO.class, on -> on
                                .eq(PersonDO::getId, AddressDO::getPersonId)
                                .eq(PersonDO::getId, AddressDO::getPersonId))
                        .eq(PersonDO::getId, 1)
                        .and(i -> i.eq(PersonDO::getHeadImg, "er")
                                .or()
                                .eq(AddressDO::getPersonId, 1))
                        .eq(PersonDO::getId, 1));
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 简单的函数使用
     * <p>
     * SELECT SUM(t.id) AS id,MAX(t.id) AS headImg FROM `person` t
     * LEFT JOIN person_address t1 ON (t1.person_id = t.id) WHERE t.del=0 AND (t.id = ?)
     */
    @Test
    void functionTest() {
        PersonDTO one = personMapper.selectJoinOne(PersonDTO.class, MPJWrappers.<PersonDO>lambdaJoin()
                .selectSum(PersonDO::getId)
                // 映射到相应的字段上
                .selectMax(PersonDO::getId, PersonDTO::getHeadImg)
                .leftJoin(AddressDO.class, AddressDO::getPersonId, PersonDO::getId)
                .eq(PersonDO::getId, 1));
        System.out.println(one);
    }


    /**
     * 忽略个别查询字段
     * <p>
     * SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,
     * t1.person_id,t1.area_id,t1.tel,t1.address,t1.del,t1.address FROM `person` t
     * LEFT JOIN person_address t1 ON (t1.person_id = t.id) WHERE t.del=0 AND (t.id = ?) LIMIT ?
     */
    @Test
    void ignoreIdTest() {
        IPage<PersonDTO> page = personMapper.selectJoinPage(new Page<>(1, 10), PersonDTO.class,
                MPJWrappers.<PersonDO>lambdaJoin()
                        .selectAll(PersonDO.class)
                        // 表示 全部字段，但是忽略 id
                        .select(AddressDO.class, p -> true)
                        .select(AddressDO::getAddress)
                        .leftJoin(AddressDO.class, AddressDO::getPersonId, PersonDO::getId)
                        .eq(PersonDO::getId, 1));
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 关联查询返回map
     * <p>
     * SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,t1.address FROM `person` t
     * LEFT JOIN person_address t1 ON (t1.person_id = t.id) WHERE t.del=0 AND (t.id = ?)
     */
    @Test
    void joinToMapTest() {
        List<Map<String, Object>> list = personMapper.selectJoinMaps(MPJWrappers.<PersonDO>lambdaJoin()
                .selectAll(PersonDO.class)
                .select(AddressDO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getPersonId, PersonDO::getId)
                .eq(PersonDO::getId, 1));
        list.forEach(System.out::println);
    }

    /**
     * 一对多
     * 字段有相同的时，会自动进行处理。
     * SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,
     * t1.id AS mpj_id,t1.person_id,t1.area_id,t1.tel,t1.address,t1.del AS mpj_del FROM `person` t
     * LEFT JOIN person_address t1 ON (t1.person_id = t.id) WHERE t.del=0 ORDER BY t.id DESC
     */
    @Test
    void testJoin() {
        MPJLambdaWrapper<PersonDO> wrapper = new MPJLambdaWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .selectCollection(AddressDO.class, PersonDTO::getAddressList)
                .leftJoin(AddressDO.class, AddressDO::getPersonId, PersonDO::getId)
                .orderByDesc(PersonDO::getId);

        List<PersonDTO> list = personMapper.selectJoinList(PersonDTO.class, wrapper);
        list.forEach(System.out::println);
    }
}
