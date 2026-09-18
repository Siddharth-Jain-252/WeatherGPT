# WeatherGPT

WeatherGPT is an AI-powered weather application that combines real-time weather data with conversational interaction. Instead of navigating through multiple weather screens, users can ask questions in natural language and receive responses based on current or forecasted weather information.

The project uses a microservice-based backend with Spring Boot handling the main application logic and FastAPI handling AI-related operations.

## Features

* Natural-language weather queries
* Current weather information
* Weather forecasts
* Location-based weather search
* Current location support
* Conversational chat with recent message context
* AI-based intent detection
* Weather-related recommendations and alerts
* JWT-based authentication
* Role-based access control
* Redis caching for frequently requested weather data
* PostgreSQL for persistent storage
* Interactive map-based location selection
* Voice input support
* Docker-based development environment

## Architecture

```text
                         React Frontend
                              |
                              | REST API
                              v
                       Spring Boot API
                              |
              +---------------+---------------+
              |               |               |
              v               v               v
         PostgreSQL         Redis        Weather API
              |
              |
              +-----------------------+
                                      |
                                      v
                                FastAPI Service
                                      |
                                      v
                                  LLM / AI
```

### Spring Boot

Spring Boot acts as the primary backend service and is responsible for:

* Authentication and authorization
* JWT handling
* User management
* Weather API integration
* Redis caching
* PostgreSQL persistence
* Request validation
* Business logic
* Communication with the FastAPI service

### FastAPI

FastAPI is used as a separate AI service. It handles:

* User query analysis
* Intent detection
* Determining required weather capabilities
* LLM communication
* AI-generated responses

Keeping the AI layer separate from the main backend makes it easier to change or extend the LLM implementation without affecting the rest of the application.

## Request Flow

A typical weather chat request follows this flow:

```text
User
 |
 v
React Frontend
 |
 v
Spring Boot
 |
 +--> Authenticate request
 |
 +--> Send query to FastAPI
 |        |
 |        +--> Detect intent
 |        |
 |        +--> Determine required capabilities
 |
 +--> Retrieve required weather data
 |        |
 |        +--> Redis
 |        |
 |        +--> Weather API
 |
 +--> Send weather data to AI service
 |
 v
FastAPI / LLM
 |
 v
Generated response
 |
 v
Spring Boot
 |
 v
React Frontend
```

For example, a user might ask:

```text
"Should I carry an umbrella in Mumbai today?"
```

The AI service can identify this as a weather-advice request and determine that current weather and precipitation information are required. Spring Boot then retrieves the relevant data and the AI service generates the final response using that information.

## Technology Stack

### Frontend

* React
* JavaScript
* Vite
* Tailwind CSS
* React Router
* Browser Geolocation API
* Browser Speech Recognition API

### Backend

* Java
* Spring Boot
* Spring Security
* JWT
* REST APIs
* PostgreSQL
* Redis

### AI Service

* Python
* FastAPI
* LLM API

### Infrastructure

* Docker
* Docker Compose
* PostgreSQL
* Redis
* Adminer

## Project Structure

```text
WeatherGPT/
│
├── fastapi/
│   ├── app/
│   ├── requirements.txt
│   ├── Dockerfile
│   └── ...
│
├── spring/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       └── resources/
│   ├── pom.xml
│   ├── Dockerfile
│   └── ...
│
├── weathergpt-frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   └── ...
│
├── docker-compose.yml
├── .gitignore
└── README.md
```

## API Structure

The Spring Boot application exposes versioned REST endpoints.

### Authentication

```text
/api/v1/auth
```

Main operations include:

```text
POST /register
POST /login
```

### Weather

```text
/api/v1/weather
```

Available operations include:

```text
GET /current/city
GET /current/coor
GET /day/city
```

These endpoints support retrieving weather information using city names or geographic coordinates.

### LLM

```text
/api/v1/llm
```

The LLM-related endpoints are used for:

```text
Intent detection
Chat generation
Weather alerts
```

## Caching

Redis is used to cache weather responses and reduce repeated requests to the external weather service.

The basic caching flow is:

```text
Request
   |
   v
Spring Boot
   |
   v
Redis
   |
   +---- Cache Hit ----> Return cached data
   |
   +---- Cache Miss
              |
              v
         Weather API
              |
              v
        Store in Redis
              |
              v
         Return data
```

