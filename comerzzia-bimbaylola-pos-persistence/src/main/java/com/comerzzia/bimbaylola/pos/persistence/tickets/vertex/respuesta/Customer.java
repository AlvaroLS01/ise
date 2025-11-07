
package com.comerzzia.bimbaylola.pos.persistence.tickets.vertex.respuesta;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "destination"
})
@Generated("jsonschema2pojo")
public class Customer {
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
