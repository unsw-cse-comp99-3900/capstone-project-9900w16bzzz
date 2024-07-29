# Stage 1: Build the React app
FROM node:14 AS build

# Set the working directory
WORKDIR /app

# Stage 2: Serve the app with Nginx
FROM nginx:alpine

# Copy the built React app to Nginx's default directory
COPY ./build /usr/share/nginx/html

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/nginx.conf

# Expose port 8080
EXPOSE 8080

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
