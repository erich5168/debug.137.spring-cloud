# debug.137.spring-cloud
Problem:
After calling POST `http://localhost:8012/actuator/busrefresh`

1.  Using old token still allow me to access: Api-User > Controller
2. Controller returns old token


## Get Started
1. `ConfigServer` > `application.properties`
Modify to your own git repo
```text
spring.cloud.config.server.git.username=
spring.cloud.config.server.git.password=
```

2. Start application sequence
 1.ConfigServer
 2. PhotoDiscovery
 3. Debug mode : ApiGateway 
   - Breakpoint: AuthorizationHeaderFilter | 133
 4. Debug mode : ApiUser
   - Breakpoint: AuthenticationFilter | 133
   - Breakpoint: UsersController | 37
 
3. Load `Segrey - Micro + AWS- sec 15~18.postman_collection.json` to POSTMAN
My URL endpoints might be slightly different.  This should make things a little easier

## How to test

Follow video 137.
