package me.fabric.eyephonemod.gui.client.util;

import net.minecraft.data.client.model.BlockStateVariantMap.TriFunction;

public class AnimationKeyframePlayer<T> {

    public final TriFunction<Double, T, T, T> interpolator;
    private final T from;
    private final T to;
    public final int numberOfFrames;
    public final Ease easeType;
    public final Type animationType;
    private final double[] frames;

    private boolean forward = true;
    private int currentFrame = 0;

    public AnimationKeyframePlayer(T from, T to, TriFunction<Double, T, T, T> interpolator, int numberOfFrames, Ease easeType, Type animationType) {
        this.interpolator = interpolator;
        this.from = from;
        this.to = to;
        this.numberOfFrames = numberOfFrames;
        this.easeType = easeType;
        this.animationType = animationType;
        if (numberOfFrames <= 1) this.frames = new double[]{1.0};
        else {
            this.frames = new double[numberOfFrames];
            for (int i = 0; i < numberOfFrames; i++) {
                this.frames[i] = i / (numberOfFrames - 1.0);
            }
        }
    }

    public T next() {
        animationType.prepare(this);
        final double instance = easeType.func(frames[currentFrame]);
        return interpolator.apply(instance, from, to);
    }

    public enum Ease {
        QUAD_IN_OUT {
            @Override
            public double func(double x) {
                return x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2;
            }
        },
        QUAD_OUT {
            @Override
            public double func(double x) {
                return 1 - (1 - x) * (1 - x);
            }
        };

        public abstract double func(double x);
    }

    public enum Type {
        PERSISTENT {
            public void prepare(AnimationKeyframePlayer<?> player) {
                player.currentFrame++;
                player.currentFrame = Math.min(player.currentFrame, player.numberOfFrames - 1);
            }
        },
        REPEATING {
            public void prepare(AnimationKeyframePlayer<?> player) {
                player.currentFrame++;
                if (player.currentFrame == player.numberOfFrames) {
                    player.currentFrame = 0;
                }
            }
        },
        REVERSING {
            public void prepare(AnimationKeyframePlayer<?> player) {
                if (player.forward) player.currentFrame++;
                else player.currentFrame--;

                if (player.currentFrame == 0 || player.currentFrame == player.numberOfFrames - 1)
                    player.forward = !player.forward;
            }
        };

        public abstract void prepare(AnimationKeyframePlayer<?> player);
    }
}
