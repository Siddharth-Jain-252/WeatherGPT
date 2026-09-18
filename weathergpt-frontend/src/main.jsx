import React, { useCallback, useEffect, useMemo, useState } from "react";
import { createRoot } from "react-dom/client";
import { MapContainer, TileLayer, WMSTileLayer, Marker, Popup, useMap } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import {
  AlertTriangle, ArrowDown, ArrowUp, Bot, CalendarDays, Check, ChevronRight,
  Cloud, CloudRain, CloudSun, Droplets, Eye, Gauge, Home, LogIn, LogOut,
  Map as MapIcon, MapPin, Menu, MessageCircle, RefreshCw, Search, Send, Mic, Settings, ShieldCheck,
  Sun, Thermometer, User, UserPlus, Wind, X
} from "lucide-react";
import "./styles.css";

const API = "https://weathergpt-api-dgri.onrender.com";
const TOKEN_KEY = "weathergpt_token";
const USER_KEY = "weathergpt_user";

const EMPTY = "—";

function isValidNumber(value) {
  if (value === null || value === undefined || value === "") return false;
  const n = typeof value === "number" ? value : Number(value);
  return Number.isFinite(n);
}

function numberValue(value) {
  if (!isValidNumber(value)) return null;
  return typeof value === "number" ? value : Number(value);
}

function formatNumber(value, digits = 0) {
  const n = numberValue(value);
  if (n === null) return EMPTY;
  return new Intl.NumberFormat(undefined, {
    maximumFractionDigits: digits,
    minimumFractionDigits: 0
  }).format(n);
}

function formatTemperature(value) {
  const n = numberValue(value);
  return n === null ? EMPTY : `${formatNumber(n)}°`;
}

function formatPercent(value) {
  const n = numberValue(value);
  return n === null ? EMPTY : `${formatNumber(n)}%`;
}

function formatUnit(value, unit, digits = 0) {
  const n = numberValue(value);
  return n === null ? EMPTY : `${formatNumber(n, digits)} ${unit}`;
}

function cleanText(value, fallback = EMPTY) {
  return typeof value === "string" && value.trim() ? value.trim() : fallback;
}

function field(object, camelName, snakeName) {
  if (!object || typeof object !== "object") return undefined;
  if (object[camelName] !== undefined && object[camelName] !== null) return object[camelName];
  if (snakeName && object[snakeName] !== undefined && object[snakeName] !== null) return object[snakeName];
  return undefined;
}

function currentField(current, camelName, snakeName) {
  return field(current, camelName, snakeName);
}

function dayField(day, camelName, snakeName) {
  return field(day, camelName, snakeName);
}

