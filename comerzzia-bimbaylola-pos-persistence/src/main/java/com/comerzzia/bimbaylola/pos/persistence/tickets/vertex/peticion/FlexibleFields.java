
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "flexibleCodeFields",
    "flexibleDateFields",
    "flexibleNumericFields"
})
@Generated("jsonschema2pojo")
public class FlexibleFields {

    @JsonProperty("flexibleCodeFields")
    private List<FlexibleCodeField> flexibleCodeFields = null;
    @JsonProperty("flexibleDateFields")
    private List<FlexibleDateField> flexibleDateFields = null;
    @JsonProperty("flexibleNumericFields")
    private List<FlexibleNumericField> flexibleNumericFields = null;

    @JsonProperty("flexibleCodeFields")
    public List<FlexibleCodeField> getFlexibleCodeFields() {
        return flexibleCodeFields;
    }

    @JsonProperty("flexibleCodeFields")
    public void setFlexibleCodeFields(List<FlexibleCodeField> flexibleCodeFields) {
        this.flexibleCodeFields = flexibleCodeFields;
    }

    @JsonProperty("flexibleDateFields")
    public List<FlexibleDateField> getFlexibleDateFields() {
        return flexibleDateFields;
    }

    @JsonProperty("flexibleDateFields")
    public void setFlexibleDateFields(List<FlexibleDateField> flexibleDateFields) {
        this.flexibleDateFields = flexibleDateFields;
    }

    @JsonProperty("flexibleNumericFields")
    public List<FlexibleNumericField> getFlexibleNumericFields() {
        return flexibleNumericFields;
    }

    @JsonProperty("flexibleNumericFields")
    public void setFlexibleNumericFields(List<FlexibleNumericField> flexibleNumericFields) {
        this.flexibleNumericFields = flexibleNumericFields;
    }

}
