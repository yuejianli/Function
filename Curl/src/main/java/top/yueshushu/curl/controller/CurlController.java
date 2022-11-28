package top.yueshushu.curl.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.yueshushu.curl.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * curl 配置处理
 *
 * @author yuejianli
 * @date 2022-11-28
 */
@RestController
public class CurlController {

    @RequestMapping("/getOne")
    public String getOne() {
        return "一个普通的Get请求";
    }

    @RequestMapping("/getTwo")
    public String getTwo(String name) {
        return "一个普通的Get请求,名称是:"+name;
    }

    @RequestMapping("/getThree")
    public String getThree(String name,String sex) {
        return "一个普通的Get请求,名称是:"+name+",性别是:"+sex;
    }

    @RequestMapping("/getFour/{name}/{sex}")
    public String getFour(@PathVariable("name") String name, @PathVariable("sex") String sex) {
        return "一个普通的 path variable 请求,名称是:"+name+",性别是:"+sex;
    }

    @RequestMapping("/getFive")
    public String getFive(String name, String sex, HttpServletRequest httpServletRequest) {
        // 获取请求头
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)){
            return "未传入请求头 Authorization";
        }
        if (!"abcd".equals(authorization)){
            return "传入了错误的请求头";
        }
        return "一个普通的 path variable 请求,名称是:"+name+",性别是:"+sex;
    }


    @RequestMapping("/getSix")
    public String getSix(HttpServletRequest httpServletRequest) {
        // 获取请求头
        String authorization = httpServletRequest.getHeader("Authorization");
        String referer = httpServletRequest.getHeader("Referer");

        return "返回请求头 Authorization :" +authorization+",Referer:"+referer;
    }


    @PostMapping("/postOne")
    public String postOne(@RequestBody User user) {
        return "一个普通的 post 请求,名称是:"+ user.getName()+",性别是:"+ user.getSex();
    }

    @PostMapping("/postTwo")
    public String postTwo(@RequestBody User user,HttpServletRequest httpServletRequest) {
        // 获取请求头
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)){
            return "未传入请求头 Authorization";
        }
        if (!"abcd".equals(authorization)){
            return "传入了错误的请求头";
        }

        return "一个普通的 post 请求,名称是:"+ user.getName()+",性别是:"+ user.getSex();
    }




    @PutMapping("/putTwo")
    public String putTwo(@RequestBody User user,HttpServletRequest httpServletRequest) {
        // 获取请求头
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)){
            return "未传入请求头 Authorization";
        }
        if (!"abcd".equals(authorization)){
            return "传入了错误的请求头";
        }

        return "一个普通的 put 请求,名称是:"+ user.getName()+",性别是:"+ user.getSex();
    }

    @DeleteMapping("/deleteTwo")
    public String deleteTwo(@RequestBody User user,HttpServletRequest httpServletRequest) {
        // 获取请求头
        String authorization = httpServletRequest.getHeader("Authorization");
        if (!StringUtils.hasText(authorization)){
            return "未传入请求头 Authorization";
        }
        if (!"abcd".equals(authorization)){
            return "传入了错误的请求头";
        }

        return "一个普通的 delete 请求,名称是:"+ user.getName()+",性别是:"+ user.getSex();
    }


    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return "上传文件成功,文件名是:"+file.getOriginalFilename();
    }


}
