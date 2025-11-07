
package com.comerzzia.bimbaylola.pos.gui.ventas.tickets.articulos.vertex.recibir;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "app",
    "timeElapsed(ms)",
    "timeReceived"
})
@Generated("jsonschema2pojo")
public class Meta {

    @JsonProperty("app")
    private String app;
    @JsonProperty("timeElapsed(ms)")
    private Integer timeElapsedMs;
    @JsonProperty("timeReceived")
    private String timeReceived;

    @JsonProperty("app")
    public String getApp() {
        return app;
    }

    @JsonProperty("app")
    public void setApp(String app) {
        this.app = app;
    }

    @JsonProperty("timeElapsed(ms)")
    public Integer getTimeElapsedMs() {
        return timeElapsedMs;
    }

    @JsonProperty("timeElapsed(ms)")
    public void setTimeElapsedMs(Integer timeElapsedMs) {
        this.timeElapsedMs = timeElapsedMs;
    }

    @JsonProperty("timeReceived")
    public String getTimeReceived() {
        return timeReceived;
    }

    @JsonProperty("timeReceived")
    public void setTimeReceived(String timeReceived) {
        this.timeReceived = timeReceived;
    }

}
