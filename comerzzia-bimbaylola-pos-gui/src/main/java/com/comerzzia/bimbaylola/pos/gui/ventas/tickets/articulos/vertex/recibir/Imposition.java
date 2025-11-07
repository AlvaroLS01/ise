
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "impositionId",
    "userDefined",
    "value"
})
@Generated("jsonschema2pojo")
public class Imposition {

    @JsonProperty("impositionId")
    private String impositionId;
    @JsonProperty("userDefined")
    private Boolean userDefined;
    @JsonProperty("value")
    private String value;

    @JsonProperty("impositionId")
    public String getImpositionId() {
        return impositionId;
    }

    @JsonProperty("impositionId")
    public void setImpositionId(String impositionId) {
        this.impositionId = impositionId;
    }

    @JsonProperty("userDefined")
    public Boolean getUserDefined() {
        return userDefined;
    }

    @JsonProperty("userDefined")
    public void setUserDefined(Boolean userDefined) {
        this.userDefined = userDefined;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    @JsonProperty("value")
    public void setValue(String value) {
        this.value = value;
    }

}
