package com.volmit.react.content.sampler;

import com.volmit.react.React;
import com.volmit.react.api.sampler.ReactCachedSampler;
import com.volmit.react.util.format.Form;
import com.volmit.react.util.math.M;
import com.volmit.react.util.math.RollingSequence;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class SamplerChunksGenerated extends ReactCachedSampler implements Listener {
    public static final String ID = "chunks-generated";
    private static final double D1_OVER_SECONDS = 1.0 / 1000D;
    private transient final AtomicInteger generated;
    private transient RollingSequence avg;
    private transient long lastSample = 0L;
    private int sequenceAverageLength = 5;

    public SamplerChunksGenerated() {
        super(ID, 1000); // 1 tick interval for higher accuracy
        generated = new AtomicInteger(0);
    }

    @Override
    public Material getIcon() {
        return Material.FURNACE_MINECART;
    }

    @Override
    public void start() {
        React.instance.registerListener(this);
        avg = new RollingSequence(sequenceAverageLength);
    }

    @Override
    public void stop() {
        React.instance.unregisterListener(this);
    }

    @EventHandler
    public void on(ChunkLoadEvent event) {
        if (event.isNewChunk()) {
            generated.incrementAndGet();
        }
    }

    @Override
    public double onSample() {
        if (lastSample == 0) {
            lastSample = M.ms();
        }

        int r = generated.getAndSet(0);
        long dur = Math.max(M.ms() - lastSample, 1000);
        lastSample = M.ms();
        avg.put(r / (dur * D1_OVER_SECONDS));

        return avg.getAverage();
    }

    @Override
    public String formattedValue(double t) {
        return Form.f(Math.round(t));
    }

    @Override
    public String formattedSuffix(double t) {
        return "GEN/s";
    }
}