
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.peticion;

import java.math.BigDecimal;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fieldId",
    "value"
})
@Generated("jsonschema2pojo")
public class FlexibleNumericField {

    @JsonProperty("fieldId")
    private BigDecimal fieldId;
    @JsonProperty("value")
    private BigDecimal value;

    @JsonProperty("fieldId")
    public BigDecimal getFieldId() {
        return fieldId;
    }

    @JsonProperty("fieldId")
    public void setFieldId(BigDecimal fieldId) {
        this.fieldId = fieldId;
    }

    @JsonProperty("value")
    public BigDecimal getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
