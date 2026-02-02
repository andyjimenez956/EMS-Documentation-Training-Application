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

                scenario("Shortness of Breath"),
                scenario("Chest Pain – Suspected Myocardial Infarction"),
                scenario("Stroke – FAST Positive"),
                scenario("Altered Mental Status"),
                scenario("Diabetic Emergency – Hypoglycemia"),
                scenario("Diabetic Emergency – Hyperglycemia"),
                scenario("Seizure – Postictal Patient"),
                scenario("Syncope / Near Syncope"),
                scenario("Traumatic Fall – Elderly Patient"),
                scenario("Motor Vehicle Collision – Moderate Trauma"),
                scenario("Motor Vehicle Collision – Severe Trauma"),
                scenario("Pedestrian Struck"),
                scenario("Gunshot Wound"),
                scenario("Stab Wound"),
                scenario("Assault with Head Injury"),
                scenario("Overdose – Opioid"),
                scenario("Overdose – Unknown Substance"),
                scenario("Alcohol Intoxication"),
                scenario("Respiratory Distress – Asthma Exacerbation"),
                scenario("Respiratory Distress – COPD Exacerbation"),
                scenario("Allergic Reaction – Anaphylaxis"),
                scenario("Cardiac Arrest – Asystole"),
                scenario("Cardiac Arrest – Ventricular Fibrillation"),
                scenario("Cardiac Arrest – Pulseless Electrical Activity"),
                scenario("Heat Exhaustion"),
                scenario("Heat Stroke"),
                scenario("Hypothermia"),
                scenario("Drowning / Near Drowning"),
                scenario("Burn Injury – Thermal"),
                scenario("Burn Injury – Chemical"),
                scenario("Carbon Monoxide Exposure"),
                scenario("Childbirth – Normal Delivery"),
                scenario("Childbirth – Complicated Delivery"),
                scenario("Pediatric Fever"),
                scenario("Pediatric Respiratory Distress"),
                scenario("Behavioral Emergency – Suicidal Ideation"),
                scenario("Behavioral Emergency – Agitation"),
                scenario("Abdominal Pain"),
                scenario("GI Bleed"),
                scenario("Sepsis – Suspected Infection"),
                scenario("Unconscious Unknown"),
                scenario("Failure to Thrive – Elderly"),
                scenario("Traumatic Amputation"),
                scenario("Workplace Injury"),
                scenario("Electrocution"),
                scenario("Snake Bite"),
                scenario("Animal Bite"),
                scenario("Eye Injury"),
                scenario("Epistaxis (Nosebleed)")
        );

        scenarioRepository.saveAll(scenarios);

        System.out.println("✅ Seeded " + scenarios.size() + " EMS scenarios");
    }

    private Scenario scenario(String title) {
        Scenario s = new Scenario();
        s.setTitle(title);
        s.setActive(true);
        return s;
    }
}
