#
# Build stage
#
FROM node:alpine AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

#
# Package stage
#
FROM nginx:alpine
COPY --from=build app/dist/ usr/share/nginx/html/
COPY /nginx.conf /etc/nginx/conf.d/default.conf
