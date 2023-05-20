package hu.psprog.leaflet.tlp.api.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * Log retrieval request object.
 *
 * @author Peter Smith
 */
@Data
public class LogRequest {

    private boolean queried;
    private String source;
    private String level;
    private String content;
    private int page;
    private int limit;
    private OrderBy orderBy;
    private OrderDirection orderDirection;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date to;

    public int getPage() {

        if (page == 0) {
            page = 1;
        }

        return page;
    }

    public int getLimit() {

        if (limit == 0) {
            limit = 50;
        }

        return limit;
    }

    public OrderBy getOrderBy() {

        if (Objects.isNull(orderBy)) {
            orderBy = OrderBy.TIMESTAMP;
        }

        return orderBy;
    }

    public OrderDirection getOrderDirection() {

        if (Objects.isNull(orderDirection)) {
            orderDirection = OrderDirection.DESC;
        }

        return orderDirection;
    }
}
