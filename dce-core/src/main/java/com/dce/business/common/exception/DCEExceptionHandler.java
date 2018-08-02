package com.dce.business.common.exception;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.dce.business.common.result.Result;

public class DCEExceptionHandler implements HandlerExceptionResolver {

    @Override
    @SuppressWarnings("unused")
    public ModelAndView resolveException( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        if (ex instanceof IllegalArgumentException || ex instanceof BusinessException) {
            print(response, Result.failureCode, ex.getMessage());
        } else {
            print(response, Result.failureCode, "系统繁忙，请稍后再试");
        }
        return null;
    }

    private void print(HttpServletResponse response, String code, String msg) {
        Result<?> result = Result.result(code, msg, null);
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("utf-8");
        PrintWriter pw = null;
        try {
            pw = response.getWriter();
            pw.print(JSON.toJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }

    }

}
