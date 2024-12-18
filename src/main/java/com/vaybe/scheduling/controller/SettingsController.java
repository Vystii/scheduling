package com.vaybe.scheduling.controller;

import com.vaybe.scheduling.exception.ResourceNotFoundException;
import com.vaybe.scheduling.model.Settings;
import com.vaybe.scheduling.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {

    @Autowired
    private SettingsRepository settingsRepository;

    @PostMapping
    public Settings createOrUpdateSettings(@RequestBody Settings settings) {
        Settings existingSettings = new Settings();
        existingSettings.setGranularity(settings.getGranularity());
        existingSettings.setPauseDuration(settings.getPauseDuration());
        return settingsRepository.save(existingSettings);
    }

    @GetMapping("/{id}")
    public Settings getSettings(@PathVariable Long id) {
        return settingsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Settings not found with id " + id));
    }
}
