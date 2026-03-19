FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY angular.json ./
COPY tsconfig*.json ./
COPY src ./src

RUN npm run build --prod

FROM nginx:stable-alpine

COPY --from=build /app/dist/frontend-angular/browser /usr/share/nginx/html

RUN chown -R nginx:nginx /usr/share/nginx/html && \
    chmod -R 755 /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
