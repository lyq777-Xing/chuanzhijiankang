package net.zjitc.security;

import cn.hutool.json.JSONUtil;
import net.zjitc.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("Content-Type","application/json;charset=utf-8");
//        权限不足状态码
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ServletOutputStream outputStream = response.getOutputStream();
        Result result = new Result(false, "权限不足");
        outputStream.write(JSONUtil.toJsonStr(result).getBytes("UTF-8"));

//        PrintWriter writer = response.getWriter();
//        writer.write("{\"status\":\"error\",\"msg\":\"权限不足，请联系管理员\"}");
        outputStream.flush();
        outputStream.close();
//        writer.flush();
//        writer.close();
    }
}
