package edu.wgu.d.emsbackend.scenario.service;

import edu.wgu.d.emsbackend.scenario.Scenario;
import edu.wgu.d.emsbackend.scenario.ScenarioRepository;
import edu.wgu.d.emsbackend.scenario.dto.CreateScenarioRequest;
import edu.wgu.d.emsbackend.scenario.dto.UpdateScenarioRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ScenarioService {

    private final ScenarioRepository scenarioRepository;

    public ScenarioService(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    public Scenario create(CreateScenarioRequest req) {
        Scenario s = new Scenario();
        s.setTitle(req.getTitle().trim());
        s.setPrompt(req.getPrompt().trim());
        if (req.getActive() != null) s.setActive(req.getActive());
        return scenarioRepository.save(s);
    }

    public List<Scenario> listAll() {
        return scenarioRepository.findAll();
    }

    public List<Scenario> listActive() {
        return scenarioRepository.findByActiveTrueOrderByCreatedDesc();

    }

    public Scenario get(UUID id) {
        return scenarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Scenario not found: " + id));
    }

    public Scenario update(UUID id, UpdateScenarioRequest req) {
        Scenario existing = get(id);
        existing.setTitle(req.getTitle().trim());
        existing.setPrompt(req.getPrompt().trim());
        existing.setActive(req.isActive());
        return scenarioRepository.save(existing);
    }

    public void delete(UUID id) {
        scenarioRepository.deleteById(id);
    }
}
