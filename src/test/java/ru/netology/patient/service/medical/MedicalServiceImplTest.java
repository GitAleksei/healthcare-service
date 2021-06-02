package ru.netology.patient.service.medical;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MedicalServiceImplTest {

    @Test
    void checkBloodPressure_alert_test() {
        String id = "2241";
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(new PatientInfo(id, "Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                                new BloodPressure(120, 80))));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertServiceImpl sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository,
                sendAlertService);
        BloodPressure currentPressure = new BloodPressure(80, 120);
        medicalService.checkBloodPressure(id, currentPressure);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        String expected = String.format("Warning, patient with id: %s, need help", id);
        String actual = argumentCaptor.getValue();

        assertEquals(expected, actual);
    }

    @Test
    void checkTemperature_alert_test() {
        String id = "2242";
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(new PatientInfo(id, "Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                                new BloodPressure(120, 80))));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        SendAlertServiceImpl sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository,
                sendAlertService);
        BigDecimal currentTemperature = new BigDecimal("31.9");
        medicalService.checkTemperature(id, currentTemperature);

        Mockito.verify(sendAlertService).send(argumentCaptor.capture());

        String expected = String.format("Warning, patient with id: %s, need help", id);
        String actual = argumentCaptor.getValue();

        assertEquals(expected, actual);
    }

    @Test
    void checkTemperaturePressure_not_alert_test() {
        String id = "2243";
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(id))
                .thenReturn(new PatientInfo(id, "Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"),
                                new BloodPressure(120, 80))));

        SendAlertServiceImpl sendAlertService = Mockito.spy(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepository,
                sendAlertService);

        BloodPressure currentPressure = new BloodPressure(120, 80);
        medicalService.checkBloodPressure(id, currentPressure);
        BigDecimal currentTemperature = new BigDecimal("37.9");
        medicalService.checkTemperature(id, currentTemperature);

        String expected = String.format("Warning, patient with id: %s, need help", id);

        Mockito.verify(sendAlertService, Mockito.never()).send(expected);
    }
}