function getStoredUser() {
  try {
    const raw = localStorage.getItem(USER_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

function saveAuth(data) {
  if (data?.token) localStorage.setItem(TOKEN_KEY, data.token);
  if (data?.user) localStorage.setItem(USER_KEY, JSON.stringify(data.user));
}

function clearAuth() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(USER_KEY);
}

function extractErrorMessage(body, status) {
  if (!body) return `Request failed (${status})`;
  if (typeof body === "string" && body.trim()) return body;
  return cleanText(
    body.message ||
    body.error ||
    body.detail ||
    body.title ||
    body?.errors?.[0]?.message,
    `Request failed (${status})`
  );
}

async function request(path, options = {}) {
  const headers = {
    Accept: "application/json",
    ...(options.body ? { "Content-Type": "application/json" } : {}),
    ...(options.headers || {})
  };

  const token = localStorage.getItem(TOKEN_KEY);
  if (token) headers.Authorization = `Bearer ${token}`;

  let response;

  try {
    response = await fetch(`${API}${path}`, {
      ...options,
      headers
    });
  } catch {
    throw new Error(
      "Unable to reach the WeatherGPT backend. Please check your internet connection or try again."
    );
  }

  const contentType = response.headers.get("content-type") || "";
  let body = null;

  try {
    if (contentType.includes("application/json")) {
      body = await response.json();
    } else {
      const text = await response.text();
      body = text || null;
    }
  } catch {
    body = null;
  }

  if (!response.ok) {
    if (response.status === 401) {
      clearAuth();
      window.dispatchEvent(new Event("weathergpt:logout"));
    }
    throw new Error(extractErrorMessage(body, response.status));
  }

  return body;
}

function weatherIcon(text = "", size = 24) {
  const t = String(text || "").toLowerCase();

  if (t.includes("thunder")) return <CloudRain size={size} />;
  if (
    t.includes("rain") ||
    t.includes("drizzle") ||
    t.includes("shower") ||
    t.includes("storm")
  ) {
    return <CloudRain size={size} />;
  }
  if (t.includes("snow") || t.includes("sleet") || t.includes("ice")) {
    return <Cloud size={size} />;
  }
  if (t.includes("cloud") || t.includes("overcast")) {
    return <CloudSun size={size} />;
  }
  if (t.includes("clear") || t.includes("sunny")) {
    return <Sun size={size} />;
  }
  return <CloudSun size={size} />;
}

/*
 * The backend returns forecast dates in the location's local calendar.
 * We deliberately calculate tomorrow from the first forecast date instead
 * of using the browser timezone. This avoids an India/browser timezone
 * mismatch when a searched city is elsewhere in the world.
 */
function parseDateOnly(dateString) {
  if (!dateString || typeof dateString !== "string") return null;
  const match = dateString.match(/^(\d{4})-(\d{2})-(\d{2})$/);
  if (!match) return null;
  return new Date(Date.UTC(
    Number(match[1]),
    Number(match[2]) - 1,
    Number(match[3])
  ));
}

function dateOnlyString(date) {
  if (!(date instanceof Date) || Number.isNaN(date.getTime())) return null;
  return [
    date.getUTCFullYear(),
    String(date.getUTCMonth() + 1).padStart(2, "0"),
    String(date.getUTCDate()).padStart(2, "0")
  ].join("-");
}

function addDaysToDateString(dateString, days) {
  const date = parseDateOnly(dateString);
  if (!date) return null;
  date.setUTCDate(date.getUTCDate() + days);
  return dateOnlyString(date);
}

function formatForecastDate(dateString, options = { weekday: "long" }) {
  const date = parseDateOnly(dateString);
  if (!date) return EMPTY;
  return new Intl.DateTimeFormat(undefined, {
    ...options,
    timeZone: "UTC"
  }).format(date);
}

function getForecastDays(forecast) {
  const days = forecast?.forecast?.forecastday;
  return Array.isArray(days) ? days.filter(Boolean) : [];
}

function findForecastDay(forecast, wantedDate) {
  const days = getForecastDays(forecast);
  if (!days.length) return null;

  if (wantedDate) {
    const exact = days.find(d => d?.date === wantedDate);
    if (exact) return exact;
  }

  return null;
}

function forecastDaySummary(day, locationName = "") {
  const d = day?.day || {};
  const condition = cleanText(d?.condition?.text, "Weather data available");
  const name = cleanText(locationName, "your location");

  const high = formatTemperature(dayField(d, "maxTempC", "maxtemp_c"));
  const low = formatTemperature(dayField(d, "minTempC", "mintemp_c"));
  const rain = formatPercent(dayField(d, "dailyChanceOfRain", "daily_chance_of_rain"));
  const humidity = formatPercent(dayField(d, "avgHumidity", "avghumidity"));
  const wind = formatUnit(dayField(d, "maxWindKph", "maxwind_kph"), "km/h");

  return [
    `${formatForecastDate(day?.date)} in ${name}: ${condition}.`,
    `High ${high}, low ${low}.`,
    `Rain chance ${rain}.`,
    `Average humidity ${humidity}.`,
    `Maximum wind ${wind}.`
  ].join(" ");
}

function isTomorrowForecastQuestion(text) {
  const q = String(text || "").toLowerCase();

  const tomorrow =
    /\btomorrow\b/.test(q) ||
    /\bnext\s+day\b/.test(q) ||
    /\bthe\s+next\s+day\b/.test(q);

  const weatherWords =
    /\b(weather|forecast|temperature|temp|rain|rainfall|rainy|umbrella|wind|humidity|cloud|sunny|outdoor|outside|market|walk|sport|sports|go out|travel|trip)\b/.test(q);

  return tomorrow && weatherWords;
}

function isGeneralForecastQuestion(text) {
  const q = String(text || "").toLowerCase();
  return /\b(weather|forecast|temperature|temp|rain|rainfall|umbrella|wind|humidity|cloud|sunny)\b/.test(q);
}

function inferLocationFromText(text) {
  const q = String(text || "");

  const patterns = [
    /\b(?:in|at|for|near)\s+([A-Za-z][A-Za-z .'-]{1,50})\s*$/i,
    /\b(?:weather|forecast)\s+(?:of|for)\s+([A-Za-z][A-Za-z .'-]{1,50})\s*$/i
  ];

  for (const pattern of patterns) {
    const match = q.match(pattern);
    if (match?.[1]) {
      return match[1].trim().replace(/[?.!,]+$/, "");
    }
  }

  return null;
}

async function forecastFallbackAnswer(query, location) {
  const explicitLocation = inferLocationFromText(query);
  const city = cleanText(explicitLocation || location, "");

  if (!city) {
    throw new Error("Please set a home location or include a city in your question.");
  }

  const forecast = await request(
    `/api/v1/weather/week/city?city=${encodeURIComponent(city)}`
  );

  const days = getForecastDays(forecast);
  if (!days.length) {
    throw new Error("The backend returned no forecast data for this location.");
  }

  const locationName = cleanText(
    forecast?.location?.name,
    city
  );

  const firstDate = days[0]?.date;
  const tomorrowDate = addDaysToDateString(firstDate, 1);
  const tomorrow = findForecastDay(forecast, tomorrowDate);

  if (!tomorrow) {
    throw new Error("Tomorrow's forecast is not available in the returned 7-day forecast.");
  }

  return {
    response:
      `Here is tomorrow's forecast for ${locationName}. ` +
      forecastDaySummary(tomorrow, locationName),
    intent: "tomorrow_weather_forecast",
    location: locationName,
    requiredCapabilities: ["week_weather_data"],
    fallback: true
  };
}

function App() {
  const [user, setUser] = useState(getStoredUser());
  const [view, setView] = useState("dashboard");
  const [mobileOpen, setMobileOpen] = useState(false);
  const [location, setLocation] = useState(
    getStoredUser()?.homeLocation || "Jalandhar"
  );
  const [search, setSearch] = useState(
    getStoredUser()?.homeLocation || "Jalandhar"
  );
  const [weather, setWeather] = useState(null);
  const [forecast, setForecast] = useState(null);
  const [loading, setLoading] = useState(false);
  const [notice, setNotice] = useState("");
  const [authMode, setAuthMode] = useState("login");
  const [refreshKey, setRefreshKey] = useState(0);
  const [locationLoading, setLocationLoading] = useState(false);

  const authenticated = !!localStorage.getItem(TOKEN_KEY);

  /*
   * Use the browser's Geolocation API to get the user's current position.
   * We then reverse-geocode the coordinates into a city and feed that city
   * into the existing WeatherGPT weather pipeline.
   *
   * Geolocation works on HTTPS (and localhost during development) and the
   * browser will ask the user for permission the first time.
   */


  const navigate = useCallback((next) => {
    setView(next);
    setMobileOpen(false);
    setNotice("");
  }, []);

  const loadWeather = useCallback(async (city = location) => {
    const cleanCity = String(city || "").trim();

    if (!cleanCity) {
      setNotice("Please enter a city name.");
      return;
    }

    setLoading(true);
    setNotice("");

    try {
      
      const [current, week] = await Promise.all([
        request(
          `/api/v1/weather/current/city?city=${encodeURIComponent(cleanCity)}`
        ),
        request(
          `/api/v1/weather/week/city?city=${encodeURIComponent(cleanCity)}`
        )
      ]);

      setWeather(current || null);
      setForecast(week || null);

      const resolved =
        cleanText(current?.location?.name, "") ||
        cleanText(week?.location?.name, "") ||
        cleanCity;

      setLocation(resolved);
      setSearch(resolved);
    } catch (error) {
      setNotice(error?.message || "Unable to load weather.");
    } finally {
      setLoading(false);
    }
  }, [location]);

  const useCurrentLocation = useCallback(() => {
    if (!navigator.geolocation) {
      setNotice("Current location is not supported by this browser.");
      return;
    }

    setLocationLoading(true);
    setNotice("");

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        const { latitude, longitude } = position.coords;

        try {
          
          const response = await fetch(
            `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${encodeURIComponent(latitude)}&lon=${encodeURIComponent(longitude)}&zoom=10&addressdetails=1`,
            {
              headers: {
                Accept: "application/json"
              }
            }
          );

          if (!response.ok) {
            throw new Error("Unable to determine the city from your location.");
          }

          const data = await response.json();
          const address = data?.address || {};

          const city =
            address.city ||
            address.town ||
            address.municipality ||
            address.village ||
            address.county;

          if (!city) {
            throw new Error("Your coordinates were found, but a city could not be determined.");
          }

          setLocation(city);
          setSearch(city);
          await loadWeather(city);
          setNotice(`Using your current location: ${city}`);
        } catch (error) {
          setNotice(
            error?.message ||
            "Your location was found, but WeatherGPT could not determine the city."
          );
        } finally {
          setLocationLoading(false);
        }
      },
      (error) => {
        const messages = {
          1: "Location permission was denied. Allow location access in your browser and try again.",
          2: "Your current location could not be determined. Check your device location services.",
          3: "Location timed out. On a laptop/desktop, make sure Windows Location is enabled and your browser has permission for this site. You can also try again."
        };

        setNotice(
          messages[error?.code] ||
          "Unable to access your current location."
        );
        setLocationLoading(false);
      },
      {
        enableHighAccuracy: false,
        timeout: 30000,
        maximumAge: 600000
      }
    );
  }, [loadWeather]);

  useEffect(() => {
    if (!authenticated) return;

    const initialLocation =
      cleanText(user?.homeLocation, "") || "Jalandhar";

    loadWeather(initialLocation);
    
  }, [authenticated]);

  useEffect(() => {
    if (!refreshKey || !authenticated) return;
    loadWeather(location);
  }, [refreshKey, authenticated, location, loadWeather]);

  useEffect(() => {
    const handleForcedLogout = () => {
      setUser(null);
      setWeather(null);
      setForecast(null);
      setView("dashboard");
    };

    window.addEventListener("weathergpt:logout", handleForcedLogout);
    return () =>
      window.removeEventListener("weathergpt:logout", handleForcedLogout);
  }, []);

  const logout = useCallback(() => {
    clearAuth();
    setUser(null);
    setWeather(null);
    setForecast(null);
    setNotice("");
    setView("dashboard");
  }, []);

  const handleAuth = useCallback((data) => {
    saveAuth(data);
    setUser(data?.user || getStoredUser());
    setAuthMode("login");
  }, []);

  if (!authenticated) {
    return (
      <AuthScreen
        mode={authMode}
        setMode={setAuthMode}
        onAuth={handleAuth}
      />
    );
  }

  return (
    <div className="app-shell">
      <Sidebar
        view={view}
        navigate={navigate}
        open={mobileOpen}
        onClose={() => setMobileOpen(false)}
        logout={logout}
      />

      <main className="main">
        <header className="topbar">
          <button
            className="icon-btn mobile-menu"
            onClick={() => setMobileOpen(true)}
            aria-label="Open menu"
          >
            <Menu size={21} />
          </button>

          <div className="topbar-search">
            <Search size={18} />
            <form
              onSubmit={(event) => {
                event.preventDefault();
                loadWeather(search);
              }}
            >
              <input
                value={search}
                onChange={(event) => setSearch(event.target.value)}
                placeholder="Search city..."
                aria-label="Search city"
              />
            </form>
          </div>

          <button
            className={`location-btn ${locationLoading ? "loading" : ""}`}
            onClick={useCurrentLocation}
            disabled={locationLoading || loading}
            title="Use my current location"
            aria-label="Use my current location"
            type="button"
          >
            <MapPin size={17} className={locationLoading ? "spin" : ""} />
            <span>{locationLoading ? "Locating..." : "Use my location"}</span>
          </button>

          <div className="top-actions">
            <button
              className="icon-btn"
              onClick={() => setRefreshKey(k => k + 1)}
              title="Refresh"
              aria-label="Refresh weather"
              disabled={loading}
            >
              <RefreshCw className={loading ? "spin" : ""} size={18} />
            </button>

            <div className="user-chip">
              <div className="avatar">
                {cleanText(user?.username, "U").slice(0, 1).toUpperCase()}
              </div>
              <span>{cleanText(user?.username, "User")}</span>
            </div>
          </div>
        </header>

        {notice && (
          <div className="notice" role="alert">
            <AlertTriangle size={17} />
            <span>{notice}</span>
            <button
              onClick={() => setNotice("")}
              aria-label="Dismiss message"
            >
              <X size={16} />
            </button>
          </div>
        )}

        {view === "dashboard" && (
          <Dashboard
            weather={weather}
            forecast={forecast}
            location={location}
            loading={loading}
            navigate={navigate}
          />
        )}

        {view === "forecast" && (
          <Forecast forecast={forecast} loading={loading} />
        )}

        {view === "map" && (
          <WeatherMap location={location} weather={weather} />
        )}

        {view === "chat" && (
          <Chat
            user={user}
            location={location}
            setLocation={setLocation}
            setSearch={setSearch}
          />
        )}

        {view === "profile" && (
          <Profile
            user={user}
            setUser={setUser}
            onLocationChanged={(nextLocation) => {
              const clean = String(nextLocation || "").trim();
              if (clean) {
                setLocation(clean);
                setSearch(clean);
                loadWeather(clean);
              }
            }}
          />
        )}

        {view === "settings" && <SettingsPage />}
      </main>
    </div>
  );
}

function Sidebar({ view, navigate, open, onClose, logout }) {
  const items = [
    ["dashboard", Home, "Overview"],
    ["forecast", CalendarDays, "Forecast"],
    ["map", MapIcon, "Map"],
    ["chat", MessageCircle, "WeatherGPT"],
    ["profile", User, "Profile"],
    ["settings", Settings, "Settings"]
  ];

  return (
    <>
      {open && <div className="overlay" onClick={onClose} />}

      <aside className={`sidebar ${open ? "open" : ""}`}>
        <div className="brand">
          <div className="brand-mark">
            <CloudSun size={23} />
          </div>

          <div>
            <strong>WeatherGPT</strong>
            <small>Forecast intelligence</small>
          </div>

          <button
            className="icon-btn side-close"
            onClick={onClose}
            aria-label="Close menu"
          >
            <X size={18} />
          </button>
        </div>

        <nav>
          <div className="nav-label">Workspace</div>

          {items.map(([id, Icon, label]) => (
            <button
              key={id}
              className={`nav-item ${view === id ? "active" : ""}`}
              onClick={() => navigate(id)}
            >
              <Icon size={19} />
              <span>{label}</span>
              {view === id && (
                <ChevronRight size={16} className="nav-arrow" />
              )}
            </button>
          ))}
        </nav>

        <div className="sidebar-bottom">
          

          <button className="nav-item logout" onClick={logout}>
            <LogOut size={18} />
            <span>Sign out</span>
          </button>
        </div>
      </aside>
    </>
  );
}

function Dashboard({ weather, forecast, location, loading, navigate }) {
  const current = weather?.current || null;
  const loc = weather?.location || forecast?.location || null;
  const days = getForecastDays(forecast);
  const condition = cleanText(current?.condition?.text, "Weather data unavailable");

  return (
    <div className="page">
      <section className="welcome">
        <div>
          <p className="eyebrow">WEATHER OVERVIEW</p>
          <h1>{cleanText(loc?.name, location)}</h1>
          <p className="muted">
            {loc
              ? [
                  cleanText(loc.region, ""),
                  cleanText("India", "")
                ].filter(Boolean).join(", ") || "Current weather"
              : "Search a city to get started"}
          </p>
        </div>

        <button className="primary" onClick={() => navigate("chat")}>
          <MessageCircle size={17} />
          Ask WeatherGPT
        </button>
      </section>

      {loading && (
        <div className="loading-card">
          <RefreshCw className="spin" size={20} />
          Updating weather data...
        </div>
      )}

      <section className="hero-weather">
        <div className="hero-left">
          <div className="hero-location">
            <MapPin size={16} />
            {cleanText(loc?.name, location)}
          </div>

          <div className="temp-row">
            <span className="big-temp">
              {formatNumber(currentField(current, "tempC", "temp_c"))}
              {numberValue(currentField(current, "tempC", "temp_c")) === null ? "" : "°"}
            </span>
            <span className="unit">C</span>
          </div>

          <div className="condition">
            {weatherIcon(condition, 21)}
            <span>{condition}</span>
            <span className="dot-sep">•</span>
            <span>
              Feels like {formatTemperature(currentField(current, "feelsLikeC", "feelslike_c"))}
            </span>
          </div>
        </div>

        <div className="hero-visual">
          {weatherIcon(condition, 90)}
        </div>

        <div className="hero-right">
          <span>Wind</span>
          <b>
            {formatNumber(currentField(current, "windKph", "wind_kph"))}
            <small>{numberValue(currentField(current, "windKph", "wind_kph")) === null ? "" : " km/h"}</small>
          </b>

          <span>Humidity</span>
          <b>
            {formatNumber(current?.humidity)}
            <small>{numberValue(current?.humidity) === null ? "" : "%"}</small>
          </b>
        </div>
      </section>

      <section className="section-head">
        <div>
          <h2>Today's details</h2>
          <p>Current conditions at {cleanText(loc?.name, location)}</p>
        </div>

        <button className="text-btn" onClick={() => navigate("forecast")}>
          Full forecast <ChevronRight size={16} />
        </button>
      </section>

      <div className="metrics">
        <Metric
          icon={<Thermometer />}
          label="Feels like"
          value={formatTemperature(currentField(current, "feelsLikeC", "feelslike_c"))}
        />

        <Metric
          icon={<Wind />}
          label="Wind"
          value={formatUnit(currentField(current, "windKph", "wind_kph"), "km/h")}
          extra={cleanText(currentField(current, "windDir", "wind_dir"), "")}
        />

        <Metric
          icon={<Droplets />}
          label="Rain chance"
          value={formatPercent(currentField(current, "chanceOfRain", "chance_of_rain"))}
        />

        <Metric
          icon={<Gauge />}
          label="Pressure"
          value={formatUnit(currentField(current, "pressureMb", "pressure_mb"), "hPa")}
        />

        <Metric
          icon={<Eye />}
          label="Cloud cover"
          value={formatPercent(currentField(current, "cloud", "cloud"))}
        />

        <Metric
          icon={<Sun />}
          label="UV index"
          value={formatNumber(currentField(current, "uv", "uv"), 1) === EMPTY
            ? EMPTY
            : formatNumber(currentField(current, "uv", "uv"), 1)}
        />
      </div>

      <section className="section-head forecast-title">
        <div>
          <h2>7-day outlook</h2>
          <p>Plan ahead with the latest forecast</p>
        </div>
      </section>

      <div className="forecast-strip">
        {days.slice(0, 7).map((day, index) => (
          <DayCard
            key={day?.date || index}
            day={day}
            first={index === 0}
          />
        ))}

        {!days.length && !loading && (
          <div className="empty">
            Forecast will appear here after you search for a city.
          </div>
        )}
      </div>

      <section className="quick-grid">
        <button className="quick-card" onClick={() => navigate("chat")}>
          <div className="quick-icon">
            <Bot size={21} />
          </div>
          <div>
            <b>WeatherGPT assistant</b>
            <span>Ask whether the weather is right for your plans.</span>
          </div>
          <ChevronRight />
        </button>

        <div className="quick-card static">
          <div className="quick-icon">
            <ShieldCheck size={21} />
          </div>
          <div>
            <b>Live data</b>
            <span>Weather is retrieved through your deployed backend.</span>
          </div>
          <Check />
        </div>
      </section>
    </div>
  );
}

function Metric({ icon, label, value, extra }) {
  return (
    <div className="metric">
      <div className="metric-icon">{icon}</div>
      <div>
        <span>{label}</span>
        <b>{value}</b>
        {extra && <small>{extra}</small>}
      </div>
    </div>
  );
}

function DayCard({ day, first }) {
  const d = day?.day || {};
  const label = first
    ? "Today"
    : formatForecastDate(day?.date, { weekday: "short" });

  return (
    <div className={`day-card ${first ? "selected" : ""}`}>
      <span className="day-label">{label}</span>

      <div className="day-icon">
        {weatherIcon(d?.condition?.text, 28)}
      </div>

      <b className="day-temp">
        {formatTemperature(dayField(d, "avgTempC", "avgtemp_c"))}
      </b>

      <span className="day-range">
        <ArrowUp size={12} />
        {formatTemperature(dayField(d, "maxTempC", "maxtemp_c"))}
        <ArrowDown size={12} />
        {formatTemperature(dayField(d, "minTempC", "mintemp_c"))}
      </span>

      <span className="rain">
        <Droplets size={12} />
        {formatPercent(dayField(d, "dailyChanceOfRain", "daily_chance_of_rain"))}
      </span>
    </div>
  );
}

function Forecast({ forecast, loading }) {
  const days = getForecastDays(forecast);

  return (
    <div className="page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">EXTENDED OUTLOOK</p>
          <h1>Forecast</h1>
          <p className="muted">A clear view of the week ahead.</p>
        </div>
      </div>

      {loading ? (
        <div className="loading-card">
          <RefreshCw className="spin" size={20} />
          Loading forecast...
        </div>
      ) : (
        <div className="forecast-list">
          {days.map((day, index) => (
            <div className="forecast-row" key={day?.date || index}>
              <div className="forecast-day">
                <b>
                  {index === 0
                    ? "Today"
                    : formatForecastDate(day?.date, { weekday: "long" })}
                </b>
                <span>{cleanText(day?.date)}</span>
              </div>

              <div className="forecast-condition">
                {weatherIcon(day?.day?.condition?.text, 24)}
                <span>
                  {cleanText(day?.day?.condition?.text)}
                </span>
              </div>

              <div className="forecast-high">
                <ArrowUp size={15} />
                {formatTemperature(dayField(day?.day, "maxTempC", "maxtemp_c"))}
              </div>

              <div className="forecast-low">
                <ArrowDown size={15} />
                {formatTemperature(dayField(day?.day, "minTempC", "mintemp_c"))}
              </div>

              <div className="forecast-rain">
                <Droplets size={15} />
                {formatPercent(dayField(day?.day, "dailyChanceOfRain", "daily_chance_of_rain"))}
              </div>

              <div className="forecast-humidity">
                <Droplets size={15} />
                {formatPercent(dayField(day?.day, "avgHumidity", "avghumidity"))}
              </div>
            </div>
          ))}

          {!days.length && (
            <div className="empty">
              No forecast data is available. Search for a city again.
            </div>
          )}
        </div>
      )}
    </div>
  );
}

function Chat({ user, location, setLocation, setSearch }) {
  const [messages, setMessages] = useState(() => [
    {
      id: crypto.randomUUID?.() || String(Date.now()),
      role: "assistant",
      text:
        `Hi ${cleanText(user?.username, "there")}. ` +
        "Tell me what you're planning and I'll use the weather to help you decide."
    }
  ]);

  const [text, setText] = useState("");
  const [busy, setBusy] = useState(false);

  // Browser-native speech-to-text state.
  const [listening, setListening] = useState(false);
  const [speechSupported, setSpeechSupported] = useState(true);
  const [speechError, setSpeechError] = useState("");

  const recognitionRef = React.useRef(null);
  const textBeforeListeningRef = React.useRef("");

  useEffect(() => {
    const SpeechRecognition =
      window.SpeechRecognition ||
      window.webkitSpeechRecognition;

    if (!SpeechRecognition) {
      setSpeechSupported(false);
      return undefined;
    }

    const recognition = new SpeechRecognition();

    recognition.continuous = true;
    recognition.interimResults = true;
    recognition.lang = "en-IN";
    recognition.maxAlternatives = 1;

    recognition.onstart = () => {
      setListening(true);
      setSpeechError("");
    };

    recognition.onresult = (event) => {
      let finalText = "";
      let interimText = "";

      for (let i = event.resultIndex; i < event.results.length; i += 1) {
        const transcript = event.results[i][0]?.transcript || "";

        if (event.results[i].isFinal) {
          finalText += transcript;
        } else {
          interimText += transcript;
        }
      }

      setText(current => {
        const base = textBeforeListeningRef.current.trim();

        if (finalText.trim()) {
          const combined = [base, finalText.trim()]
            .filter(Boolean)
            .join(" ");

          textBeforeListeningRef.current = combined;
          return combined;
        }

        const preview = [base, interimText.trim()]
          .filter(Boolean)
          .join(" ");

        return preview;
      });
    };

    recognition.onerror = (event) => {
      setListening(false);

      const errors = {
        "not-allowed":
          "Microphone permission was denied. Allow microphone access for localhost in Chrome.",
        "service-not-allowed":
          "Speech recognition is not allowed by this browser.",
        "audio-capture":
          "No microphone was found. Check your microphone connection.",
        "no-speech":
          "I didn't hear anything. Tap the microphone and try again.",
        "network":
          "Speech recognition needs a network connection in this browser.",
        "aborted":
          ""
      };

      const message =
        errors[event.error] ||
        "Speech recognition could not start. Please try again.";

      setSpeechError(message);
    };

    recognition.onend = () => {
      setListening(false);
    };

    recognitionRef.current = recognition;

    return () => {
      recognition.onend = null;
      recognition.onerror = null;
      recognition.onresult = null;
      recognition.onstart = null;

      try {
        recognition.stop();
      } catch {
      }

      recognitionRef.current = null;
    };
  }, []);

  const toggleMicrophone = useCallback(() => {
    if (busy) return;

    const recognition = recognitionRef.current;

    if (!recognition) {
      setSpeechSupported(false);
      setSpeechError(
        "Speech-to-text is not supported by this browser. Try Google Chrome or Microsoft Edge."
      );
      return;
    }

    if (listening) {
      try {
        recognition.stop();
      } catch {
      }

      setListening(false);
      return;
    }

    setSpeechError("");
    textBeforeListeningRef.current = text.trim();

    try {
      recognition.start();
    } catch (error) {
      if (error?.name !== "InvalidStateError") {
        setSpeechError(
          "Could not start the microphone. Please try again."
        );
      }
    }
  }, [busy, listening, text]);

  const appendMessage = useCallback((message) => {
    setMessages(current => [
      ...current,
      {
        id:
          crypto.randomUUID?.() ||
          `${Date.now()}-${Math.random()}`,
        ...message
      }
    ]);
  }, []);

  const send = useCallback(async (rawValue = text) => {
    const value = String(rawValue || "").trim();

    if (!value || busy) return;

    // Stop speech recognition before sending.
    if (recognitionRef.current && listening) {
      try {
        recognitionRef.current.stop();
      } catch {
        // Already stopped.
      }
      setListening(false);
    }

    appendMessage({ role: "user", text: value });
    setText("");
    textBeforeListeningRef.current = "";
    setSpeechError("");
    setBusy(true);

    try {
      const data = await request("/api/v1/chat", {
        method: "POST",
        body: JSON.stringify({ query: value })
      });

      const responseText =
        cleanText(data?.response, "") ||
        "I couldn't generate a response.";

      if (data?.location) {
        setLocation(data.location);
        setSearch(data.location);
      }

      appendMessage({
        role: "assistant",
        text: responseText,
        intent: data?.intent
      });
    } catch (error) {
      if (isTomorrowForecastQuestion(value)) {
        try {
          const fallback = await forecastFallbackAnswer(
              value,
              location
          );

          if (fallback.location) {
            setLocation(fallback.location);
            setSearch(fallback.location);
          }

          appendMessage({
            role: "assistant",
            text: fallback.response,
            intent: fallback.intent,
            fallback: true
          });

          return;
        } catch (fallbackError) {
          appendMessage({
            role: "assistant",
            text:
              fallbackError?.message ||
              "I couldn't retrieve tomorrow's forecast."
          });

          return;
        }
      }

      if (isGeneralForecastQuestion(value)) {
        try {
          const explicitLocation = inferLocationFromText(value);
          const city = cleanText(explicitLocation || location, "");

          if (city) {
            const fallback = await request(
              `/api/v1/weather/week/city?city=${encodeURIComponent(city)}`
            );

            const days = getForecastDays(fallback);

            if (days.length) {
              const name = cleanText(
                fallback?.location?.name,
                city
              );

              appendMessage({
                role: "assistant",
                text:
                  `I couldn't reach the AI response service, but I can still show the live forecast for ${name}. ` +
                  `Today: ${buildTomorrowAnswer(days[0], name)}`,
                intent: "weather_data_fallback",
                fallback: true
              });

              setLocation(name);
              setSearch(name);

              return;
            }
          }
        } catch {
          // Fall through to the original error.
        }
      }

      appendMessage({
        role: "assistant",
        text:
          error?.message ||
          "Something went wrong while contacting WeatherGPT."
      });
    } finally {
      setBusy(false);
    }
  }, [
    appendMessage,
    busy,
    location,
    listening,
    setLocation,
    setSearch,
    text
  ]);

  const suggestions = useMemo(
    () => [
      "What is tomorrow's weather?",
      "Will I need an umbrella?",
      "Is it good for outdoor sports?"
    ],
    []
  );

  return (
    <div className="chat-page page">
      <div className="chat-head">
        <div className="bot-badge">
          <Bot />
        </div>

        <div>
          <p className="eyebrow">WEATHER ASSISTANT</p>
          <h1>WeatherGPT</h1>
          <p className="muted">
            Practical weather advice, grounded in live conditions.
          </p>
        </div>
      </div>

      <div className="chat-window">
        <div className="messages">
          {messages.map(message => (
            <div
              key={message.id}
              className={`message ${message.role}`}
            >
              <div className="message-avatar">
                {message.role === "assistant"
                  ? <Bot size={16} />
                  : <User size={16} />}
              </div>

              <div className="bubble">
                <div>{message.text}</div>

                {message.intent && (
                  <small className="intent">
                    Intent: {message.intent}
                    {message.fallback
                      ? " • Weather data fallback"
                      : ""}
                  </small>
                )}
              </div>
            </div>
          ))}

          {busy && (
            <div className="message assistant">
              <div className="message-avatar">
                <Bot size={16} />
              </div>

              <div className="bubble typing">
                Thinking
                <span>.</span>
                <span>.</span>
                <span>.</span>
              </div>
            </div>
          )}
        </div>

        <div className="suggestions">
          {suggestions.map(suggestion => (
            <button
              key={suggestion}
              onClick={() => send(suggestion)}
              disabled={busy}
            >
              {suggestion}
            </button>
          ))}
        </div>

        {speechError && (
          <div className="speech-error" role="status">
            <AlertTriangle size={15} />
            <span>{speechError}</span>

            <button
              type="button"
              onClick={() => setSpeechError("")}
              aria-label="Dismiss speech error"
            >
              <X size={14} />
            </button>
          </div>
        )}

        <form
          className="chat-input"
          onSubmit={event => {
            event.preventDefault();
            send();
          }}
        >
          <input
            value={text}
            onChange={event => {
              setText(event.target.value);
              textBeforeListeningRef.current =
                event.target.value;
            }}
            placeholder={
              listening
                ? "Listening..."
                : "Ask about the weather..."
            }
            aria-label="Ask WeatherGPT"
            disabled={busy}
          />

          <button
            type="button"
            className={`mic-button ${listening ? "listening" : ""}`}
            onClick={toggleMicrophone}
            disabled={busy || !speechSupported}
            title={
              !speechSupported
                ? "Speech-to-text is not supported in this browser"
                : listening
                  ? "Stop voice typing"
                  : "Start voice typing"
            }
            aria-label={
              listening
                ? "Stop voice typing"
                : "Start voice typing"
            }
          >
            <Mic size={18} />
          </button>

          <button
            type="submit"
            className="send"
            disabled={busy || !text.trim()}
            aria-label="Send message"
          >
            <Send size={18} />
          </button>
        </form>
      </div>
    </div>
  );
}


function WeatherMap({ location, weather }) {
  const [coordinates, setCoordinates] = useState(null);
  const [mapLoading, setMapLoading] = useState(true);
  const [mapError, setMapError] = useState("");

  useEffect(() => {
    let cancelled = false;

    async function geocodeLocation() {
  const query = String(location || "").trim();

  if (!query) return;

  setMapLoading(true);
  setMapError("");

  try {
    const response = await fetch(
      `https://geocoding-api.open-meteo.com/v1/search?name=${encodeURIComponent(
        query
      )}&count=1&language=en&format=json`
    );

    if (!response.ok) {
      throw new Error("Unable to locate this place.");
    }

    const data = await response.json();

    if (!data?.results?.length) {
      throw new Error(`Could not find "${query}" on the map.`);
    }

    const result = data.results[0];

    const lat = Number(result.latitude);
    const lon = Number(result.longitude);

    if (!Number.isFinite(lat) || !Number.isFinite(lon)) {
      throw new Error("The map service returned invalid coordinates.");
    }

    if (!cancelled) {
      setCoordinates([lat, lon]);
    }
  } catch (error) {
    if (!cancelled) {
      setMapError(
        error?.message || "Unable to locate this place on the map."
      );
      setCoordinates(null);
    }
  } finally {
    if (!cancelled) {
      setMapLoading(false);
    }
  }
}

    geocodeLocation();
    return () => { cancelled = true; };
  }, [location]);

  const current = weather?.current || null;

  return (
    <div className="page map-page">
      <div className="page-heading map-heading">
        <div>
          <p className="eyebrow">GEOSPATIAL WEATHER</p>
          <h1>Weather Map</h1>
          <p className="muted">
            Explore geographic and weather data for {location || "your location"}.
          </p>
        </div>
        <div className="map-location-badge">
          <MapPin size={16} />
          <span>{location || "Unknown location"}</span>
        </div>
      </div>

      <div className="map-layout">
        <section className="map-card">
          {mapLoading && (
            <div className="map-loading">
              <RefreshCw className="spin" size={18} />
              <span>Loading map...</span>
            </div>
          )}

          {mapError && (
            <div className="map-error">
              <AlertTriangle size={18} />
              <span>{mapError}</span>
            </div>
          )}

          {coordinates && (
            <QGISLeafletMap
              latitude={coordinates[0]}
              longitude={coordinates[1]}
              location={location}
            />
          )}
        </section>

        <aside className="map-info">
          <div className="map-info-card">
            <div className="map-info-icon"><MapPin size={19} /></div>
            <div><span>Location</span><strong>{location || EMPTY}</strong></div>
          </div>
          <div className="map-info-card">
            <div className="map-info-icon"><Thermometer size={19} /></div>
            <div><span>Temperature</span><strong>{formatTemperature(currentField(current, "tempC", "temp_c"))}</strong></div>
          </div>
          <div className="map-info-card">
            <div className="map-info-icon"><Wind size={19} /></div>
            <div><span>Wind</span><strong>{formatUnit(currentField(current, "windKph", "wind_kph"), "km/h")}</strong></div>
          </div>
          <div className="map-info-card">
            <div className="map-info-icon"><Droplets size={19} /></div>
            <div><span>Humidity</span><strong>{formatPercent(current?.humidity)}</strong></div>
          </div>

          <div className="qgis-status-card">
            <div className="qgis-status-title"><span className="qgis-dot" /> QGIS layer</div>
            <p>
              {import.meta.env.VITE_QGIS_WMS_URL
                ? "Connected to your configured QGIS WMS service."
                : "Base map is active. Add VITE_QGIS_WMS_URL to enable your QGIS WMS layers."}
            </p>
          </div>
        </aside>
      </div>
    </div>
  );
}

function QGISLeafletMap({ latitude, longitude, location }) {
  const position = [latitude, longitude];
  const qgisUrl = import.meta.env.VITE_QGIS_WMS_URL;
  const qgisLayers = import.meta.env.VITE_QGIS_LAYERS;

  return (
    <MapContainer center={position} zoom={11} scrollWheelZoom className="weather-map">
      <TileLayer
        attribution='&copy; OpenStreetMap contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />

      {qgisUrl && qgisLayers && (
        <WMSTileLayer
          url={qgisUrl}
          params={{
            layers: qgisLayers,
            format: "image/png",
            transparent: true,
            version: "1.3.0"
          }}
          opacity={0.72}
        />
      )}

      <Marker position={position}>
        <Popup><strong>{location}</strong><br />WeatherGPT location</Popup>
      </Marker>

      <MapCenterUpdater latitude={latitude} longitude={longitude} />
      <MapControls />
    </MapContainer>
  );
}

function MapCenterUpdater({ latitude, longitude }) {
  const map = useMap();

  useEffect(() => {
    if (Number.isFinite(latitude) && Number.isFinite(longitude)) {
      map.flyTo([latitude, longitude], 11, { duration: 0.8 });
    }
  }, [map, latitude, longitude]);

  return null;
}

function MapControls() {
  const map = useMap();

  const locate = () => {
    if (!navigator.geolocation) return;
    navigator.geolocation.getCurrentPosition(
      position => {
        map.flyTo([position.coords.latitude, position.coords.longitude], 13, { duration: 0.8 });
      },
      () => {},
      { enableHighAccuracy: false, timeout: 10000, maximumAge: 600000 }
    );
  };

  return (
    <div className="custom-map-controls">
      <button type="button" onClick={() => map.zoomIn()} title="Zoom in" aria-label="Zoom in">+</button>
      <button type="button" onClick={() => map.zoomOut()} title="Zoom out" aria-label="Zoom out">−</button>
      <button type="button" onClick={locate} title="My location" aria-label="My location"><MapPin size={16} /></button>
    </div>
  );
}

function AuthScreen({ mode, setMode, onAuth }) {
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    role: "normal_user",
    homeLocation: ""
  });

  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  const submit = async event => {
    event.preventDefault();
    setBusy(true);
    setError("");

    try {
      if (mode === "register") {
        const username = form.username.trim();
        const email = form.email.trim();
        const homeLocation = form.homeLocation.trim();
        const role = form.role;

        if (!role) {
          throw new Error("Please select a role.");
        }

        if (username.length < 3) {
          throw new Error("Username must contain at least 3 characters.");
        }

        if (form.password.length < 8) {
          throw new Error("Password must contain at least 8 characters.");
        }

        await request("/api/v1/auth/register", {
          method: "POST",
          body: JSON.stringify({
            username,
            email,
            role,
            homeLocation,
            workLocation: "",
            password: form.password
          })
        });

        setMode("login");
        setError("Account created. You can sign in now.");
        setForm(current => ({
          ...current,
          password: ""
        }));
      } else {
        const data = await request("/api/v1/auth/login", {
          method: "POST",
          body: JSON.stringify({
            email: form.email.trim(),
            password: form.password
          })
        });

        onAuth(data);
      }
    } catch (error) {
      setError(error?.message || "Authentication failed.");
    } finally {
      setBusy(false);
    }
  };

  return (
    <div className="auth-shell">
      <div className="auth-art">
        <div className="auth-logo">
          <div className="brand-mark">
            <CloudSun size={24} />
          </div>
          <b>WeatherGPT</b>
        </div>

        <div className="auth-copy">
          <p className="eyebrow">WEATHER, WITH CONTEXT</p>
          <h1>
            Know the forecast.
            <br />
            <em>Make better plans.</em>
          </h1>
          <p>
            Real-time weather data and an intelligent assistant in one calm,
            focused workspace.
          </p>
        </div>

        <div className="auth-art-footer">
          <span>Live weather</span>
          <span>7-day outlook</span>
          <span>AI advice</span>
        </div>
      </div>

      <div className="auth-panel">
        <div className="auth-box">
          <div className="mobile-auth-brand">
            <div className="brand-mark">
              <CloudSun size={22} />
            </div>
            <b>WeatherGPT</b>
          </div>

          <div className="auth-heading">
            <h2>
              {mode === "login" ? "Welcome back" : "Create your account"}
            </h2>
            <p>
              {mode === "login"
                ? "Sign in to your weather workspace."
                : "Set up your personal weather workspace."}
            </p>
          </div>

          {error && (
            <div
              className={`auth-message ${
                error.includes("created") ? "success" : ""
              }`}
            >
              {error}
            </div>
          )}

          <form onSubmit={submit}>
            {mode === "register" && (
              <label>
                Name
                <input
                  required
                  minLength="3"
                  maxLength="20"
                  value={form.username}
                  onChange={event =>
                    setForm({
                      ...form,
                      username: event.target.value
                    })
                  }
                  placeholder="Your name"
                />
              </label>
            )}

            {mode === "register" && (
              <label>
                Role
                <select
                  required
                  value={form.role}
                  onChange={event =>
                    setForm({
                      ...form,
                      role: event.target.value
                    })
                  }
                >
                  <option value="normal_user">Normal User</option>
                  <option value="marine">Marine</option>
                  <option value="aviation">Aviation</option>
                  <option value="agriculture">Agriculture</option>
                  <option value="research">Research</option>
                </select>
              </label>
            )}

            <label>
              Email
              <input
                required
                type="email"
                value={form.email}
                onChange={event =>
                  setForm({
                    ...form,
                    email: event.target.value
                  })
                }
                placeholder="you@example.com"
              />
            </label>

            {mode === "register" && (
              <label>
                Home location
                <input
                  value={form.homeLocation}
                  onChange={event =>
                    setForm({
                      ...form,
                      homeLocation: event.target.value
                    })
                  }
                  placeholder="e.g. Jalandhar"
                />
              </label>
            )}

            <label>
              Password
              <input
                required
                minLength="8"
                maxLength="20"
                type="password"
                value={form.password}
                onChange={event =>
                  setForm({
                    ...form,
                    password: event.target.value
                  })
                }
                placeholder="At least 8 characters"
              />
            </label>

            <button
              className="primary auth-submit"
              disabled={busy}
            >
              {busy ? (
                <>
                  <RefreshCw className="spin" size={17} />
                  Please wait
                </>
              ) : mode === "login" ? (
                <>
                  <LogIn size={17} />
                  Sign in
                </>
              ) : (
                <>
                  <UserPlus size={17} />
                  Create account
                </>
              )}
            </button>
          </form>

          <div className="auth-switch">
            {mode === "login"
              ? "New to WeatherGPT?"
              : "Already have an account?"}

            <button
              onClick={() => {
                setMode(mode === "login" ? "register" : "login");
                setError("");
              }}
            >
              {mode === "login" ? "Create an account" : "Sign in"}
            </button>
          </div>

          <p className="auth-note">
            <ShieldCheck size={14} />
            Your session is secured with JWT authentication.
          </p>
        </div>
      </div>
    </div>
  );
}

function Profile({ user, setUser, onLocationChanged }) {
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");

  const [form, setForm] = useState({
    username: "",
    email: "",
    role: "",
    homeLocation: "",
    workLocation: ""
  });

  useEffect(() => {
    let mounted = true;

    request("/api/v1/auth/profile")
      .then(data => {
        if (!mounted) return;

        const next = {
          username: cleanText(data?.username, ""),
          email: cleanText(data?.email, ""),
          role: cleanText(data?.role, ""),
          homeLocation: cleanText(data?.homeLocation, ""),
          workLocation: cleanText(data?.workLocation, "")
        };

        setForm(next);
        setUser(data);

        localStorage.setItem(USER_KEY, JSON.stringify(data));
      })
      .catch(error => {
        if (!mounted) return;
        setError(error?.message || "Unable to load profile.");
        setForm({
          username: cleanText(user?.username, ""),
          email: cleanText(user?.email, ""),
          role: cleanText(user?.role, ""),
          homeLocation: cleanText(user?.homeLocation, ""),
          workLocation: cleanText(user?.workLocation, "")
        });
      })
      .finally(() => {
        if (mounted) setLoading(false);
      });

    return () => {
      mounted = false;
    };
  }, [setUser, user?.email, user?.homeLocation, user?.role, user?.username, user?.workLocation]);

  const save = event => {
    event.preventDefault();

    /*
     * The supplied backend has no update-profile endpoint.
     * Do not pretend that a save succeeded. We save the useful local
     * preference and immediately use the new location for weather requests.
     */
    setSaving(true);
    setMsg("");
    setError("");

    const next = {
      ...user,
      username: form.username,
      email: form.email,
      role: form.role,
      homeLocation: form.homeLocation,
      workLocation: form.workLocation
    };

    localStorage.setItem(USER_KEY, JSON.stringify(next));
    setUser(next);

    if (form.homeLocation.trim()) {
      onLocationChanged(form.homeLocation.trim());
    }

    setMsg(
      "Preferences updated for this browser. The current backend does not expose an update-profile endpoint."
    );
    setSaving(false);
  };

  if (loading) {
    return (
      <div className="page">
        <div className="loading-card">
          <RefreshCw className="spin" size={20} />
          Loading profile...
        </div>
      </div>
    );
  }

  return (
    <div className="page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">ACCOUNT</p>
          <h1>Your profile</h1>
          <p className="muted">
            Your WeatherGPT preferences and locations.
          </p>
        </div>
      </div>

      {error && <div className="notice">{error}</div>}

      <div className="profile-layout">
        <div className="profile-card">
          <div className="large-avatar">
            {cleanText(form.username, "U").slice(0, 1).toUpperCase()}
          </div>

          <h2>{cleanText(form.username, "User")}</h2>
          <p>{cleanText(form.email, "")}</p>
          <span className="role-pill">
            {cleanText(form.role, "USER")}
          </span>
        </div>

        <form className="settings-card" onSubmit={save}>
          <h3>Personal details</h3>

          <label>
            Username
            <input
              required
              minLength="3"
              maxLength="20"
              value={form.username}
              onChange={event =>
                setForm({
                  ...form,
                  username: event.target.value
                })
              }
            />
          </label>

          <label>
            Email
            <input value={form.email} disabled />
          </label>

          <label>
            Home location
            <input
              value={form.homeLocation}
              onChange={event =>
                setForm({
                  ...form,
                  homeLocation: event.target.value
                })
              }
            />
          </label>

          <label>
            Work location
            <input
              value={form.workLocation}
              onChange={event =>
                setForm({
                  ...form,
                  workLocation: event.target.value
                })
              }
            />
          </label>

          <button className="primary" disabled={saving}>
            {saving ? "Saving..." : "Save preferences"}
          </button>

          {msg && <p className="muted small">{msg}</p>}
        </form>
      </div>
    </div>
  );
}

function SettingsPage() {
  return (
    <div className="page">
      <div className="page-heading">
        <div>
          <p className="eyebrow">PREFERENCES</p>
          <h1>Settings</h1>
          <p className="muted">
            A few simple controls for your workspace.
          </p>
        </div>
      </div>

      <div className="settings-card narrow">
        <div className="setting-row">
          <div>
            <b>Temperature</b>
            <span>Weather values are displayed in Celsius.</span>
          </div>
          <span className="setting-value">°C</span>
        </div>

        <div className="setting-row">
          <div>
            <b>Data source</b>
            <span>
              Weather data is requested through the WeatherGPT API.
            </span>
          </div>
          <span className="online">
            <i />
            Connected
          </span>
        </div>

        <div className="setting-row">
          <div>
            <b>Authentication</b>
            <span>
              JWT bearer authentication is used for protected requests.
            </span>
          </div>
          <ShieldCheck size={20} />
        </div>
      </div>
    </div>
  );
}

createRoot(document.getElementById("root")).render(<App />);
