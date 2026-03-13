FROM node:22-alpine
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY index.html ./
COPY vite.config.js ./
COPY src ./src
COPY public ./public
RUN addgroup -g 1001 -S appuser && adduser -S appuser -u 1001 && \
    chown -R appuser:appuser /app/node_modules && \
    chown appuser:appuser /app && \
    chmod 755 /app
USER appuser
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]