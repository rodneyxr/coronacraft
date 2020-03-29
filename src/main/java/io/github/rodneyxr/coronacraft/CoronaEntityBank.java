package io.github.rodneyxr.coronacraft;

import org.spongepowered.api.entity.Entity;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoronaEntityBank {
    ConcurrentHashMap<UUID, CoronaEntity> entityMap;

    public CoronaEntityBank() {
        entityMap = new ConcurrentHashMap<>();
    }

    public CoronaEntity getOrCreate(Entity entity) {
        CoronaEntity coronaEntity = entityMap.getOrDefault(entity.getUniqueId(), null);
        if (coronaEntity == null) {
            coronaEntity = new CoronaEntity(entity);
            entityMap.put(entity.getUniqueId(), coronaEntity);
        }
        return coronaEntity;
    }

    public void put(CoronaEntity entity) {
        entityMap.put(entity.getUniqueId(), entity);
    }

    public Collection<CoronaEntity> getAll() {
        return entityMap.values();
    }

}
