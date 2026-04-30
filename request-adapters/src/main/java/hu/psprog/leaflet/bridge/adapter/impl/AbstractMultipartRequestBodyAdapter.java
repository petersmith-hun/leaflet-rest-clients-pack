package hu.psprog.leaflet.bridge.adapter.impl;

import hu.psprog.leaflet.bridge.adapter.RequestBodyAdapter;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;

import java.io.Serializable;
import java.util.function.Function;

/**
 * {@link RequestBodyAdapter} implementation for sending multipart/form-data requests.
 *
 * @author Peter Smith
 */
abstract class AbstractMultipartRequestBodyAdapter<S extends Serializable> implements RequestBodyAdapter<S> {

    /**
     * Returns a {@link FormBuilder} instance, that can be used to build the multipart form.
     *
     * @param source instance stores the source object and executes operations on stored instance
     * @return FormBuilder instance
     */
    FormBuilder getMultipartBuilderFor(S source) {
        return new FormBuilder(source);
    }

    /**
     * Flow-like multipart form builder tool.
     */
    class FormBuilder {

        private final MultipartEntityBuilder formData;
        private final S source;

        private FormBuilder(S source) {
            this.formData = MultipartEntityBuilder.create()
                    .setContentType(ContentType.MULTIPART_FORM_DATA);
            this.source = source;
        }

        /**
         * Adds given field value of type String to the form.
         *
         * @param name field name
         * @param reference String value extractor {@link Function}
         * @return FormBuilder instance
         */
        FormBuilder withStringField(String name, Function<S, String> reference) {
            formData.addTextBody(name, reference.apply(source));
            return this;
        }

        /**
         * Retrieves built multipart form instance.
         *
         * @return multipart form as {@link UrlEncodedFormEntity}
         */
        HttpEntity build() {
            return formData.build();
        }
    }
}
