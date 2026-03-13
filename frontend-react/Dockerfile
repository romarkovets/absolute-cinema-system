FROM node:22-alpine
RUN addgroup -g 1001 -S appuser && adduser -S appuser -u 1001
WORKDIR /app
COPY package*.json ./
RUN npm install && chown -R appuser:appuser /app/node_modules
USER appuser
COPY --chown=appuser:appuser . .
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host"]