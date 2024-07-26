import './monitoring.css'
import dayjs, {Dayjs} from 'dayjs';
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useParams} from "react-router-dom";
import {jwtDecode} from "jwt-decode";
import {Sensor} from "./Sensor.ts";

import {Line} from 'react-chartjs-2';
import {
    Chart as ChartJS,
    LineElement,
    CategoryScale,
    LinearScale,
    PointElement,
    Title,
    Tooltip,
    Legend, ChartOptions
} from 'chart.js';

ChartJS.register(LineElement, CategoryScale, LinearScale, PointElement, Title, Tooltip, Legend);


const URL = "http://localhost:8082/spring-demo-monitoring";
const TODAY = dayjs(new Date());

const Monitoring = () => {
    const [sensorInstances, setSensorInstances] = useState<Sensor[]>([]);
    const navigate = useNavigate();
    const {deviceId} = useParams();
    const [selectedDate, setSelectedDate] = useState<Dayjs>(TODAY);

    const handleDateChange = (event: any) => {
        const newDate = dayjs(event.target.value);
        setSelectedDate(newDate);
        setSensorInstances([]); // Clear sensor instances when the date is changed
    };

    useEffect(() => {
        const jwtToken = localStorage.getItem('jwtToken');
        if (jwtToken) {
            try {
                const decodedToken = jwtDecode(jwtToken);

                // eslint-disable-next-line @typescript-eslint/ban-ts-comment
                // @ts-ignore
                const userRole = decodedToken.role;
                console.log("User Role:", userRole);
            } catch (error) {
                console.error("Error decoding JWT token:", error);
            }
        } else {
            console.error("JWT token not found in localStorage");
        }
        const fetchSensorData = async () => {
            if (jwtToken) {
                const optionsGet = {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${jwtToken}`,
                        'Content-Type': 'application/json',
                    },
                };
                try {
                    const response = await fetch(`${URL}/${deviceId}?date=${selectedDate.format('YYYY-MM-DD')}`, optionsGet);
                    const data: Sensor[] = await response.json();
                    console.log("Sensor Data:", data);

                    setSensorInstances((prevInstances) => {
                        const newInstances = [...prevInstances, ...data];
                        if (newInstances.length > 24) {
                            return newInstances.slice(newInstances.length - 24);
                        }
                        return newInstances;
                    });

                } catch (error) {
                    console.error("Error fetching sensor data:", error);
                }
            }
        };

        const intervalId = setInterval(fetchSensorData, 1000);

        return () => clearInterval(intervalId);

    }, [deviceId, selectedDate]);

    const chartData = {
        labels: sensorInstances.map(instance => dayjs(instance.timestamp).hour()),
        datasets: [
            {
                label: 'Sensor Value',
                data: sensorInstances.map(instance => instance.sensorValue),
                fill: false,
                borderColor: 'rgba(75,192,192,1)',
                tension: 0.1
            }
        ]
    };

    const chartOptions: ChartOptions<'line'> = {
        scales: {
            x: {
                type: 'linear',
                position: 'bottom',
                min: 0,
                max: 23
            },
            y: {
                beginAtZero: true
            }
        }
    };

    return (
        <div className={"container"}>
            <label htmlFor="date-input">Select Date: </label>
            <input
                id="date-input"
                type="date"
                value={selectedDate.format('YYYY-MM-DD')}
                onChange={handleDateChange}
            />
            <p>Selected Date: {selectedDate.format('YYYY-MM-DD')}</p>
            <Line data={chartData} options={chartOptions}/>
            <button onClick={() => {
                navigate(-1)
            }}>Back
            </button>
        </div>
    );
}

export default Monitoring;