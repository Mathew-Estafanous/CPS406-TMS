import "../WhiteMenu/WhiteMenu.css"
import {useEffect, useState} from "react";

function msToTime(milliseconds) {
    let seconds = Math.floor((milliseconds / 1000) % 60),
        minutes = Math.floor((milliseconds / (1000 * 60)) % 60),
        hours = Math.floor((milliseconds / (1000 * 60 * 60)) % 24);

    hours = (hours < 10) ? "0" + hours : hours;
    minutes = (minutes < 10) ? "0" + minutes : minutes;
    seconds = (seconds < 10) ? "0" + seconds : seconds;

    return {
        "hours": hours,
        "minutes": minutes,
        "seconds": seconds
    }
}

function ElapsedTime() {
    //dockingAreaTime is milliseconds
    let startingTime = JSON.parse(sessionStorage.getItem("dockingAreaTime"))
    if (startingTime === null) {
        startingTime = new Date().getTime();
        sessionStorage.setItem("dockingAreaTime", startingTime);
    }
    const [elapsedTime, setElapsedTime] = useState(msToTime(new Date().getTime()-startingTime));

    useEffect(() => {
        const interval = setInterval(() => {
            const newElapsedTime = msToTime(new Date().getTime()-startingTime);
            setElapsedTime(newElapsedTime);
        }, 1000); //Do this every 1 second XD !!1!!1

        return () => clearInterval(interval);
    }, [elapsedTime]);

    return (
        <>
            {elapsedTime.hours} : {elapsedTime.minutes} : {elapsedTime.seconds}
        </>
    )
}

export default ElapsedTime;