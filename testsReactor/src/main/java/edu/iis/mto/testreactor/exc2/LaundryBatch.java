package edu.iis.mto.testreactor.exc2;

import java.util.Objects;

public class LaundryBatch {

    private final double weightKg;
    private final Material type;

    private LaundryBatch(Builder builder) {
        this.weightKg = Objects.requireNonNull(builder.weightKg, "weightKg == null");
        this.type = Objects.requireNonNull(builder.type, "type == null");
    }

    public double getWeightKg() {
        return weightKg;
    }

    public Material getType() {
        return type;
    }

    @Override
    public String toString() {
        return "LaundryBatch [weigth=" + weightKg + ", type=" + type + "]";
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private double weightKg;
        private Material type;

        private Builder() {}

        public Builder withWeightKg(double weightKg) {
                if (weightKg < 0) throw new IllegalArgumentException();
                this.weightKg = weightKg;
            return this;
        }

        public Builder withType(Material type) {
            this.type = type;
            return this;
        }

        public LaundryBatch build() {
            return new LaundryBatch(this);
        }
    }

}
