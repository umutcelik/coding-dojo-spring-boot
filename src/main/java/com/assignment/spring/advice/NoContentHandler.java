package com.assignment.spring.advice;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import com.assignment.spring.dto.WeatherDto;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Controller advice to return {@link HttpStatus#NO_CONTENT} for empty collection results.
 */
@Slf4j
@ControllerAdvice
public class NoContentHandler implements ResponseBodyAdvice<Collection> {

    public static final Type LIST_TYPE = new ParameterizedTypeReference<List<WeatherDto>>() {
    }.getType();

    /**
     * Checks if advice supports the response. Applies the advice if response type is <code>List</code>
     *
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(final MethodParameter returnType, final Class<? extends HttpMessageConverter<?>> converterType) {
        //this is going to support just Collection<WeatherDto>
        //this filter should have better implementation with new endpoints.
        if (returnType.getGenericParameterType().equals(LIST_TYPE)) {
            return true;
        }
        return false;
    }

    /**
     * Checks the response, if body is empty sets the response code as NO_CONTENT.
     *
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Override
    public Collection beforeBodyWrite(final Collection body, final MethodParameter returnType, final MediaType selectedContentType,
                                      final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                      final ServerHttpRequest request, final ServerHttpResponse response) {
        if (body.isEmpty()) {
            log.debug("No response, setting http response code as no content");
            response.setStatusCode(NO_CONTENT);
        }
        return body;
    }
}
