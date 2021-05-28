# Getting Started

Spring Boot STOMP Server backed by an external Rabbit MQ Broker. The connection between application and Broker is fully encrypted with TLS v1.2 and only authenticated clients with a valid JWT (X-authorization) header in CONNECT frame are authorized to connect.





### Keystore Password
prvt1Key

### Key Password
bunnies

### Convert PEM to DER encoded private key

If you want the `DER` encoded version of your `PEM` private key.

`openssl rsa -outform der -in private.pem -out private.key`

### you can display the contents of a DER formatted certificate using this command:

`openssl x509 -in MYCERT.der -inform der -text`

### Convert .pem Certificate to DER encoded

`openssl x509 -outform der -in your-cert.pem -out your-cert.crt`

### Auth Tokn
eyJraWQiOiIxMjM0NSIsInR5cCI6IkpXVCIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiJzdG9tcC1jbGllbnQtbmciLCJhdWQiOiJzdG9tcC1jbGllbnQtbmciLCJwZXJtaXNzaW9ucyI6WyJBREQiXSwiaXNzIjoic3RvbXAtcmFiYml0LXNwcmluZy1zZXJ2ZXIiLCJleHAiOjE2MjI2OTcyNTgsImlhdCI6MTYyMjE5NzI1OCwianRpIjoiZmMxNDJlNjEtNDc2Yi00ODlkLWE5MjMtMmY0MjdiMjJlYjQwIn0.zuB6mINic-UadpG4p1_DSnEqzFOnn9zdZeYz6nFih62NbdVmKKdCwhCSCazz1aZCbYfsF9y21YUGODn4eOzFCfwFSIWepH13F_Ft8x4XiMfm9-iHDol0BqnS-enQXHZ8jYNWzy4khmM-cQ-2g4_j3RQaQWbn1MLAFW3nPS9Z-bEqIIn-6vROrMlLiK6U4t0wq8i9alWm0lCLiXy2Fq3lhDr2HY0lKEvsmj0VZRUzsL7EcnvaFdIYQzCYgA6qGnnlLuxNnEJpVN7e2cgRCLXjEK9lTyUCNIhH1xAGL5zzwKS5kpMGBd3ZlCwq22y0zlkYTTjus_BCqgNCMZr4jEz_Fg