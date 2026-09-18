# WeatherGPT Frontend

Vite + React frontend for WeatherGPT, including Overview, Forecast, WeatherGPT chat, Profile, Settings, and a Map workspace.

## Run

```bash
npm install
npm run dev
```

## Build

```bash
npm run build
```

## QGIS

The Map page works immediately with the OpenStreetMap base layer. To overlay your QGIS Server WMS layers, copy `.env.example` to `.env` and set:

```env
VITE_QGIS_WMS_URL=https://YOUR-QGIS-SERVER/qgisserver
VITE_QGIS_LAYERS=your_layer_name
```

For multiple WMS layers, use the layer names supported by your QGIS Server project, separated according to your server's WMS layer naming.

The frontend does not require a QGIS server just to start; the QGIS overlay is enabled when the environment variables are configured.

## Backend

The API base URL is currently the deployed WeatherGPT backend used by the supplied frontend:

`https://weathergpt-api-dgri.onrender.com`

Change the `API` constant in `src/main.jsx` if your backend URL changes.
