package com.sensorreader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class Reader implements Runnable{
    private final Producer producer;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public void run() {
        String csvFile = "B:\\Facultate\\An IV - 1\\SD\\sensor-reader\\sensor.csv";
        String deviceFile = "B:\\Facultate\\An IV - 1\\SD\\sensor-reader\\deviceConfig.txt";

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile))) {
            String lineSensor;
            String lineDevice;
            while ((lineSensor = bufferedReader.readLine()) != null) {
                Float value = Float.parseFloat(lineSensor.split("\n")[0]);
                ArrayList<String> deviceIds = new ArrayList<>();
                try (BufferedReader br = new BufferedReader(new FileReader(deviceFile))) {
                    while ((lineDevice = br.readLine()) != null) {
                        deviceIds.add(lineDevice);
                    }
                } catch (Exception e) { System.out.println(e.getMessage()); }
                for (String deviceId: deviceIds
                     ) {
                    System.out.println("\"" + deviceId + "\": " + value);
                    producer.sendJsonMessage(SensorDTO.builder()
                            .timestamp(calendar.getTime())
                            .sensorValue(value)
                            .sensorId(deviceId)
                            .build());
                }
                this.calendar.add(Calendar.HOUR, 1);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
