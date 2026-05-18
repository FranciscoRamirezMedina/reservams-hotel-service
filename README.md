\# ReservaMS - Hotel Service



\## Descripcion



Este microservicio administra los hoteles de la cadena hotelera ReservaMS.



Permite crear hoteles, listar hoteles activos y asignar operadores a hoteles.



\## Responsabilidades



\- Crear hoteles.

\- Listar hoteles.

\- Buscar hoteles por ID.

\- Buscar hoteles por ciudad.

\- Desactivar hoteles.

\- Asignar operadores a hoteles.

\- Listar hoteles asignados a un operador.



\## Puerto



8083



\## Base de datos



reservams\_hotel\_db



\## Endpoints principales



\- GET /api/v1/hotels

\- GET /api/v1/hotels/active

\- GET /api/v1/hotels/{id}

\- POST /api/v1/hotels

\- PUT /api/v1/hotels/{id}

\- DELETE /api/v1/hotels/{id}

\- POST /api/v1/hotels/{hotelId}/operators

\- GET /api/v1/hotels/operator/{operatorUserId}



\## Ejecucion



1\. Crear la base de datos reservams\_hotel\_db.

2\. Ejecutar el script SQL ubicado en la carpeta database.

3\. Levantar Eureka Server.

4\. Ejecutar el hotel-service.

5\. Probar los endpoints desde Postman o desde el API Gateway.



