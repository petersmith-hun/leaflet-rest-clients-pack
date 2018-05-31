package hu.psprog.leaflet.recaptcha.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

/**
 * ReCaptcha service validation response model.
 *
 * @author Peter Smith
 */
@JsonDeserialize(builder = ReCaptchaResponse.ReCaptchaResponseBuilder.class)
public class ReCaptchaResponse {

    private boolean successful;
    private Date challengeTimeStamp;
    private String hostname;
    private List<ReCaptchaErrorCode> errorCodes;

    public boolean isSuccessful() {
        return successful;
    }

    public Date getChallengeTimeStamp() {
        return challengeTimeStamp;
    }

    public String getHostname() {
        return hostname;
    }

    public List<ReCaptchaErrorCode> getErrorCodes() {
        return errorCodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ReCaptchaResponse that = (ReCaptchaResponse) o;

        return new EqualsBuilder()
                .append(successful, that.successful)
                .append(challengeTimeStamp, that.challengeTimeStamp)
                .append(hostname, that.hostname)
                .append(errorCodes, that.errorCodes)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(successful)
                .append(challengeTimeStamp)
                .append(hostname)
                .append(errorCodes)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("successful", successful)
                .append("challengeTimeStamp", challengeTimeStamp)
                .append("hostname", hostname)
                .append("errorCodes", errorCodes)
                .toString();
    }

    public static ReCaptchaResponseBuilder getBuilder() {
        return new ReCaptchaResponseBuilder();
    }

    /**
     * Builder for {@link ReCaptchaResponse}.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class ReCaptchaResponseBuilder {
        private boolean success;
        private Date challengeTimeStamp;
        private String hostname;
        private List<ReCaptchaErrorCode> errorCodes;

        private ReCaptchaResponseBuilder() {
        }

        public ReCaptchaResponseBuilder withSuccess(boolean success) {
            this.success = success;
            return this;
        }

        @JsonProperty("challenge_ts")
        public ReCaptchaResponseBuilder withChallengeTimeStamp(Date challengeTimeStamp) {
            this.challengeTimeStamp = challengeTimeStamp;
            return this;
        }

        public ReCaptchaResponseBuilder withHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        @JsonProperty("error-codes")
        public ReCaptchaResponseBuilder withErrorCodes(List<ReCaptchaErrorCode> errorCodes) {
            this.errorCodes = errorCodes;
            return this;
        }

        public ReCaptchaResponse build() {
            ReCaptchaResponse reCaptchaResponse = new ReCaptchaResponse();
            reCaptchaResponse.errorCodes = this.errorCodes;
            reCaptchaResponse.hostname = this.hostname;
            reCaptchaResponse.successful = this.success;
            reCaptchaResponse.challengeTimeStamp = this.challengeTimeStamp;
            return reCaptchaResponse;
        }
    }
}
