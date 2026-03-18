FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./

RUN npm ci

COPY angular.json ./
COPY tsconfig*.json ./
COPY src ./src

RUN npm run build --prod

FROM nginx:stable-alpine

COPY --from=build /app/dist/frontend-angular/browser /usr/share/nginx/html

RUN addgroup -g 1001 -S appuser && adduser -S appuser -u 1001 && \
    chown -R appuser:appuser /usr/share/nginx/html

USER appuser

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
