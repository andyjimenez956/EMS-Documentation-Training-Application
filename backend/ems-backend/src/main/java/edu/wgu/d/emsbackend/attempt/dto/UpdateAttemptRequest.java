package edu.wgu.d.emsbackend.attempt.dto;

public class UpdateAttemptRequest {

    private String patientFirstName;
    private String patientLastName;
    private Integer patientAge;
    private String patientSex;
    private String chiefComplaint;

    private Integer systolicBp;
    private Integer diastolicBp;
    private Integer heartRate;
    private Integer respiratoryRate;
    private Integer spo2;

    private String assessmentNotes;
    private String interventions;

    private String disposition;

    private String narrativeText;

    public String getPatientFirstName() { return patientFirstName; }
    public String getPatientLastName() { return patientLastName; }
    public Integer getPatientAge() { return patientAge; }
    public String getPatientSex() { return patientSex; }
    public String getChiefComplaint() { return chiefComplaint; }

    public Integer getSystolicBp() { return systolicBp; }
    public Integer getDiastolicBp() { return diastolicBp; }
    public Integer getHeartRate() { return heartRate; }
    public Integer getRespiratoryRate() { return respiratoryRate; }
    public Integer getSpo2() { return spo2; }

    public String getAssessmentNotes() { return assessmentNotes; }
    public String getInterventions() { return interventions; }

    public String getDisposition() { return disposition; }

    public String getNarrativeText() { return narrativeText; }
}
