
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "destination"
})
@Generated("jsonschema2pojo")
public class Customer__1 {

    @JsonProperty("destination")
    private Destination__1 destination;

    @JsonProperty("destination")
    public Destination__1 getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Destination__1 destination) {
        this.destination = destination;
    }

}
