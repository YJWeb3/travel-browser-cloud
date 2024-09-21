package com.zheng.travel.browser.auth.interceptor;

import com.zheng.travel.browser.auth.anno.RequireLogin;
import com.zheng.travel.browser.auth.config.JwtProperties;
import com.zheng.travel.browser.auth.util.AuthenticationUtils;
import com.zheng.travel.browser.core.exception.BusinessException;
import com.zheng.travel.browser.redis.utils.RedisCache;

import com.zheng.travel.browser.user.redis.key.UserRedisKeyPrefix;
import com.zheng.travel.browser.user.vo.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final RedisCache redisCache;
    private final JwtProperties jwtProperties;

    public LoginInterceptor(RedisCache redisCache, JwtProperties jwtProperties) {
        this.redisCache = redisCache;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 0. 判断一个接口是否需要进行登录拦截
        // 0.1 判断 handler 是否是 HandlerMethod 实例, 如果不是, 直接放行
        if (!(handler instanceof HandlerMethod)) {
            // handler => 静态资源
            // handler => CORS 的预请求
            return true;
        }

        // 0.2 将 handler 对象转换为 HandlerMethod 对象
        HandlerMethod hm = (HandlerMethod) handler;
        // 0.3 从 HandlerMethod 对象中获取对应的 Controller 对象
        Class<?> controllerClass = hm.getBeanType();
        // 0.4 分别从 Controller 和 HandlerMethod 上获取 @RequireLogin 注解
        RequireLogin classAnno = controllerClass.getAnnotation(RequireLogin.class);
        RequireLogin methodAnno = hm.getMethodAnnotation(RequireLogin.class);
        // 0.5 如果一个都拿不到, 直接放行
        if (classAnno == null && methodAnno == null) {
            return true;
        }

        // 1. 从请求头中拿到 jwt token
        String token = request.getHeader(LoginUser.TOKEN_HEADER);
        // 2. 基于 jwt sdk 解析 token, 如果解析失败直接抛出异常
        try {
            Jws<Claims> jwt = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret())
                    .parseClaimsJws(token);
            // 3. 获取 token 中登录时间数据, 判断是否已经过期
            Claims claims = jwt.getBody();
            String uuid = (String) claims.get(LoginUser.LOGIN_USER_REDIS_UUID);
            // 4. 从 redis 中获取数据, 如果能拿到说明没有过期, 如果拿不到, 说明已经过期了
            String userLoginKey = UserRedisKeyPrefix.USER_LOGIN_INFO_STRING.fullKey(uuid);
            LoginUser loginUser = redisCache.getCacheObject(userLoginKey);
            long loginTime;
            if (loginUser == null) {
                // 5. 如果已经过期, 抛出异常
                throw new BusinessException(401, "token 已失效");
            } else if ((loginUser.getExpireTime() - (loginTime = System.currentTimeMillis())) <= LoginUser.TWENTY_MILLISECONDS) {
                // 6. 如果用户过期剩余时间小于 20 分钟, 就刷新该用户的过期时间
                loginUser.setLoginTime(loginTime);
                long expireTime = jwtProperties.getExpireTime() * LoginUser.MINUTES_MILLISECONDS;
                long fullExpireTime = loginTime + expireTime;
                loginUser.setExpireTime(fullExpireTime);
                // 重新计算过期时间后, 再次设置到 Redis 中, 就覆盖原来 Redis 中的对象
                redisCache.setCacheObject(userLoginKey, loginUser, expireTime, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            log.warn("[登录拦截] jwt token 解析失败", e);
            throw new BusinessException(401, "用户未认证");
        }

        // 5. 其他情况就放行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求执行完成以后准备响应之前执行
        // 线程即将完成本次请求, 先将当前线程空间内存储的数据清除掉
        AuthenticationUtils.removeUser();
    }
}
