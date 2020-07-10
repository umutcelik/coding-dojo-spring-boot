package com.assignment.spring.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class WeatherDto {

    @NotBlank
    @ApiModelProperty(example = "Amsterdam")
    private String city;

    @NotBlank
    @ApiModelProperty(example = "NL")
    private String country;

    @NotBlank
    @ApiModelProperty(example = "METRIC")
    private String unit;

    @ApiModelProperty(example = "17.3")
    private Double temperature;
}
