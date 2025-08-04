package com.railse.hiring.workforcemgmt.repository;

import com.railse.hiring.workforcemgmt.model.Activity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryActivityRepository implements ActivityRepository {
    private final Map<Long, Activity> activityStore = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    @Override
    public Activity save(Activity activity) {
        if (activity.getId() == null) {
            activity.setId(idCounter.incrementAndGet());
        }
        activityStore.put(activity.getId(), activity);
        return activity;
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return Optional.ofNullable(activityStore.get(id));
    }

    @Override
    public List<Activity> findByTaskIdOrderByTimestamp(Long taskId) {
        return activityStore.values().stream()
                .filter(activity -> activity.getTaskId().equals(taskId))
                .sorted((a1, a2) -> Long.compare(a1.getTimestamp(), a2.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Activity> findAll() {
        return List.copyOf(activityStore.values());
    }
}
