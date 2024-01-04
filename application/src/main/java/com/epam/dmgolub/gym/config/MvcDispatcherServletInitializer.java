package com.epam.dmgolub.gym.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class MvcDispatcherServletInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(final ServletContext servletContext) {

        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);

        // Manage the lifecycle of the root application context
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        final AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(AppConfig.class);

        // Register and map the dispatcher servlet
       final ServletRegistration.Dynamic dispatcher =
           servletContext.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        registerHiddenFieldFilter(servletContext);
    }

    private void registerHiddenFieldFilter(final ServletContext servletContext) {
        servletContext.addFilter("hiddenHttpMethodFilter",
            new HiddenHttpMethodFilter()).addMappingForUrlPatterns(null, true, "/*");
    }
}
