import "../WhiteMenu/WhiteMenu.css"
import {useEffect, useState} from "react";

/**
 * Converts milliseconds to hours, minutes, and seconds.
 * @param  {Number} milliseconds The milliseconds to be converted.
 * @return {Object} Hours, minutes, and seconds equivalent to the milliseconds.
 */
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
/**
 * Converts duration to milliseconds.
 * @param  {Object} duration Hours, minutes, and seconds.
 * @return {Number} Milliseconds equivalent to the duration.
 */
function durationToMs(duration) {
    let hourMs = (duration.hours * 60 * 60 * 1000) || 0,
        minutesMs = (duration.minutes * 60 * 1000) || 0,
        secondsMs = (duration.seconds * 1000) || 0;
    return hourMs + minutesMs + secondsMs
}

/**
 * ElapsedTime represents an increasing timer.
 * @param {Object} eta The estimated time of a Truck Driver.
 * @returns {JSX.Element} A display of the current elapsed time of the client.
 */
function ElapsedTime({eta}) {

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
        }, 1000);

        return () => clearInterval(interval);
    }, [elapsedTime]);

    return (
        <div className={durationToMs(eta) >= durationToMs(elapsedTime)? "" : "Red"}>
            {elapsedTime.hours} : {elapsedTime.minutes} : {elapsedTime.seconds}
        </div>
    )
}

export default ElapsedTime;