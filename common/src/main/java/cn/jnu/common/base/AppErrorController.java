package cn.jnu.common.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
public class AppErrorController extends AbstractErrorController {

    private static final String ERROR_PATH = "/error";

    public AppErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @ApiIgnore
    @RequestMapping(value = ERROR_PATH)
    public Result<String> errorApiHandler(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> attr = this.getErrorAttributes(request, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.EXCEPTION,
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.STACK_TRACE,
                ErrorAttributeOptions.Include.BINDING_ERRORS));
        String uri = request.getRequestURI();
        Integer code = response.getStatus();
        String message = attr.get("message").toString();
        log.error("uri: {}, code: {}, message: {}", uri, code, message);
        return Result.failure(code, message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
