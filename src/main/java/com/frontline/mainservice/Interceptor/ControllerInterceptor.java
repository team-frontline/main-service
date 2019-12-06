package com.frontline.mainservice.Interceptor;

import com.frontline.mainservice.utils.CertX509Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.cert.X509Certificate;

@Component
public class ControllerInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        boolean certVerified = false;
        HandlerMethod method = (HandlerMethod) handler;
        MethodParameter[] parms = method.getMethodParameters();
        for (MethodParameter parm : parms) {
            logger.info(parm.getNestedGenericParameterType().getTypeName());
            System.out.println(parm.getNestedGenericParameterType().getTypeName());
        }

        X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

        try {
            certVerified = CertX509Handler.validateCertificate(certs);
        } catch (Exception e) {
            response.setStatus(4000);    //chrome displays its generic page for 400
            response.setHeader("Error", e.getMessage());

        }

        return certVerified;
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        logger.info("this is interceptor, postHandle method");
    }
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        logger.info("this is interceptor, afterCompletion method");
    }
}
