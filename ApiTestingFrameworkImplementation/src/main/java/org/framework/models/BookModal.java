package org.framework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookModal {
            private String isbn;
            private String title;
            private String subTitle;
            private String author;
            private String publish_date;
            private String publisher;
            private int pages;
            private String description;
            private String website;
}
