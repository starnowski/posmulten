FROM node:14

COPY package.json project/
COPY package-lock.json project/
COPY convert.js project/

WORKDIR project
RUN npm install
