В приложении испоьлзуется **Secrets Plugin** и для работы приложения необходимо [`local.properties`](local.properties) указать нужные данные: `AUTH_SERVICE_BASE_API_URL` и `RECIPE_SERVICE_BASE_API_URL`.
Пример файла [`local.properties`](local.properties): 
```.properties
sdk.dir=...
AUTH_SERVICE_BASE_API_URL="https://YOUR_API_EXAMPLE:YOUR_AUTH_SERVICE_PORT"
RECIPE_SERVICE_BASE_API_URL="https://YOUR_API_EXAMPLE:YOUR_RECIPE_SERVICE_PORT"
```
