FROM node:22-alpine

WORKDIR /app

# Copy express-server
COPY ./express-server ./express-server

# Copy angular (client)
COPY ./angular/dist/pit-claim ./angular/dist/pit-claim

# Install server deps
WORKDIR /app/express-server
RUN npm clean-install --only=production

# Ensure the non-root user (e.g., node) owns the dist folder
RUN chown -R node:node /app/angular/dist

EXPOSE 8080

CMD ["node", "server.js"]