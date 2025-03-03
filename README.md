# Proyecto Backend para la gestion de Franquicias de Nequi

## Requerimientos

Se requiere construir un API para manejar una lista de franquicias. Una franquicia se
compone por un nombre y un listado de sucursales y, a su vez, una sucursal está
compuesta por un nombre y un listado de productos ofertados en la sucursal. Un producto
se componente de un nombre y una cantidad de stock.
### Criterios de aceptación
1. El proyecto debe ser desarrollado en Spring Boot.
2. Exponer endpoint para agregar una nueva franquicia.
3. Exponer endpoint para agregar una nueva sucursal a una franquicia.
4. Exponer endpoint para agregar un nuevo producto a una sucursal.
5. Exponer endpoint para eliminar un nuevo producto a una sucursal.
6. Exponer endpoint para modificar el stock de un producto.
7. Exponer endpoint que permita mostrar cual es el producto que más stock tiene por sucursal
para una franquicia puntual. Debe retornar un listado de productos que indique a que sucursal
pertenece.
8. Utilizar sistemas de persistencia de datos como Redis, MySql, MongoDB, Dynamo en algún
proveedor de Nube. Queda abierto a libre escogencia.

## Desarrollo del modelo de datos
![ModeloDeDatosNequiFranquicias](https://github.com/user-attachments/assets/3635b0ee-4691-473f-a9a3-074c72b5cc4c)

## Herramientas usadas
- Terraform BD: Infraestructura para crear una base de datos MySQL en un servicio RDS en AWS.
- HeidiSQL: Para la gestión de la base de datos.
- SQL WorkBench: Para ver el estado del servidor Base de datos.
- SpringBoot: Codificación del backend (WebFlux, Swagger, loombok... etc.).
- Docker: Para contener el proyecto y desplegarlo en cualquier entorno.
- AWS: RDS para BD. EC2 para alojar el API

## Documentación de la Api
Se puede encontrar en la ruta [url]/swagger-ui.html cuando se pone en marcha el servicio o directamente en el servicio desplegado en AWS (Sujeto a disponibilidad) http://54.242.196.147/swagger-ui/index.html

## ¿Cómo ejecutar el proyecto?
### Consideraciones
- Tener AWS cli instalado y configurado
- Tener Terraform Cli Instalado
- Tener Docker instalado
- Mucha fe

### Creación de la BD en AWS con Terraform
Si no se tiene una base de datos creada:
En la carpeta aws-rds-mysql abrir una consola y ejecutar los siguientes comandos
- terraform init
- terraform plan
- terraform apply

### Ejecución del proyecto en local con Docker
Con el servicio docker activo, en la carpeta raíz del proyecto abrir una consola y ejecutar los siguientes comandos:

Ejecutar todo el proyecto con un solo comando
- docker-compose up

Construir imagen:
- docker build -t image-nequi-franquicias .

Instanciar contenedor ajustando las variables de entorno
- docker run -d -p 8080:8080 --name container-nequi-franquicias -e MYSQL_URL="r2dbc:mysql://[URL]/nequi_franquicias" -e MYSQL_USER="admin" -e MYSQL_PASSWORD="contrasenamuysegura123" image-nequi-franquicias

o usando el archivo .env tambien sin olvidar ajustar las variables de entorno
- docker run -d -p 8080:8080 --name container-nequi-franquicias --env-file .env image-nequi-franquicias

Con esto queda desplegado el servicio en el puerto local 8080, para acceder a la documentación entras a localhost:8080/swagger-ui.html
