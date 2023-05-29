# health-master
El Administrador de Aplicaciones es una herramienta diseñada para administrar y controlar aplicaciones Java en entornos de servidor. Proporciona funcionalidades para arrancar, detener, verificar el estado y gestionar aplicaciones Java, ya sean aplicaciones Spring Boot o servidores Tomcat y GlassFish, 
incluso puede gestionar individualmente las aplicaciones que corren dentro de los servidores Tomcat y Glafish.
tambien se puede adminsitrar el cliclo de vida de aplicaciones que no son java, como mongoDb, activemQ.

### Caracteristicas
- Arranque y detención de aplicaciones: El administrador permite iniciar y detener aplicaciones de manera sencilla y rápida, ya que expone servicios Rest para ejecutar estos comandos.
- Estado de las aplicaciones: Proporciona información en tiempo real sobre el estado de las aplicaciones administradas. Puede verificar si una aplicación está en ejecución o detenida, lo que permite monitorear y administrar el estado de las aplicaciones de manera eficiente.
- Listar Archivos Logs: permite listar los archivos txt de logs generados por las aplicaciones, para poder descargarlos.
- Descarga de Logs: Además de administrar las aplicaciones en ejecución, el administrador permite descargar los archivos de logs  generados para su análisis o respaldo.
- Conexión SSH: Utilizando una conexión SSH, el administrador se conecta al servidor donde se encuentran las aplicaciones y realiza las operaciones necesarias. Esto proporciona un mecanismo seguro para administrar las aplicaciones sin necesidad de acceder directamente al servidor.

### Tecnologías utilizadas
* JDK 1.8
* Maven 3
* Spring - Boot 2.7


### Configuration project
- Clonar proyecto : git-clone
- Usuario Confianza: 
- Permisos: 

### Documentación de la API

Puedes encontrar la documentación completa de la API en el siguiente enlace:

- [Swagger API Documentation](http://localhost:8080/swagger-ui/index.html)  
Nota: Si no se muestra la documentación es por que se encuentra abajo la aplicacion.
La documentación incluye detalles sobre los endpoints, los modelos de datos, los parámetros y las respuestas esperadas.
### Diagramas
- diagrama de clase
![diagramClasss.png](diagramas%2FdiagramClasss.png)
- diagrama de flujo
![diagramFlujo.png](diagramas%2FdiagramFlujo.png)
### Ultimas actualizaciones