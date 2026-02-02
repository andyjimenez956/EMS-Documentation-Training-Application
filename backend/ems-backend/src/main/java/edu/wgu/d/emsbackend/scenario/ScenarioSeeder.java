package edu.wgu.d.emsbackend.scenario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScenarioSeeder implements CommandLineRunner {

    private final ScenarioRepository scenarioRepository;

    public ScenarioSeeder(ScenarioRepository scenarioRepository) {
        this.scenarioRepository = scenarioRepository;
    }

    @Override
    public void run(String... args) {

        // Prevent reseeding on every startup
        if (scenarioRepository.count() > 0) {
            return;
        }

        List<Scenario> scenarios = List.of(
                scenario(
                        "Shortness of Breath",
                        "You respond to a patient with shortness of breath. Document assessment findings, vitals, interventions, and disposition."
                ),
                scenario(
                        "Chest Pain – Suspected Myocardial Infarction",
                        "You respond to a patient with chest pain. Document OPQRST, risk factors, vitals, care provided, and transport decision."
                ),
                scenario(
                        "Stroke – FAST Positive",
                        "You respond to a possible stroke. Document onset time, neuro findings, vitals, and transport priority."
                ),
                scenario(
                        "Diabetic Emergency – Hypoglycemia",
                        "Patient appears altered with low blood sugar. Document assessment, vitals, treatment, and response to treatment."
                ),
                scenario(
                        "Seizure – Postictal Patient",
                        "Patient had a witnessed seizure and is postictal. Document airway status, vitals, history, and transport."
                ),
                scenario(
                        "Motor Vehicle Collision – Moderate Trauma",
                        "MVC with possible injuries. Document mechanism of injury, exam findings, vitals, and interventions."
                ),
                scenario(
                        "Overdose – Opioid",
                        "Patient suspected opioid overdose. Document respirations, mental status, interventions (including naloxone if used), and outcome."
                ),
                scenario(
                        "Allergic Reaction – Anaphylaxis",
                        "Patient has signs of anaphylaxis. Document assessment, vitals, treatment, and improvement/worsening."
                ),
                scenario(
                        "Cardiac Arrest – Ventricular Fibrillation",
                        "You arrive to a cardiac arrest in VF. Document CPR timeline, defibrillation, airway, meds (if applicable), and ROSC/no ROSC."
                ),
                scenario(
                        "Pediatric Fever",
                        "Pediatric patient with fever. Document assessment, vitals, caregiver report, and transport plan."
                ),
                scenario(
                        "Behavioral Emergency – Suicidal Ideation",
                        "Patient expresses suicidal thoughts. Document scene safety, mental status, risk indicators, and disposition."
                ),
                scenario(
                        "General Emergency – Sick Person",
                        "Patient feeling sick. Document scene safety, mental status, risk indicators, and disposition."
                ),
                scenario(
                        "Abdominal Pain",
                        "Patient reports abdominal pain. Document history, assessment, vitals, interventions, and transport."
                )
        );

        scenarioRepository.saveAll(scenarios);
        System.out.println("✅ Seeded " + scenarios.size() + " EMS scenarios");
    }

    private Scenario scenario(String title, String prompt) {
        Scenario s = new Scenario();
        s.setTitle(title);
        s.setPrompt(prompt);     // ✅ FIX: prompt is required (@NotBlank)
        s.setActive(true);
        return s;
    }
}
