package cn.mdni.business.constants;

import cn.mdni.core.dto.StatusDto;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * <dl>
 * <dd>Description: 美得你sm  签名比对拦截器  </dd>
 * <dd>Company: 美得你智装科技有限公司</dd>
 * <dd>@date：2017/12/19</dd>
 * <dd>@author：Allen</dd>
 * </dl>
 */
public class SignatureComparisonInterceptor implements HandlerInterceptor {
    /**
     *  对请求进行 拦截过滤
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            String reqJson = request.getParameter("requestKey");
            boolean signPass = SignatureUtils.signAuth(reqJson);
            if (!signPass) {
                //向浏览器发送一个响应头，设置浏览器的解码方式为UTF-8,其实设置了本句，也默认设置了Response的编码方式为UTF-8，但是开发中最好两句结合起来使用
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(StatusDto.buildStatusDtoWithCode(ResponseEnum.ERROR.getCode(), ResponseEnum.ERROR.getMessage()).toString());
            }
            return signPass;
        }catch (Exception exp){
            exp.printStackTrace();
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
