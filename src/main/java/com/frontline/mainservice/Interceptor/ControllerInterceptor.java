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
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class ControllerInterceptor implements HandlerInterceptor {
    private X509Certificate serverCertificate = CertX509Handler.loadCertFromKeyStore();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ControllerInterceptor() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        //for demonstration purpose - disable the browser certificate check
//        boolean certVerified = false;
//        HandlerMethod method = (HandlerMethod) handler;
//        MethodParameter[] parms = method.getMethodParameters();
//        for (MethodParameter parm : parms) {
//            logger.info(parm.getNestedGenericParameterType().getTypeName());
//        }
//
//        X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
//
//        try {
//            certVerified = CertX509Handler.validateCertificate(certs);
//        } catch (Exception e) {
//            response.setStatus(400);    //chrome displays its generic page for 400
//            response.setHeader("Error", e.getMessage());
//
//        }

        return true; //for demo otherwise return certVerified
    }

    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
//        logger.info("this is interceptor, postHandle method");
//        response.addHeader("Cert",CertX509Handler.stringFromCert(serverCertificate));

    }
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
//        logger.info("Header :"+ response.getHeader("Cert"));
    }
}
