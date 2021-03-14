[![Build](https://github.com/achmudas/music-service/actions/workflows/build.yml/badge.svg?branch=main)](https://github.com/achmudas/music-service/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=achmudas_music-service&metric=alert_status)](https://sonarcloud.io/dashboard?id=achmudas_music-service)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=achmudas_music-service&metric=coverage)](https://sonarcloud.io/dashboard?id=achmudas_music-service)
# music-service

Service to search and store users favorite artist. Also top 5 albums are retrieved and stored for 
each artist.

### Launching with docker
#### Prereqs
* Java 11
* Docker

#### Build and launch
```bash
./mvnw clean install
docker build -t music-service:latest .
docker run -p 8080:8080 music-service:latest 
```

#### Access API
http://localhost:8080/swagger-ui.html
