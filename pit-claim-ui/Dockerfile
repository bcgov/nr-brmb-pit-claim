FROM node:22-alpine

WORKDIR /app

# Copy express-server
COPY ./express-server ./express-server

# Copy angular (client)
COPY ./angular/dist/pit-claim-ui ./angular/dist/pit-claim-ui

# Install server deps
WORKDIR /app/express-server
RUN npm clean-install --only=production

EXPOSE 8080

CMD ["node", "server.js"]