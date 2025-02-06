package fun.smartflow.exception;

import cn.dev33.satoken.exception.NotLoginException;
import fun.smartflow.bean.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public Result handlerException(NotLoginException e) {
        return Result.error(401,e.getMessage());
    }
}
