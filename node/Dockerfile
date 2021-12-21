FROM node:17.2-alpine

COPY . /src
WORKDIR /src

RUN npm install -g @grpc/grpc-js@1.4.4 @grpc/proto-loader@0.6.7;

ENV NODE_PATH=/usr/local/lib/node_modules/
ENTRYPOINT ["node", "index.js"]