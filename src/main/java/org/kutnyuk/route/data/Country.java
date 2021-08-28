package org.kutnyuk.route.data;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country implements Serializable {

    private static final long serialVersionUID = 9543264564353L;

    @JsonProperty("cca3")
    private String name;

    @JsonProperty
    private List<String> borders;

    @JsonIgnore
    private boolean processed;

    @JsonIgnore
    private Country previous;
}
