const express = require('express');
const http = require('http');
const path = require('path');
const cors = require('cors');
const fs = require('fs');

const app = express();

// Host setup
const port = process.env.PORT || 8080;
const hostname = "0.0.0.0";

const angularPath = path.join(__dirname, '../angular/dist/pit-claim');

function setEnvironmentValues() {
	// Env values to replace in appConfig.json
	// This code updates the file only once, when the Node server starts
	
	console.log(`Updating appConfig.json`);
	
	const configPath = path.join(angularPath, '/assets/data/appConfig.json');

	let config = fs.readFileSync(configPath, 'utf8');

	config = config
		.replace(/#\{BASE_URL\}#/g, process.env.BASE_URL)
		.replace(/#\{APPLICATION_ENVIRONMENT_NAME\}#/g, process.env.APPLICATION_ENVIRONMENT_NAME )
		.replace(/#\{CIRRAS_CLAIMS_REST_URI\}#/g, process.env.CIRRAS_CLAIMS_REST_URI)
		.replace(/#\{PIT_UNDERWRITING_UI_URL\}#/g, process.env.PIT_UNDERWRITING_UI_URL)
		.replace(/#\{WEBADE_OAUTH2_AUTHORIZE_URL\}#/g, process.env.WEBADE_OAUTH2_AUTHORIZE_URL)
		.replace(/#\{CIRRAS_CLAIMS_UI\}#/g, process.env.CIRRAS_CLAIMS_UI)
		.replace(/#\{WEBADE_OAUTH2_SCOPES\}#/g, process.env.WEBADE_OAUTH2_SCOPES)
		.replace(/#\{WEBADE_OAUTH2_ENABLE_CHECKTOKEN\}#/g, process.env.WEBADE_OAUTH2_ENABLE_CHECKTOKEN )
		.replace(/#\{UI_CHECKTOKEN_ENDPOINT\}#/g, process.env.UI_CHECKTOKEN_ENDPOINT )
		.replace(/#\{WEBADE_OAUTH2_SITEMINDER_URL\}#/g, process.env.WEBADE_OAUTH2_SITEMINDER_URL );

	fs.writeFileSync(configPath, config);

	console.log(`Updated appConfig.json`);
};

function startServer() {
	try {
		setEnvironmentValues();
		
		// Middleware
		app.use(cors());
		app.use(express.json());

		//// Serve static files from the Angular app
		// app.use('/pub/wfprev', express.static(path.join(__dirname, 'dist/wfprev')));

		app.use(express.static(angularPath));

		// Send all requests to Angular app
		app.get((req, res) => {
			res.sendFile(path.join(angularPath, 'index.html'));
		});

		// Listen on {port} and {hostname} to be accessible from public IP address
		// app.listen(port, () => {
		// 	console.log(`Server running on http://localhost:${port}`);
		// });

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


