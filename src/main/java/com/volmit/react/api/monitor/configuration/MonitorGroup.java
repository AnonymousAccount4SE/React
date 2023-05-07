package com.volmit.react.api.monitor.configuration;

import com.volmit.react.React;
import com.volmit.react.api.sampler.Sampler;
import com.volmit.react.util.C;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.awt.Color;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
public class MonitorGroup {
    private String color;
    private String name;

    @Singular
    private List<String> samplers;
    private String head;

    public Sampler getHeadSampler() {
        if(head == null) {
         head = samplers.get(0);
        }

        return React.instance.getSampleController().getSampler(head);
    }

    public void setHeadSampler(String s) {
       head = s;
    }

    public List<Sampler> getSubSamplers() {
        return samplers.stream().skip(1).map(i -> React.instance.getSampleController().getSampler(i)).collect(Collectors.toList());
    }

    public int getColorValue()
    {
        return Color.decode(color).getRGB();
    }
}