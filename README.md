# CPS406-TMS

## Running Backend

The WTServer must be run with a set of environment variables tht are used to
configure the warehouse.

First is `TOTAL_DOCKING_AREAS` which must be an integer greater than 1. As the name suggests
it determines how many docking areas are available to use at the same time.

Remaining environment variables are related to the admin portal.
- `AUTH_KEY` - The encryption key that is used to sign the JWT tokens. This key should not be shared publicly.
- `AUTH_USERNAME` - The username that will be used to log into the Admin portal.
- `AUTH_PASSWORD` - The password that will be used to log into the Admin Portal.

Here is an example `.env` file that can be used to run the server.
```
TOTAL_DOCKING_AREAS=2
AUTH_KEY=top_secret_key
AUTH_USERNAME=admin
AUTH_PASSWORD=password
```

====

**IntelliJ IDEA:** 
You can use the [EnvFile plugin](https://plugins.jetbrains.com/plugin/7861-envfile) to add
your environment variables in `.env` in your run configuration.

**VS Code:**
Similarly, you can configure your [run configuration](https://code.visualstudio.com/docs/editor/debugging) to
include the `.env` file.

====

## Running Frontend
The frontend is a react service that can be easily started with `npm start`. There is no configuration required
for the frontend.