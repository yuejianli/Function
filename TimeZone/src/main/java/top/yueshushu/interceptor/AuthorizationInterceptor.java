package top.yueshushu.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import top.yueshushu.aop.AuthToken;
import top.yueshushu.common.Global;
import top.yueshushu.entity.User;
import top.yueshushu.mapper.UserMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Method;


@Slf4j
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String REQUEST_CURRENT_KEY = "REQUEST_CURRENT_KEY";

    public static final String X_REAL_IP = "x-real-ip";

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        String ip = request.getHeader(X_REAL_IP);
        if (method.getAnnotation(AuthToken.class) != null || handlerMethod.getBeanType().getAnnotation(AuthToken.class) != null) {

            String token = request.getHeader("Authorization");
            if (StringUtils.isEmpty(token)) {
                isFalse(response);
                return false;
            }

            // 模拟获取 User
            Integer userId = 0;
            if ("1".equals(token)) {
                userId = 1;
            } else if ("2".equals(token)) {
                userId = 2;
            } else if ("3".equals(token)) {
                userId = 3;
            }
            User currentUser = userMapper.selectById(userId);
            if (!StringUtils.hasText(token) || currentUser == null) {
                isFalse(response);
                return false;
            }
            if (currentUser != null) {
                Global.setTransfer("user", currentUser);
                Global.setTransfer("token", token);
                Global.setTransfer("userName", currentUser.getName());
                Global.setTransfer("ip", ip == null ? request.getRemoteAddr() : ip);
                // 客户端传递过来的时区
                Global.setTransfer("timezone", request.getHeader("TimeZone"));
            }
        }
        request.setAttribute(REQUEST_CURRENT_KEY, null);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Global.release();
    }

    private void isFalse(HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        PrintWriter out = null;
        try {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("utf-8");
            jsonObject.put("code", "401");
            jsonObject.put("message", "登录已过期,请重新登录");
            out = response.getWriter();
            out.println(jsonObject);
        } catch (Exception e) {
            log.error("发生异常", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }
}
