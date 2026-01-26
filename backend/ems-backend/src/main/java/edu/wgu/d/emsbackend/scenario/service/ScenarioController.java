package edu.wgu.d.emsbackend.scenario;

import edu.wgu.d.emsbackend.scenario.dto.CreateScenarioRequest;
import edu.wgu.d.emsbackend.scenario.dto.ScenarioResponse;
import edu.wgu.d.emsbackend.scenario.dto.UpdateScenarioRequest;
import edu.wgu.d.emsbackend.scenario.service.ScenarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    private final ScenarioService scenarioService;

    public ScenarioController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @PostMapping
    public ScenarioResponse create(@Valid @RequestBody CreateScenarioRequest req) {
        return toResponse(scenarioService.create(req));
    }

    @GetMapping
    public List<ScenarioResponse> listAll() {
        return scenarioService.listAll().stream().map(this::toResponse).toList();
    }

    @GetMapping("/active")
    public List<ScenarioResponse> listActive() {
        return scenarioService.listActive().stream().map(this::toResponse).toList();
    }

    @GetMapping("/{id}")
    public ScenarioResponse getOne(@PathVariable UUID id) {
        return toResponse(scenarioService.get(id));
    }

    @PutMapping("/{id}")
    public ScenarioResponse update(@PathVariable UUID id, @Valid @RequestBody UpdateScenarioRequest req) {
        return toResponse(scenarioService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        scenarioService.delete(id);
    }

    private ScenarioResponse toResponse(Scenario s) {
        return new ScenarioResponse(
                s.getId(),
                s.getTitle(),
                s.getPrompt(),
                s.isActive(),
                s.getCreated()
        );
    }
}
