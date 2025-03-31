package org.framework.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddListOfBooks {
    private String userId;
    private List<CollectionOfIsbn> collectionOfIsbn;
}
