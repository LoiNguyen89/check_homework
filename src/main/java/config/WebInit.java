package config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {


    private String TMP_URL = System.getProperty("java.io.tmpdir");
    private long MAX_FILE_SIZE = 50 * 1024 * 1024L;
    private long MAX_REQUEST_SIZE = 40 * MAX_FILE_SIZE;
    private Integer THRESHOLD_SIZE = 0;

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        super.customizeRegistration(registration);
        registration.setMultipartConfig(new MultipartConfigElement(TMP_URL, MAX_FILE_SIZE, MAX_REQUEST_SIZE, THRESHOLD_SIZE));
    }
}
