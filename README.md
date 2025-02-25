# Proyecto Backend para la gestion de Franquicias de Nequi

## Herramientas usadas
- Terraform BD: Infraestructura para crear una base de datos MySQL en un servicio RDS en AWS.
- HeidiSQL: Para la gestión de la base de datos.
- SQL WorkBench: Para ver el estado del servidor Base de datos.
- SpringBoot: Codificación del backend (WebFlux, Swagger, loombok... etc.).
- Docker: Para contener el proyecto y desplegarlo en cualquier entorno.

### Creación de la BD en AWS con Terraform
- terraform init
- terraform plan
- terraform apply

### Ejecución del proyecto en local con Docker
- docker build -t image-nequi-franquicias .
- docker run -d -p 8080:8080 --name container-nequi-franquicias image-nequi-franquicias

## Modelo de datos
![ModeloDeDatosNequiFranquicias](https://github.com/user-attachments/assets/3635b0ee-4691-473f-a9a3-074c72b5cc4c)

## Documentación de la Api
Se puede encontrar en la ruta /swagger-ui.html cuando se pone en marcha el servicio (Swagger)

## ¿Cómo ejecutar el proyecto? (ToDo)
### Consideraciones
- Tener AWS cli instalado y configurado
- Tener Terraform Cli Instalado
- Tener Docker instalado
- Mucha fe
