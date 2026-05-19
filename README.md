В приложении испоьлзуется **Secrets Plugin** и для работы приложения необходимо [`local.properties`](local.properties) указать нужные данные: `AUTH_SERVICE_BASE_API_URL`, `RECIPE_SERVICE_BASE_API_URL`, `REDIRECT_URL`, `CODE_CHALLENGE_METHOD`, и `REF_ID`.
Пример файла [`local.properties`](local.properties): 
```.properties
sdk.dir=...
AUTH_SERVICE_BASE_API_URL="https://YOUR_AUTH_SERVICE_EXAMPLE"
RECIPE_SERVICE_BASE_API_URL="https://YOUR_RECIPE_SERVICE_EXAMPLE"
REDIRECT_URL="YOUR_REDIRECT_URL"
CODE_CHALLENGE_METHOD="YOUR_CODE_CHALLENGE_METHOD"
REF_ID="YOUR_REF_ID"
```
