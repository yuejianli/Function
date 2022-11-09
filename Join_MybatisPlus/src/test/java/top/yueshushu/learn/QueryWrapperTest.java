package top.yueshushu.learn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.yueshushu.learn.mapper.PersonMapper;
import top.yueshushu.learn.pojo.PersonDO;
import top.yueshushu.learn.pojo.PersonDTO;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * left join  时，还是 写 sql 语句， 不太好。
 */
@SpringBootTest
@Log4j2
class QueryWrapperTest {
    @Resource
    private PersonMapper personMapper;

    /**
     * 单个表处理 查询
     */
    @Test
    void test1() {

        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del FROM `person` t WHERE t.del=0 LIMIT 1
        PersonDO PersonDO = personMapper.selectJoinOne(PersonDO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class).last("LIMIT 1"));
        System.out.println(PersonDO);

        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,name AS nameName FROM `person` t WHERE t.del=0 LIMIT 1
        PersonDTO dto = personMapper.selectJoinOne(PersonDTO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .select("name AS nameName")
                .last("LIMIT 1"));
        System.out.println(dto);
    }

    /**
     * 两个表简单的  join 查询
     */
    @Test
    void test2() {
        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del FROM `person` t
        // LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id <= 10)
        List<PersonDO> PersonDO = personMapper.selectJoinList(PersonDO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .le("t.id ", 10));
        System.out.println(PersonDO);

        //  SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,t2.address AS PersonAddress FROM `person` t
        //  LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id <= ?)
        List<PersonDTO> dto = personMapper.selectJoinList(PersonDTO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .select("t2.address AS PersonAddress")
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .le("t.id ", 10));
        System.out.println(dto);
    }

    @Test
    void test3() {
        // SELECT COUNT(*) AS total FROM `person` t WHERE t.del = 0 AND (t.id < 5)
        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del FROM `person` t LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id < 5) LIMIT 10
        IPage<PersonDO> PersonDO = personMapper.selectJoinPage(new Page<>(1, 10), PersonDO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .lt("t.id ", 5));
        System.out.println(PersonDO);

        // SELECT COUNT(*) AS total FROM `person` t WHERE t.del = 0 AND (t.id < 5)
        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,t2.address AS PersonAddress FROM `person` t
        // LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id < 5) LIMIT 10
        IPage<PersonDTO> dto = personMapper.selectJoinPage(new Page<>(1, 10), PersonDTO.class, new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .select("t2.address AS PersonAddress")
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .lt("t.id ", 5));
        System.out.println(dto);
    }

    @Test
    void test4() {
        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del FROM `person` t
        // LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id < 5)
        List<Map<String, Object>> maps = personMapper.selectJoinMaps(new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .lt("t.id ", 5));
        System.out.println(maps);
        // SELECT t.id,t.name,t.sex,t.head_img,t.address_id,t.del,t2.address AS PersonAddress FROM `person` t
        // LEFT JOIN Person_address t2 on t2.Person_id = t.id WHERE t.del=0 AND (t.id < 5)
        List<Map<String, Object>> joinMaps = personMapper.selectJoinMaps(new MPJQueryWrapper<PersonDO>()
                .selectAll(PersonDO.class)
                .select("t2.address AS PersonAddress")
                .leftJoin("Person_address t2 on t2.Person_id = t.id")
                .lt("t.id ", 5));
        System.out.println(joinMaps);
    }

}
