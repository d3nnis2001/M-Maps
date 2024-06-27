import axios from 'axios';

export const getCurrentWeather = async function getCurrentWeather(lat, lon) {
    try {
        const response = await axios.get(`https://api.brightsky.dev/current_weather?lat=${lat}&lon=${lon}`)
        return response.data
    } catch (error) {
        console.log("Something went wrong when getting the current weather")
        return false;
    }
}

export const getWeatherForeCast = async function getCurrentWeatherForecast(lat, lon) {
    try {
        const from = getCurrentTime(), until = getFutureDate(10)
        const response = await axios.get(`https://api.brightsky.dev/weather?date=${from}&last_date=${until}&lat=${lat}&lon=${lon}`)
        return response.data
    } catch (error) {
        console.log("Something went wrong when getting the current weather")
        return false;
    }
}

function getCurrentTime() {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const timezoneOffset = now.getTimezoneOffset();
    const absTimezoneOffset = Math.abs(timezoneOffset);
    const timezoneHours = String(Math.floor(absTimezoneOffset / 60)).padStart(2, '0');
    const timezoneMinutes = String(absTimezoneOffset % 60).padStart(2, '0');
    const timezoneSign = timezoneOffset > 0 ? '-' : '+';
    const timezone = `${timezoneSign}${timezoneHours}:${timezoneMinutes}`;
    const formattedString = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}${timezone}`;
    return formattedString;
}

function getFutureDate(daysAhead) {
    const now = new Date();
    now.setDate(now.getDate() + daysAhead);
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const seconds = String(now.getSeconds()).padStart(2, '0');
    const timezoneOffset = now.getTimezoneOffset();
    const absTimezoneOffset = Math.abs(timezoneOffset);
    const timezoneHours = String(Math.floor(absTimezoneOffset / 60)).padStart(2, '0');
    const timezoneMinutes = String(absTimezoneOffset % 60).padStart(2, '0');
    const timezoneSign = timezoneOffset > 0 ? '-' : '+';
    const timezone = `${timezoneSign}${timezoneHours}:${timezoneMinutes}`;
    const formattedString = `${year}-${month}-${day}T${hours}:${minutes}:${seconds}${timezone}`;
    return formattedString;
}
