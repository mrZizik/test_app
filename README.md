## API
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
/api/oauth?client_id=&scope&redirect_url
returns token in fragment
``` 
