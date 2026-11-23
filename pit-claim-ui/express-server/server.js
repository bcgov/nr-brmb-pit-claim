const express = require('express');
const http = require('http');
const path = require('path');
const cors = require('cors');
const fs = require('fs');

const app = express();

// Host setup
const port = process.env.TOMCAT_PORT || 8080;
const hostname = "0.0.0.0";

const angularPath = path.join(__dirname, '../angular/dist/pit-claim/browser');

function startServer() {
	try {

		console.log(`Updating appConfig.json`);

		const configPath = path.join(angularPath, '/assets/data/appConfig.json');

		// Read the template and perform replacement in memory safely at startup
		let configTemplate = fs.readFileSync(configPath, 'utf8');
		let processedConfig = configTemplate
			.replace(/#\{BASE_URL\}#/g, process.env.BASE_URL)
			.replace(/#\{APPLICATION_ENVIRONMENT_NAME\}#/g, process.env.APPLICATION_ENVIRONMENT_NAME )
			.replace(/#\{CIRRAS_CLAIMS_REST_URI\}#/g, process.env.CIRRAS_CLAIMS_REST_URI)
			.replace(/#\{PIT_UNDERWRITING_UI_URL\}#/g, process.env.PIT_UNDERWRITING_UI_URL)
			.replace(/#\{KEYCLOAK_ISSUER_URI\}#/g, process.env.KEYCLOAK_ISSUER_URI)
			.replace(/#\{KEYCLOAK_AUTHORIZATION_ENDPOINT\}#/g, process.env.KEYCLOAK_AUTHORIZATION_ENDPOINT)
			.replace(/#\{KEYCLOAK_TOKEN_ENDPOINT\}#/g, process.env.KEYCLOAK_TOKEN_ENDPOINT)
			.replace(/#\{KEYCLOAK_LOGOUT_URL\}#/g, process.env.KEYCLOAK_LOGOUT_URL)
			.replace(/#\{KEYCLOAK_CLIENT_ID\}#/g, process.env.KEYCLOAK_CLIENT_ID);


		// Middleware
		app.use(cors());
		app.use(express.json());

		// Serve the modified config dynamically when requested
		console.log(`Serve the modified config dynamically`);
		app.get('/pub/pit-claim/assets/data/appConfig.json', (req, res) => {
			res.setHeader('Content-Type', 'application/json');
			res.send(processedConfig);
		});

		app.use('/pub/pit-claim', express.static(angularPath));

		// Send all requests to Angular app
		app.get('/pub/pit-claim/{*splat}', (req, res) => {
			res.sendFile(path.join(angularPath, 'index.html'));
		});

		const server = http.createServer(app); 
		
		// Bind to a Specific IP / Hostname (Crucial for Docker/Clouds)
		server.listen(port, hostname, () => {
			console.log(`angular app running on http://${hostname}:${port}`);
		});
		
	} catch (error) {
		console.error('Failed to initialize server:', error.message);
		// Exit process with failure status code so Docker detects container failure
		process.exit(1);
	};
};

startServer();

