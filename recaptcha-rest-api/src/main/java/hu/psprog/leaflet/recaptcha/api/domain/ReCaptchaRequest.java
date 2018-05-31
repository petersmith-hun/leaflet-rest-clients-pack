package hu.psprog.leaflet.recaptcha.api.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * ReCaptcha service validation request model.
 *
 * @author Peter Smith
 */
public class ReCaptchaRequest implements Serializable {

    private String secret;
    private String response;
    private String remoteIp;

    public String getSecret() {
        return secret;
    }

    public String getResponse() {
        return response;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ReCaptchaRequest that = (ReCaptchaRequest) o;

        return new EqualsBuilder()
                .append(secret, that.secret)
                .append(response, that.response)
                .append(remoteIp, that.remoteIp)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(secret)
                .append(response)
                .append(remoteIp)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("secret", secret)
                .append("response", response)
                .append("remoteIp", remoteIp)
                .toString();
    }

    public static ReCaptchaRequestBuilder getBuilder() {
        return new ReCaptchaRequestBuilder();
    }

    /**
     * Builder for {@link ReCaptchaRequest}.
     */
    public static final class ReCaptchaRequestBuilder {
        private String secret;
        private String response;
        private String remoteIp;

        private ReCaptchaRequestBuilder() {
        }

        public ReCaptchaRequestBuilder withSecret(String secret) {
            this.secret = secret;
            return this;
        }

        public ReCaptchaRequestBuilder withResponse(String response) {
            this.response = response;
            return this;
        }

        public ReCaptchaRequestBuilder withRemoteIp(String remoteIp) {
            this.remoteIp = remoteIp;
            return this;
        }

        public ReCaptchaRequest build() {
            ReCaptchaRequest reCaptchaRequest = new ReCaptchaRequest();
            reCaptchaRequest.secret = this.secret;
            reCaptchaRequest.response = this.response;
            reCaptchaRequest.remoteIp = this.remoteIp;
            return reCaptchaRequest;
        }
    }
}
