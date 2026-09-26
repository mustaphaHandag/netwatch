# NetWatch

Plateforme de supervision reseau : une API Spring Boot qui surveille la disponibilite de services (sites, APIs, endpoints internes), pousse les changements de statut en temps reel via WebSocket, et sera deployee de bout en bout sur Kubernetes avec une infra provisionnee en Terraform et un pipeline CI/CD complet.

Ce projet reprend l'esprit de ce que j'ai developpe en stage chez Orange Wholesale (APIs RESTful, WebSocket temps reel, notifications), mais concu et deploye de A a Z, avec une vraie chaine cloud-native autour.

## Fonctionnalites (phase 1, en cours)

- CRUD des services a surveiller (`/api/services`)
- Verification periodique automatique (scheduler Spring) avec mesure du temps de reponse
- Historique des checks par service (`/api/services/{id}/checks`)
- Diffusion en temps reel des changements de statut via WebSocket (`/ws/status`)
- Endpoint `/actuator/health` et `/actuator/prometheus` prets pour le monitoring

## Stack technique

- **Backend** : Spring Boot 3, Spring Data JPA, Spring WebSocket, Bean Validation
- **Base de donnees** : H2 en local, PostgreSQL en production (Cloud SQL)
- **Conteneurisation** : Docker (build multi-stage), docker-compose pour le dev local
- **Orchestration** : manifests Kubernetes (`infra/k8s`)
- **Infrastructure as Code** : Terraform pour provisionner un cluster GKE Autopilot et une instance Cloud SQL (`infra/terraform`)
- **CI/CD** : GitHub Actions (`.github/workflows/ci.yml`)
- **Observabilite** : metriques exposees via Spring Actuator + Micrometer, a brancher sur Prometheus/Grafana

## Lancer le projet en local

```bash
mvn spring-boot:run
```

L'API demarre sur `http://localhost:8080` avec une base H2 en memoire (profil `local`, actif par defaut).

Creer un service a surveiller :

```bash
curl -X POST http://localhost:8080/api/services \
  -H "Content-Type: application/json" \
  -d '{"name": "Mon site", "url": "https://example.com", "checkIntervalSeconds": 60}'
```

## Lancer avec Docker

```bash
docker compose up --build
```

Ceci demarre l'API (profil `prod`) et une base PostgreSQL locale.

## Roadmap

- [x] Phase 1 : API CRUD + scheduler de checks + WebSocket + stockage
- [ ] Phase 2 : alertes email/webhook quand un service change de statut, gestion de l'intervalle par service
- [ ] Phase 3 : conteneurisation validee en local (kind/minikube) avant le cloud
- [ ] Phase 4 : provisioning Terraform du cluster GKE et de Cloud SQL
- [ ] Phase 5 : pipeline CI/CD complet (build image, push registry, deploiement automatique)
- [ ] Phase 6 : dashboards Prometheus/Grafana (latence, taux de disponibilite, incidents)

## Structure du depot

```
netwatch/
|-- src/                  # code Spring Boot
|-- infra/
|   |-- terraform/        # provisioning GCP (GKE + Cloud SQL)
|   `-- k8s/              # manifests de deploiement Kubernetes
|-- .github/workflows/    # pipeline CI/CD
|-- Dockerfile
`-- docker-compose.yml
```
