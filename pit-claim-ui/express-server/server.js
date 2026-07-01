const express = require('express');
const http = require('http');
const path = require('path');
const cors = require('cors');
const bodyParser = require('body-parser');
const fs = require('fs');

const app = express();

// Host setup
const port = process.env.PORT || 8080;
//const hostname = "0.0.0.0";

// Env values to replace in appConfig.json
// This code updates the file once, when the Node server starts
// needs a bit of tweaking - right now it has trouble replacing WEBADE_OAUTH2_SCOPES
const configPath = path.join(__dirname, '../angular/dist/pit-claim/assets/data/appConfig.json');

try {

	let config = fs.readFileSync(configPath, 'utf8');

	config = config
		.replace(/#\{BASE_URL\}#/g, process.env.BASE_URL)
		.replace(/#\{ENV\}#/g, process.env.ENV )
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
    //console.log(`ENV: ${config.environment}`);
} catch (err) {
    console.error(`Failed to update ${configPath}:`, err.message);
}

// Middleware
app.use(cors());
// app.use(bodyParser.json());

//// Serve static files from the Angular app
// app.use('/pub/wfprev', express.static(path.join(__dirname, 'dist/wfprev')));

const angularPath = path.join(__dirname, '../angular/dist/pit-claim');
app.use( express.static(angularPath));


// Send all requests to Angular app
app.get((req, res) => {
    res.sendFile(path.join(angularPath, 'index.html'));
});

// const server = http.createServer(app);

// Listen on {port} and {hostname} to be accessible from public IP address
app.listen(port, () => {
   console.log(`Server running on http://localhost:${port}`);
});