This reduces unnecessary external API requests and improves response times for frequently requested locations.

## Authentication and Security

Security is primarily handled by Spring Security.

The application uses:

* JWT-based authentication
* Password hashing
* Role-based authorization
* Protected API endpoints
* API-key authentication between Spring Boot and FastAPI
* Environment variables for sensitive configuration

Sensitive values such as API keys, database credentials, JWT secrets, and service authentication keys should not be committed to the repository.

## Running the Project

### Prerequisites

Make sure the following are installed:

* Git
* Docker
* Docker Compose
* Node.js and npm (for frontend development outside Docker)
* Java
* Python

### Clone the Repository

```bash
git clone https://github.com/Siddharth-Jain-252/WeatherGPT.git

cd WeatherGPT
```

### Environment Variables

Create the required environment configuration before starting the services.

Example:

```env
WEATHER_API_KEY=your_weather_api_key

LLM_API_KEY=your_llm_api_key

FASTAPI_API_KEY=your_fastapi_api_key

JWT_SECRET=your_jwt_secret
```

The exact variables required depend on the configuration of the individual services.

Do not commit `.env` files or API credentials to Git.

### Start with Docker Compose

Build and start the services:

```bash
docker compose up --build
```

To run them in the background:

```bash
docker compose up --build -d
```

Check the service status:

```bash
docker compose ps
```

Stop the services:

```bash
docker compose down
```

## Local Services

The Docker Compose configuration exposes the main services on the following ports:

| Service     | Port |
| ----------- | ---: |
| Spring Boot | 8081 |
| FastAPI     | 8000 |
| PostgreSQL  | 5433 |
| Adminer     | 8888 |

The React development server normally runs separately through Vite.

```bash
cd weathergpt-frontend
npm install
npm run dev
```

## Database

WeatherGPT uses PostgreSQL for persistent application data.

The development Docker configuration uses:

```text
Database: weather_app
User:     weather_user
```

Adminer is included for database administration.

Once the containers are running, open:

```text
http://localhost:8888
```

and connect using the PostgreSQL credentials configured in `docker-compose.yml`.

## Redis

Redis is used as the application's caching layer.

For local development, the Redis service is available to other Docker containers through:

```text
redis:6379
```

You can inspect Redis using:

```bash
docker exec -it weather-redis redis-cli
```

Then:

```text
PING
```

A successful connection returns:

```text
PONG
```

## Development

### Frontend

```bash
cd weathergpt-frontend

npm install

npm run dev
```

### Spring Boot

```bash
cd spring

./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

### FastAPI

Create and activate a virtual environment:

```bash
cd fastapi

python -m venv venv
```

Windows:

```bash
venv\Scripts\activate
```

Linux/macOS:

```bash
source venv/bin/activate
```

Install dependencies:

```bash
pip install -r requirements.txt
```

Start FastAPI:

```bash
uvicorn app.main:app --reload --port 8000
```

The exact module path may vary depending on the FastAPI entry point in the project.

## Deployment

The application is structured so that the frontend, Spring Boot backend, and FastAPI service can be deployed independently.

A production deployment can follow this structure:

```text
                 Client
                   |
                   v
            React Frontend
                   |
                   v
            Spring Boot API
             /     |      \
            /      |       \
           v       v        v
     PostgreSQL  Redis   FastAPI
                           |
                           v
                          LLM
```

This separation allows individual components to be updated or scaled without requiring the entire application to be redeployed.

## Future Improvements

Some areas that can be extended further:

* Improved conversational memory
* More detailed weather visualizations
* Historical weather data
* Severe weather notifications
* More weather providers
* Multi-language support
* Improved voice interaction
* Automated testing across services
* Centralized logging and monitoring
* CI/CD pipeline
* Better AI response validation
* Horizontal service scaling

## Repository

GitHub:

[WeatherGPT Repository](https://github.com/Siddharth-Jain-252/WeatherGPT?utm_source=chatgpt.com)

## Author

**Siddharth Jain**

B.Tech Computer Science and Engineering

GitHub:

[Siddharth Jain](https://github.com/Siddharth-Jain-252?utm_source=chatgpt.com)

## License

Add the appropriate license for the project before distributing it publicly. Also verify the licensing requirements of any third-party APIs, libraries, and AI models used by the application.
