## API
Needed to signup new client, and generate secret for it. 

```
/api/register_client 
POST {"client_id": %your_id%}
return {"client_secret": %your_secret%}
```


Signs up user and return token to make requests
```
/api/signup
POST {"email": %your_email%, "password": %password%}
return {"token":%user token%}
```

Returns user by user token or client access token
```
/api/get_user/%token%
GET
returns {"email": %user_email%}
```

Generates token for client that is assigned to user. Allows client app make requests to user data.
```
/api/client_access_request
POST {"client_id": %client_id%, "client_secret": %client_secret%, "scope":%scope%}
returns {"token", %client_token%}
``` 
