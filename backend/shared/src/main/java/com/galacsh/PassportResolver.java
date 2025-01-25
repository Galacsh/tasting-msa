package com.galacsh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@ConditionalOnProperty(value = "passport.enabled", havingValue = "true")
public class PassportResolver implements WebMvcConfigurer {

    private final ObjectMapper mapper;

    public PassportResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(@NonNull MethodParameter parameter) {
                return parameter.getParameterType().equals(Passport.class);
            }

            @Override
            public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer, @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
                String serializedPassport = webRequest.getHeader("X-Passport");
                boolean foundPassport = StringUtils.hasText(serializedPassport);

                if (foundPassport) {
                    return mapper.readValue(serializedPassport, Passport.class);
                }

                boolean required = parameter.hasParameterAnnotation(NonNull.class);
                if (required) {
                    throw new PassportMissingException();
                } else {
                    return null;
                }
            }
        });
    }
}
