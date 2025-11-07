
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "returnsCodeFields",
    "returnsDateFields",
    "returnsIndicatorFields",
    "returnsNumericFields"
})
@Generated("jsonschema2pojo")
public class ReturnsFields {

    @JsonProperty("returnsCodeFields")
    private List<ReturnsCodeField> returnsCodeFields = null;
    @JsonProperty("returnsDateFields")
    private List<ReturnsDateField> returnsDateFields = null;
    @JsonProperty("returnsIndicatorFields")
    private List<ReturnsIndicatorField> returnsIndicatorFields = null;
    @JsonProperty("returnsNumericFields")
    private List<ReturnsNumericField> returnsNumericFields = null;

    @JsonProperty("returnsCodeFields")
    public List<ReturnsCodeField> getReturnsCodeFields() {
        return returnsCodeFields;
    }

    @JsonProperty("returnsCodeFields")
    public void setReturnsCodeFields(List<ReturnsCodeField> returnsCodeFields) {
        this.returnsCodeFields = returnsCodeFields;
    }

    @JsonProperty("returnsDateFields")
    public List<ReturnsDateField> getReturnsDateFields() {
        return returnsDateFields;
    }

    @JsonProperty("returnsDateFields")
    public void setReturnsDateFields(List<ReturnsDateField> returnsDateFields) {
        this.returnsDateFields = returnsDateFields;
    }

    @JsonProperty("returnsIndicatorFields")
    public List<ReturnsIndicatorField> getReturnsIndicatorFields() {
        return returnsIndicatorFields;
    }

    @JsonProperty("returnsIndicatorFields")
    public void setReturnsIndicatorFields(List<ReturnsIndicatorField> returnsIndicatorFields) {
        this.returnsIndicatorFields = returnsIndicatorFields;
    }

    @JsonProperty("returnsNumericFields")
    public List<ReturnsNumericField> getReturnsNumericFields() {
        return returnsNumericFields;
    }

    @JsonProperty("returnsNumericFields")
    public void setReturnsNumericFields(List<ReturnsNumericField> returnsNumericFields) {
        this.returnsNumericFields = returnsNumericFields;
    }

}
