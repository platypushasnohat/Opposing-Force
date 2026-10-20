package com.barl_inc.opposing_force.item.curios;

import com.platypushasnohat.sinew.item.AttributeEntry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.ArrayList;
import java.util.List;

public record CurioDefinition(List<AttributeEntry> attributes, List<String> descriptions) {

    public static class Builder {

        private final List<AttributeEntry> attributes = new ArrayList<>();
        private final List<String> descriptions = new ArrayList<>();

        public Builder attribute(Holder<Attribute> attribute, double value, AttributeModifier.Operation operation) {
            this.attributes.add(new AttributeEntry(attribute, value, operation));
            return this;
        }

        public Builder description(String key) {
            this.descriptions.add(key);
            return this;
        }

        public CurioDefinition build() {
            return new CurioDefinition(List.copyOf(this.attributes), List.copyOf(this.descriptions));
        }
    }
}
