package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.generator.dummy.DummyObjectProvider;

public final class CoronaKeys {
    public static final Key<Value<Boolean>> INFECTED = DummyObjectProvider.createExtendedFor(Key.class,"INFECTED");
}
