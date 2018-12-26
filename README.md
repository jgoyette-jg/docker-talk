# docker-talk

An example on integrating Spring Cloud comonents (Gateway and Eureka) with services and Keycloak using docker compose. There are not many great examples on integrating all of these components. A lot of them stop short of integrating both JWT and Oauth2 login via OIDC with Keycloak. Enjoy!
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. 

### Prerequisites

* Your favorite IDE
* Gradle
* Docker (if you want to use docker compose)
* Mapping /etc/hosts file for keycloak -> 127.0.0.1 and registry -> 127.0.0.1

### Installing

* cd or dir into docker-talk
* run build-all.sh
* Create folder for postgres database data and replace <YOUR_USER> with your current user
  * /Users/<YOUR_USER>/postgres:/var/lib/postgresql
* from the same directory run docker-compose -f docker-compose.yml up
* go to keycloak:8080 and add a user under the test realm
* verify it is up by going to localhost:8762/login
